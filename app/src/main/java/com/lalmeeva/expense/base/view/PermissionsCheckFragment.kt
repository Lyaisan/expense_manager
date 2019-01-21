package com.lalmeeva.expense.base.view

import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment

abstract class PermissionsCheckFragment: Fragment() {

    companion object {
        const val PERMISSION_REQ_CODE = 250
    }

    protected abstract var permissions: Array<String>

    override fun onResume() {
        super.onResume()
        handleRequest()
    }

    open fun handleRequest() {
        when {
            hasPermissions(permissions) -> onPermissionGranted()
            else -> requestPermissions(permissions)
        }
    }

    private fun hasPermissions(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (!hasPermission(permission)) {
                return false
            }
        }

        return true
    }

    private fun hasPermission(permission: String): Boolean {
        return context?.let {
            PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(it, permission)
        }?: false
    }

    open fun requestPermissions(permissions: Array<String>) {
        requestPermissions(permissions, PERMISSION_REQ_CODE)
    }

    protected abstract fun onPermissionDenied()

    protected abstract fun onPermissionGranted()
}