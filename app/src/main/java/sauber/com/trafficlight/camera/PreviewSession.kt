package sauber.com.trafficlight.camera

import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CaptureRequest
import android.view.Surface

class PreviewSession {

    fun createPreviewSession(surfaceTexture: SurfaceTexture, cameraDevice: CameraDevice) {
        val surface = Surface(surfaceTexture)

        cameraDevice.createCaptureSession(
            listOf(surface), object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                    val previewRequest = buildPreviewCaptureRequest(cameraDevice, surface)

                    cameraCaptureSession.setRepeatingRequest(previewRequest, null, null)
                    //Todo: free camera button to take a picture
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    //Todo: Block camera button, show a friendly screen that tells the user that the camera is down
                }
            }, null
        )
    }

    private fun buildPreviewCaptureRequest(cameraDevice: CameraDevice, surface: Surface): CaptureRequest {
        return cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            .apply {
                set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                addTarget(surface)
            }.build()
    }
}