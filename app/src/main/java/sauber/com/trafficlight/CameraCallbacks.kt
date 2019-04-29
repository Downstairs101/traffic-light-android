package sauber.com.trafficlight

import com.camerakit.CameraKitView

interface CameraCallbacks {
    fun opened()

    fun closed()

    fun error(exception: CameraKitView.CameraException)
}