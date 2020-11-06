package com.example.cameraxexperimental.data.remote

import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.math.roundToInt

class CameraImageRepository @Inject constructor(

) : ICameraImageRepository {

    companion object {
        private const val MAX_IMAGE_SIZE = 1024f
    }

    override suspend fun keepImage(image: Bitmap) {
        Log.d("CameraImageRepository", "Keeping image: ${image.height} x ${image.width}")

        try {
            val scaledBitmap = resize(image)
            Log.d("CameraImageRepository", "Image resized - new size: ${scaledBitmap.height} x ${scaledBitmap.width}")
            val compressed = compress(scaledBitmap)
            Log.d("CameraImageRepository", "Image compressed, sending to queue")
            enqueue(compressed)

        } catch (e: Exception) {

        }
    }

    private fun resize(image: Bitmap) : Bitmap {
        try {
            val ratio =
                (MAX_IMAGE_SIZE / image.width).coerceAtMost(MAX_IMAGE_SIZE / image.height)
            val width = (ratio * image.width).roundToInt()
            val height = (ratio * image.height).roundToInt()
            return Bitmap.createScaledBitmap(image, width, height, true)
        } catch (e: Exception) {
            Log.e("CameraImageRepository", "Failed to resize image (ID:${image.generationId})!")
            throw e
        }
    }

    private fun compress(image: Bitmap) : ByteArray {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 50, stream)
        return stream.toByteArray()
    }

    private fun enqueue(bytes: ByteArray) {
        try {
            Log.d("CameraImageRepository", "Sending compressed image to queue")
        } catch (e: Exception) {
            Log.d("CameraImageRepository", "Failed to queue image")
        }
    }


}