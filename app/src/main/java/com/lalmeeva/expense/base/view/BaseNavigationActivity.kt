package com.lalmeeva.expense.base.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination

abstract class BaseNavigationActivity : AppCompatActivity(), ChildView, NavController.OnDestinationChangedListener {

    protected lateinit var navController: NavController

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        navController.addOnDestinationChangedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || true.also { navigateToParent() }
    }

    override fun navigateToParent() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        navController.removeOnDestinationChangedListener(this)
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        supportActionBar?.title = destination.label
    }
}