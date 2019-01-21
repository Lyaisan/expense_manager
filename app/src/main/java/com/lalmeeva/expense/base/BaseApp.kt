package com.lalmeeva.expense.base

import android.app.Application
import com.lalmeeva.expense.base.di.AppComponent
import com.lalmeeva.expense.base.di.AppModule
import com.lalmeeva.expense.base.di.DaggerAppComponent
import com.lalmeeva.expense.data.source.BaseRepositoryModule

class BaseApp : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .baseRepositoryModule(BaseRepositoryModule())
            .build()
    }
}