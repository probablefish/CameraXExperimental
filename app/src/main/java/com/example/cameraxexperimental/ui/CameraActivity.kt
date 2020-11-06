package com.example.cameraxexperimental.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.cameraxexperimental.NavigationViewModel
import com.example.cameraxexperimental.R
import com.example.cameraxexperimental.Screen
import com.example.cameraxexperimental.app.IFragmentsTransactionsManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CameraActivity : AppCompatActivity() {

    private val navigationViewModel: NavigationViewModel by viewModels()
    
    @Inject
    lateinit var fragmentsTransactionsManager: IFragmentsTransactionsManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_activity)
        if (savedInstanceState == null) {
            add(
                fragment = CameraPreviewFragment.newInstance(),
                tag = getString(R.string.camera_preview_title),
                titleId = R.string.camera_preview_title
            )
        }

        navigationViewModel.observable().observe(this, Observer {
            when (it) {
                is Screen.CameraPreview -> {
                    onBackPressed()
                    replace(
                        fragment = CameraPreviewFragment.newInstance(),
                        tag = getString(R.string.camera_preview_title),
                        titleId = R.string.camera_preview_title,
                        addToBackStack = false
                    )
                }
                
                is Screen.ImageViewer -> {
                    replace(
                        fragment = CameraImageViewerFragment.newInstance(),
                        tag = getString(R.string.camera_imageviewer_title),
                        titleId = R.string.camera_imageviewer_title,
                        addToBackStack = true
                    )
                }
            }
        })
    }

    private fun add(fragment: Fragment, tag: String, titleId: Int) {
        supportActionBar?.run {
            setTitle(titleId)
        }
        supportFragmentManager.beginTransaction()
            .add(R.id.navigation_container, fragment, tag)
            .commit()
    }

    private fun replace(fragment: Fragment, tag: String, titleId: Int, addToBackStack: Boolean) {
        supportActionBar?.run {
            setTitle(titleId)
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.navigation_container, fragment, tag)
            .apply {
                if (addToBackStack) {
                    addToBackStack(tag)
                }
            }
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportActionBar?.run {
            title = supportFragmentManager.fragments.last().tag
        }
    }
}