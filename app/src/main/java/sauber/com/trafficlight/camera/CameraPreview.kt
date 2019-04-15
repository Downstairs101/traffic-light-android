package sauber.com.trafficlight.camera

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.util.Size
import android.view.TextureView
import sauber.com.trafficlight.camera.PreviewSettings.Companion.INVERTED

class CameraPreview(context: Context, attributes: AttributeSet) : TextureView(context, attributes) {

    var previewSettings: PreviewSettings? = null
    var alignedSize = Size(0, 0)

    fun setSurfaceTextureListener(surfaceAvailable: (surfaceTexture: SurfaceTexture) -> Unit) {
        surfaceTextureListener = SurfaceListener(surfaceAvailable)
    }


    private inner class SurfaceListener(val surfaceAvailable: (surfaceTexture: SurfaceTexture) -> Unit) :
        TextureView.SurfaceTextureListener {

        override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
            alignedSize = alignSize(width, height)

            surfaceTexture.setDefaultBufferSize(alignedSize.width, alignedSize.height)
            surfaceAvailable(surfaceTexture)
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {

        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {

        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean = false


    }

    private fun alignSize(width: Int, height: Int): Size {
        previewSettings?.also {
            if (it.getDimensionState() == INVERTED) return Size(height, width)
        }

        return Size(width, height)
    }
}