package com.downstairs101.camera.view.camera

import android.hardware.camera2.CameraCaptureSession

class SessionStateCallback {
    private var success: (CameraCaptureSession) -> Unit = {}
    private var fail: (CameraCaptureSession) -> Unit = {}

    fun setStateCallbackListener(success: (CameraCaptureSession) -> Unit, fail: (CameraCaptureSession) -> Unit) {
        this.success = success
        this.fail = fail
    }

    fun onConfigure(cameraCaptureSession: CameraCaptureSession){
        success(cameraCaptureSession)
    }

    fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession){
        fail(cameraCaptureSession)
    }

}
