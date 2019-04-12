package sauber.com.trafficlight.camera

import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CaptureRequest

class PreviewStateCallback(private val captureRequest: CaptureRequest) : CameraCaptureSession.StateCallback() {
    private var success: (CameraCaptureSession) -> Unit = {}
    private var fail: (CameraCaptureSession) -> Unit = {}

    fun setStateCallbackListener(success: (CameraCaptureSession) -> Unit, fail: (CameraCaptureSession) -> Unit) {
        this.success = success
        this.fail = fail
    }

    override fun onConfigureFailed(session: CameraCaptureSession) {
        session.setRepeatingRequest(captureRequest, null, null)
        success(session)
    }

    override fun onConfigured(session: CameraCaptureSession) {
        fail(session)
    }

}


