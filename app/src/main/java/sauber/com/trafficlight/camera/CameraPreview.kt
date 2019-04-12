package sauber.com.trafficlight.camera

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.view.TextureView
import sauber.com.trafficlight.camera.PreviewSettings.Companion.INVERTED

class CameraPreview(context: Context, attributes: AttributeSet) : TextureView(context, attributes) {

    var previewSettings: PreviewSettings? = null

    fun setSurfaceTextureListener(surfaceAvailable: (surfaceTexture: SurfaceTexture) -> Unit) {
        surfaceTextureListener = SurfaceListener(surfaceAvailable)
    }


    private inner class SurfaceListener(val surfaceAvailable: (surfaceTexture: SurfaceTexture) -> Unit) : SurfaceTextureListener {

        override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
            previewSettings?.also {
                if (it.getDimensionState() == INVERTED) {
                    surfaceTexture.setDefaultBufferSize(height, width)
                } else {
                    surfaceTexture.setDefaultBufferSize(width, height)
                }

                surfaceAvailable(surfaceTexture)
            }
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {

        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {

        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean = false


    }
}