package com.lalmeeva.expense.screens.main

interface CameraView {
    fun setBarcodesCount(count: Int)
    fun showProgress()
    fun hideProgress()
    fun showResult(result: String?)
    fun showError(message: String?)
}