package sauber.com.trafficlight.camera

import android.content.ContentValues
import android.content.Context
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView

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

    private fun optimizePreviewSize(cameraSettings: Camera.CameraSettings): Size {
        val previewSize = getFitsPreviewSize(cameraSettings)

        val sensorOrientation = cameraSettings.sensorOrientation()
        val displayOrientation = display.rotation
        val sizeState = getSizeState(displayOrientation, sensorOrientation)

        return alignSize(Size(previewSize.width, previewSize.height), sizeState)
    }

    private fun getFitsPreviewSize(cameraSettings: Camera.CameraSettings): Size {
        return cameraSettings.getSupportedSizes().first { it.width == it.height }
    }

    private fun alignSize(size: Size, sizeState: Int): Size {
        if (sizeState == INVERTED)
            return Size(size.height, size.width)

        return Size(size.width, size.height)
    }

    companion object {
        const val NORMAL = 0
        const val INVERTED = 1
    }

    private fun getSizeState(displayOrientation: Int, sensorOrientation: Int): Int {
        when (displayOrientation) {
            Surface.ROTATION_0, Surface.ROTATION_180 -> {
                if (sensorOrientation == 90 || sensorOrientation == 270) return INVERTED
            }

            Surface.ROTATION_90, Surface.ROTATION_270 -> {
                if (sensorOrientation == 0 || sensorOrientation == 180) return INVERTED
            }

            else -> {
                Log.e(ContentValues.TAG, "Display rotation is invalid: $displayOrientation")
            }
        }
        return NORMAL
    }
}