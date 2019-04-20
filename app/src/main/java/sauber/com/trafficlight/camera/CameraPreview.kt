package sauber.com.trafficlight.camera

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.util.Size
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

    private fun optimizePreviewSize(cameraSettings: Camera.CameraSettings) = getFitsPreviewSize(cameraSettings)

    private fun getFitsPreviewSize(cameraSettings: Camera.CameraSettings): Size {
        val supportedSizes = cameraSettings.getSupportedSizes()
        var size: Size? = null

        if (supportedSizes.any { matchesAspectRatio(it) }) {
            size = successorViewSize(supportedSizes)
            size ?: predecessorViewSize(supportedSizes)
        }

        return size ?: supportedSizes.first()
    }

    private fun successorViewSize(supportedSizes: List<Size>): Size? {
        return supportedSizes
            .filter { matchesAspectRatio(it) && sizeIsBiggerThanView(it) }
            .minBy { area(it.width, it.height) }
    }

    private fun predecessorViewSize(supportedSizes: List<Size>): Size? {
        return supportedSizes
            .filter { matchesAspectRatio(it) && sizeIsMinorThanView(it) }
            .maxBy { it.width * it.height }
    }

    private fun area(width: Int, height: Int) = width * height

    private fun sizeIsBiggerThanView(size: Size) = size.width >= width && size.height >= height

    private fun sizeIsMinorThanView(size: Size) = size.width <= width && size.height <= height

    private fun matchesAspectRatio(size: Size) = size.width * aspectRatio().height == size.height * aspectRatio().width

    private fun aspectRatio() = Size(1, 1)
}