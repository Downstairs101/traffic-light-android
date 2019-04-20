package sauber.com.trafficlight.camera

import android.content.ContentValues
import android.graphics.SurfaceTexture
import android.util.Log
import android.util.Size
import android.view.Surface

fun SurfaceTexture.setDefaultBufferSize(size: Size) {
    this.setDefaultBufferSize(size.width, size.height)

//    private fun alignSize(size: Size, sizeState: Int): Size {
//        if (sizeState == INVERTED)
//            return Size(size.height, size.width)
//
//        return Size(size.width, size.height)
//    }
//
//    companion object {
//        const val NORMAL = 0
//        const val INVERTED = 1
//    }
//
//    private fun getSizeState(displayOrientation: Int, sensorOrientation: Int): Int {
//        when (displayOrientation) {
//            Surface.ROTATION_0, Surface.ROTATION_180 -> {
//                if (sensorOrientation == 90 || sensorOrientation == 270) return INVERTED
//            }
//
//            Surface.ROTATION_90, Surface.ROTATION_270 -> {
//                if (sensorOrientation == 0 || sensorOrientation == 180) return INVERTED
//            }
//
//            else -> {
//                Log.e(ContentValues.TAG, "Display rotation is invalid: $displayOrientation")
//            }
//        }
//        return NORMAL
//    }
}