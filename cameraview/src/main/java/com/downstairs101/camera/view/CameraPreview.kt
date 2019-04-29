package com.downstairs101.camera.view

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.view.TextureView
import com.downstairs101.camera.view.camera.Camera

class CameraPreview(context: Context, attributes: AttributeSet) : TextureView(context, attributes) {

    var camera: Camera = Camera(context)

    fun start() {
        surfaceTextureListener = SurfaceListener({})
    }

    private inner class SurfaceListener(val surfaceAvailable: (surfaceTexture: SurfaceTexture) -> Unit) :
        SurfaceTextureListener {

        override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
            camera.openRearCamera { cameraDevice ->
                val previewSize = optimizePreviewSize(cameraDevice)
                surfaceTexture.setDefaultBufferSize(previewSize.width, previewSize.height)
                cameraDevice.setupPreviewSession(surfaceTexture)
            }

            surfaceAvailable(surfaceTexture)
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {

        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean = false


    }

    private fun optimizePreviewSize(cameraSettings: Camera.CameraSettings) =
        PreviewSize().getFitsPreviewSize(this, cameraSettings)
}