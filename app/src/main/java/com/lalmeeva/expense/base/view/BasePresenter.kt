package com.lalmeeva.expense.base.view

import androidx.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<T> {

    protected var view: T? = null

    var disposable = CompositeDisposable()

    @CallSuper
    open fun attachView(view: T) {
        this.view = view
    }

    @CallSuper
    open fun detachView() {
        this.view = null
        disposable.clear()
    }

    @CallSuper
    open fun onStart() {

    }

    @CallSuper
    open fun onStop() {

    }

    @CallSuper
    open fun destroy() {
        disposable.clear()
    }
}