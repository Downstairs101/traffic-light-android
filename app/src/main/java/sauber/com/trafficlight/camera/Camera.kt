package sauber.com.trafficlight.camera

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import sauber.com.trafficlight.extensions.backCameraId

class Camera(private val context: Context) {

    private var success: (CameraDevice) -> Unit = {}
    private var fail: (CameraDevice) -> Unit = {}

    fun openRearCamera(success: (CameraDevice) -> Unit) {
        openRearCamera(success, {})
    }

    fun openRearCamera(success: (CameraDevice) -> Unit, fail: (CameraDevice) -> Unit) {
        this.success = success
        this.fail = fail

        try {
            cameraManager().openCamera(cameraManager().backCameraId(), listener, null)
        } catch (ex: SecurityException) {
            ex.printStackTrace()
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }
    }

    fun backCameraId() = cameraManager().backCameraId()

    fun sensorOrientation(cameraId: String): Int {
        val sensorOrientation = getSensorOrientation(cameraId)
        return sensorOrientation ?: 0
    }


    private fun getSensorOrientation(cameraId: String) =
        getCameraCharacteristics(cameraId).get(CameraCharacteristics.SENSOR_ORIENTATION)

    private fun getCameraCharacteristics(cameraId: String) = cameraManager().getCameraCharacteristics(cameraId)


    fun getScalerMap(cameraId: String) =
        getCameraCharacteristics(cameraId).get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

    private val listener = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            success(camera)
        }

        override fun onDisconnected(camera: CameraDevice) {
            //see if an action is necessary
        }

        override fun onError(camera: CameraDevice, error: Int) {
            fail(camera)
        }
    }

    private fun cameraManager() = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
}
