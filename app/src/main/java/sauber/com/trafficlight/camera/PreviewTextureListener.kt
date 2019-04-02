package sauber.com.trafficlight.camera

import android.graphics.SurfaceTexture
import android.view.TextureView

class PreviewTextureListener {

    companion object {
        fun listen(surfaceAvailable: () -> Unit) = SurfaceTextureListener(surfaceAvailable)

        class SurfaceTextureListener constructor(val surfaceAvailable: () -> Unit) :
            TextureView.SurfaceTextureListener {

            override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
                surfaceAvailable()
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {

            }

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {

            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean = false

        }

    }

}