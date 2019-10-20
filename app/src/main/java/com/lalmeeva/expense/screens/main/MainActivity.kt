package com.lalmeeva.expense.screens.main

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.lalmeeva.expense.R
import com.lalmeeva.expense.base.view.BaseNavigationActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_progress.*

class MainActivity : BaseNavigationActivity(), MainView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        init()
    }

    private fun init() {
        setSupportActionBar(mainToolbar)
        navController = findNavController(R.id.main_nav_host)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun showProgress() {
        progressView.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressView.visibility = View.GONE
    }
}
