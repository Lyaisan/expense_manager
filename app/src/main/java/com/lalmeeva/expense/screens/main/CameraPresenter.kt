package com.lalmeeva.expense.screens.main

import androidx.collection.ArraySet
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.lalmeeva.expense.base.view.BasePresenter
import com.lalmeeva.expense.data.source.BillRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class CameraPresenter @Inject constructor(private val billRepository: BillRepository) :
    BasePresenter<CameraView>() {
    private val barcodesDetected: ArraySet<String> = ArraySet()

    fun receiveDetections(detectedItems: List<FirebaseVisionBarcode>) {
        if (detectedItems.isNotEmpty()) {
            val displayValue = detectedItems[0].rawValue
            synchronized(barcodesDetected) {
                barcodesDetected.add(displayValue)
                view?.setBarcodesCount(barcodesDetected.size)
            }
        }
    }

    fun parseBills() {
        if (!barcodesDetected.isEmpty()) {
            disposable.add(billRepository.parseBills(ArraySet<String>(barcodesDetected))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view?.showProgress() }
                .doOnError { view?.showError(it?.message) }
                .doFinally {
                    barcodesDetected.clear()
                    view?.hideProgress()
                }
                .subscribe({ view?.showResult(it?.toString()) }, { Timber.e(it) })
            )
        } else {
            view?.showError("Nothing to send!")
        }
    }

    fun clearBarcodes() {
        barcodesDetected.clear()
        view?.setBarcodesCount(0)
    }
}