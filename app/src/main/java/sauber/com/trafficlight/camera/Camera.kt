package sauber.com.trafficlight.camera

import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.params.StreamConfigurationMap
import android.util.Size
import sauber.com.trafficlight.extensions.backCameraId

class Camera(private val context: Context) {

    private var success: (CameraSettings) -> Unit = {}
    private var fail: (CameraSettings) -> Unit = {}

    private fun cameraManager() = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    fun openRearCamera(success: (CameraSettings) -> Unit) {
        openRearCamera(success, {})
    }

    fun openRearCamera(success: (CameraSettings) -> Unit, fail: (CameraSettings) -> Unit) {
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


    private val listener = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            success(CameraSettings(camera))
        }

        override fun onDisconnected(camera: CameraDevice) {
            //see if an action is necessary
        }

        override fun onError(camera: CameraDevice, error: Int) {
            fail(CameraSettings(camera))
        }
    }

    fun getCameraSettings(cameraDevice: CameraDevice) = CameraSettings(cameraDevice)

    inner class CameraSettings(private val cameraDevice: CameraDevice) {

        fun setupPreviewSession(surfaceTexture: SurfaceTexture) {
            cameraDevice.setupPreviewSession(surfaceTexture)
        }

        fun sensorOrientation(): Int {
            val sensorOrientation = getSensorOrientation()
            return sensorOrientation ?: 0
        }

        fun getSupportedSizes() = getScalarMap()?.getOutputSizes(SurfaceTexture::class.java)?.toList() ?: listOf()

        private fun getScalarMap(): StreamConfigurationMap? {
            return getCameraCharacteristics().get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        }

        private fun getSensorOrientation() =
            getCameraCharacteristics().get(CameraCharacteristics.SENSOR_ORIENTATION)


        private fun getCameraCharacteristics() = cameraManager().getCameraCharacteristics(cameraDevice.id)
    }
}
