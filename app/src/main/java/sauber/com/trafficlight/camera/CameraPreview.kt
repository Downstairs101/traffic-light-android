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

        return supportedSizes.firstOrNull {
            it.width * aspectRatio().height == it.height * aspectRatio().width
        } ?: supportedSizes.first()
    }

    private fun aspectRatio() = Size(1, 1)
}