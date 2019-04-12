package sauber.com.trafficlight.camera

import android.content.ContentValues
import android.util.Log
import android.view.Surface

class PreviewSettings(private val sensorOrientation: Int, private val displayOrientation: Int) {

    companion object {
        const val NORMAL = 0
        const val INVERTED = 1
    }

    fun getDimensionState(): Int {
        when (displayOrientation) {
            Surface.ROTATION_0, Surface.ROTATION_180 -> {
                if (sensorOrientation == 90 || sensorOrientation == 270) return INVERTED
            }

            Surface.ROTATION_90, Surface.ROTATION_270 -> {
                if (sensorOrientation == 0 || sensorOrientation == 180) return INVERTED
            }

            else -> {
                Log.e(ContentValues.TAG, "Display rotation is invalid: $displayOrientation")
            }
        }
        return NORMAL
    }
}