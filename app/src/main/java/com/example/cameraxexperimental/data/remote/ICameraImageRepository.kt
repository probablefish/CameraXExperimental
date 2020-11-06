package com.example.cameraxexperimental.data.remote

import android.graphics.Bitmap

interface ICameraImageRepository {
    suspend fun keepImage(image: Bitmap)
}