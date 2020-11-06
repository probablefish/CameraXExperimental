package com.example.cameraxexperimental.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.cameraxexperimental.NavigationViewModel
import com.example.cameraxexperimental.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.camera_imageviewer_fragment.*

@AndroidEntryPoint
class CameraImageViewerFragment : Fragment() {

    companion object {
        fun newInstance() =
            CameraImageViewerFragment()
    }

    val cameraViewModel by activityViewModels<CameraViewModel>()
    val navigationViewModel by activityViewModels<NavigationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpOnBackPressed()
    }

    private fun setUpOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            cameraViewModel.clearImage()
            this.isEnabled = false
            requireActivity().onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.camera_imageviewer_fragment, container, false)


    override fun onStart() {
        super.onStart()
        val image = cameraViewModel.getImage()
        imageView.setImageBitmap(image)

        camera_imageviewer_keep_btn.setOnClickListener {
            keepImage()
            returnToPreview()
        }

        camera_imageviewer_retake_btn.setOnClickListener {
            returnToPreview()
        }
    }

    private fun keepImage() {
        uploadImage()
        informUploading()
    }

    private fun returnToPreview() {
        requireActivity().onBackPressed()
    }

    private fun uploadImage() = cameraViewModel.sendImage()

    private fun informUploading() {
        Toast.makeText(
            requireContext(),
            "Uploading image",
            Toast.LENGTH_SHORT
        ).show()
    }


}