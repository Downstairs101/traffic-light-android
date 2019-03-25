package sauber.com.trafficlight.extensions

import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import java.lang.IllegalStateException

fun CameraManager.backCameraId(): String {
    for (cameraId in cameraIdList) {
        val characteristics = getCameraCharacteristics(cameraId)
        val cameraDirection = characteristics.get(CameraCharacteristics.LENS_FACING)

        if (cameraDirection != null && cameraDirection == CameraCharacteristics.LENS_FACING_BACK) {
            return cameraId
        }
    }

    throw IllegalStateException("Can't find a camera")
}