package com.lalmeeva.expense.base.view

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

abstract class PermissionsCheckFragment: Fragment() {

    companion object {
        const val PERMISSION_REQ_CODE = 250
    }

    protected abstract var permissions: Array<String>

    override fun onStart() {
        super.onStart()
        handleRequest()
    }

    open fun handleRequest() {
        when {
            hasPermissions(permissions) -> onPermissionGranted()
            else -> requestPermissions(permissions)
        }
    }

    private fun hasPermissions(permissions: Array<out String>): Boolean {
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQ_CODE -> {
                if (hasPermissions(permissions)) onPermissionGranted()
                else onPermissionDenied()
            }
        }
    }

    protected abstract fun onPermissionDenied()

    protected abstract fun onPermissionGranted()
}