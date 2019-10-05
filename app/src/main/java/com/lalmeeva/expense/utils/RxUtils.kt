package com.lalmeeva.expense.utils

import com.lalmeeva.expense.base.view.ProgressableView
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class RxUtils {
    companion object {

        fun <T> applySchedulersSingle(): SingleTransformer<T, T> {
            return SingleTransformer {
                it.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }

        fun <T> applyProgressSingle(view: ProgressableView?): SingleTransformer<T, T> {
            return SingleTransformer { it ->
                it.doOnSubscribe { view?.showProgress() }
                    .doOnError { Timber.e(it) }
                    .doFinally { view?.hideProgress() }
            }
        }
    }
}