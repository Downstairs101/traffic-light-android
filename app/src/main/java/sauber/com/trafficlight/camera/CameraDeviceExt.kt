package sauber.com.trafficlight.camera

import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CaptureRequest
import android.view.Surface

fun CameraDevice.setupPreviewSession(surfaceTexture: SurfaceTexture): SessionStateCallback {
    val surface = Surface(surfaceTexture)
    val previewRequest = buildPreviewCaptureRequest(this, surface)
    val sessionStateCallback = SessionStateCallback()
    createCaptureSession(listOf(surface), getStateCallback(previewRequest, sessionStateCallback), null)

    return sessionStateCallback
}

private fun getStateCallback(previewRequest: CaptureRequest, stateCallback: SessionStateCallback) =
    object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            session.setRepeatingRequest(previewRequest, null, null)
            stateCallback.onConfigure(session)
        }

        override fun onConfigureFailed(session: CameraCaptureSession) {
            stateCallback.onConfigureFailed(session)
        }
    }

private fun buildPreviewCaptureRequest(cameraDevice: CameraDevice, surface: Surface): CaptureRequest {
    return cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        .apply {
            set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
            addTarget(surface)
        }.build()
}