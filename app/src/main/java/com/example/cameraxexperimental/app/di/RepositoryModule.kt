package com.example.cameraxexperimental.app.di

import com.example.cameraxexperimental.data.remote.CameraImageRepository
import com.example.cameraxexperimental.data.remote.ICameraImageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module
class RepositoryModule {

    @Provides
    fun provideCameraImageRepository(): ICameraImageRepository = CameraImageRepository()
}