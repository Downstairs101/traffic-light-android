package sauber.com.trafficlight.camera

import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CaptureRequest
import android.view.Surface


fun CameraDevice.setupPreviewCaptureSession(surfaceTexture: SurfaceTexture): PreviewStateCallback {
    val surface = Surface(surfaceTexture)
    val captureRequest = buildCaptureRequest(this, surface)
    val previewStateCallback = PreviewStateCallback(captureRequest)

    createCaptureSession(listOf(surface), previewStateCallback, null)

    return previewStateCallback
}


private fun buildCaptureRequest(cameraDevice: CameraDevice, surface: Surface): CaptureRequest {
    return cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        .apply {
            set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
            addTarget(surface)
        }.build()
}
