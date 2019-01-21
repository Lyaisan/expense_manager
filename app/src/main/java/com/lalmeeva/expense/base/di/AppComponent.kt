package com.lalmeeva.expense.base.di

import com.lalmeeva.expense.data.source.BaseRepositoryModule
import com.lalmeeva.expense.screens.main.CameraFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, BaseRepositoryModule::class])
interface AppComponent {
    fun inject(cameraFragment: CameraFragment)
}