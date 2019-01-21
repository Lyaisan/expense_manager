package com.lalmeeva.expense.base.view

import android.content.Context

abstract class BaseFragment<ContextView, FragmentView, Presenter: BasePresenter<FragmentView>>: PermissionsCheckFragment() {
    var contextView: ContextView? = null

    protected open lateinit var presenter: Presenter

    override fun onAttach(context: Context) {
        inject()

        super.onAttach(context)

        contextView = context as ContextView
        presenter.attachView(this as FragmentView)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onDetach() {
        super.onDetach()
        contextView = null
        presenter.detachView()
    }

    open fun inject() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.destroy()
    }
}