package sauber.com.trafficlight.camera

import android.content.Context
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import sauber.com.trafficlight.extensions.backCameraId

class Camera(private val context: Context) {
    private lateinit var openedCamera: (CameraDevice) -> Unit

    fun openRearCamera(success: (CameraDevice) -> Unit) {
        try {
            openedCamera = success
            cameraManager().openCamera(cameraManager().backCameraId(), listener, null)
        } catch (ex: SecurityException) {
            ex.printStackTrace()
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }
    }

    private fun cameraManager() = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    private val listener = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            openedCamera(camera)
        }

        override fun onDisconnected(camera: CameraDevice) {
            //see if an action is necessary
        }

        override fun onError(camera: CameraDevice, error: Int) {
            //todo: Show friendly screen to tells user that he cant use camera feature
        }
    }

}