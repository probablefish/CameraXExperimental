package com.example.cameraxexperimental.ui

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.cameraxexperimental.NavigationViewModel
import com.example.cameraxexperimental.R
import com.example.cameraxexperimental.Screen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.camera_preview_fragment.*
import java.lang.Exception
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class CameraPreviewFragment : Fragment() {

    companion object {
        fun newInstance() =
            CameraPreviewFragment()

        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        private var flashModeIndex = 0
        private val FLASH_MODES = arrayOf(ImageCapture.FLASH_MODE_AUTO, ImageCapture.FLASH_MODE_OFF, ImageCapture.FLASH_MODE_ON)
    }

    val cameraViewModel by activityViewModels<CameraViewModel>()
    val navigationViewModel by activityViewModels<NavigationViewModel>()

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.camera_preview_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (cameraPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }

        camera_take_picture.setOnClickListener {
            takePicture()
        }

        btn_flashmode.setOnClickListener {
            cycleFlashMode()
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (cameraPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    activity?.baseContext,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                activity?.finish()
            }
        }
    }

    private fun cameraPermissionsGranted() =
        REQUIRED_PERMISSIONS.all { activity?.checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED }

    private fun requestPermissions() {
        activity?.requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }

    private fun startCamera() {
        Log.d("CAMERA_PREVIEW_FRAGMENT", "startCamera()")
        val context = activity?.baseContext

        if (context != null) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({

                // val resolution = Size(1024, 768)

                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                    .build()
                    .also {
                        it.setSurfaceProvider(viewFinder.createSurfaceProvider())
                    }

                imageCapture = ImageCapture.Builder()
                    .setTargetRotation(Surface.ROTATION_270)
                    .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                    .build()

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()

                    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                } catch (e: Exception) {
                    Log.e("CAMERA_PREVIEW_FRAGMENT", "Use case binding failed", e)
                }
            }, ContextCompat.getMainExecutor(context))
        }
    }

    private fun takePicture() {
        Log.d("CAMERA_PREVIEW_FRAGMENT", "takePicture()")

        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(cameraExecutor, imageCapturedCallback)
    }

    private val imageCapturedCallback = object : OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            Log.d("CAMERA_PREVIEW_FRAGMENT", "Image capture success - Image - format: ${image.format} height: ${image.height} width: ${image.width}")

            val buffer = image.planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)
            val bmp = BitmapFactory.decodeByteArray(bytes, 0 , bytes.size)

            super.onCaptureSuccess(image)

            cameraViewModel.update(bmp)
            navigationViewModel.goTo(Screen.ImageViewer)
        }

        override fun onError(exception: ImageCaptureException) {
            super.onError(exception)
            Log.e("CAMERA_PREVIEW_FRAGMENT", "Image capture error: ", exception)
        }
    }

    private fun cycleFlashMode() {
        when {
            flashModeIndex < FLASH_MODES.size - 1 -> flashModeIndex++
            else -> flashModeIndex = 0
        }

        when (val mode = FLASH_MODES[flashModeIndex]) {
            ImageCapture.FLASH_MODE_AUTO -> setFlashMode(mode, R.drawable.ic_auto_flash)
            ImageCapture.FLASH_MODE_OFF -> setFlashMode(mode, R.drawable.ic_no_flash)
            ImageCapture.FLASH_MODE_ON -> setFlashMode(mode, R.drawable.ic_flash)
        }
    }

    private fun setFlashMode(mode: Int, icon: Int) {
        setFlashButtonDrawable(icon)
        imageCapture?.flashMode = mode
    }


    private fun setFlashButtonDrawable(drawable: Int) {
        btn_flashmode.setImageDrawable(ResourcesCompat.getDrawable(requireActivity().resources, drawable, null))
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }


}