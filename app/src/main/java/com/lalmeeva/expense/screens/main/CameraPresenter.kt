package com.lalmeeva.expense.screens.main

import androidx.collection.ArraySet
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.lalmeeva.expense.base.view.BasePresenter
import com.lalmeeva.expense.data.source.BillRepository
import com.lalmeeva.expense.utils.RxUtils
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
                .compose(RxUtils.applySchedulersSingle())
                .compose(RxUtils.applyProgressSingle(view))
                .doFinally { barcodesDetected.clear() }
                .subscribe({ view?.showResult(it?.toString()) }, {
                    view?.showError(it?.message)
                    Timber.e(it)
                })
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