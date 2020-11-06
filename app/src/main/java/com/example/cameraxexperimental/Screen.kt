package com.example.cameraxexperimental

sealed class Screen {
    object CameraPreview: Screen()
    object ImageViewer: Screen()
}