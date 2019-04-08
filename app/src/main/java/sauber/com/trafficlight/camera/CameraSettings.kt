package sauber.com.trafficlight.camera

import android.content.ContentValues
import android.util.Log
import android.view.Surface

class CameraSettings {

    fun areDimensionsSwapped(sensorOrientation: Int, displayOrientation: Int): Boolean {
        when (displayOrientation) {
            Surface.ROTATION_0, Surface.ROTATION_180 -> {
                return sensorOrientation == 90 || sensorOrientation == 270
            }
            Surface.ROTATION_90, Surface.ROTATION_270 -> {
                return sensorOrientation == 0 || sensorOrientation == 180
            }
            else -> {
                Log.e(ContentValues.TAG, "Display rotation is invalid: $displayOrientation")
            }
        }
        return false
    }
}