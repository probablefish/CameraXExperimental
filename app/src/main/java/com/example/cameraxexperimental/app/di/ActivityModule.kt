package com.example.cameraxexperimental.app.di

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.cameraxexperimental.app.FragmentsTransactionsManager
import com.example.cameraxexperimental.app.IFragmentsTransactionsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
class ActivityModule {

    @Provides
    fun fragmentManager(activity: Activity): FragmentManager =
        (activity as AppCompatActivity).supportFragmentManager

    @Provides
    fun provideFragmentTransactionManager(fragmentManager: FragmentManager): IFragmentsTransactionsManager =
        FragmentsTransactionsManager(fragmentManager)

}