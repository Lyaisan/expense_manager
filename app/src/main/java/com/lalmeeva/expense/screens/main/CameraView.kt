package com.lalmeeva.expense.screens.main

import com.lalmeeva.expense.base.view.ProgressableView

interface CameraView : ProgressableView {
    fun setBarcodesCount(count: Int)
    fun showResult(result: String?)
    fun showError(message: String?)
}