package sauber.com.trafficlight.camera

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.view.TextureView

class CameraPreview(context: Context, attributes: AttributeSet) : TextureView(context, attributes) {

    fun setSurfaceTextureListener(surfaceAvailable: (surfaceTexture: SurfaceTexture, width: Int, height: Int) -> Unit) {
        surfaceTextureListener = SurfaceListener(surfaceAvailable)
    }

    private inner class SurfaceListener(val surfaceAvailable: (surfaceTexture: SurfaceTexture, width: Int, height: Int) -> Unit)
        : TextureView.SurfaceTextureListener {

        override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
            surfaceAvailable(surfaceTexture, width, height)
        }


        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {

        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {

        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean = false


    }
}