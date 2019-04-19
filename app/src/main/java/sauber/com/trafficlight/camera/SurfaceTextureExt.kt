package sauber.com.trafficlight.camera

import android.graphics.SurfaceTexture
import android.util.Size

fun SurfaceTexture.setDefaultBufferSize(size: Size) {
    this.setDefaultBufferSize(size.width, size.height)
}