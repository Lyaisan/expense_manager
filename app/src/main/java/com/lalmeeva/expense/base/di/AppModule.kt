package com.lalmeeva.expense.base.di

import android.content.Context
import com.lalmeeva.expense.base.BaseApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: BaseApp) {

    @Provides
    @Singleton
    fun provideContext(): Context = app
}