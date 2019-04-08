package sauber.com.trafficlight.camera

import android.graphics.SurfaceTexture

fun SurfaceTexture.setDefaultBufferSize(width: Int, height: Int, swapDimension: Boolean) {
    if (swapDimension) {
        setDefaultBufferSize(height, width)
    } else
        setDefaultBufferSize(width, height)
}