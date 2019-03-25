package sauber.com.trafficlight


import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_camera.*
import sauber.com.trafficlight.extensions.backCameraId

class CameraFragment : Fragment() {

    private val surfaceListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
            openCamera()
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {}

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?) = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }


    override fun onResume() {
        super.onResume()

        if (cameraPreview.isAvailable) {
            openCamera()
        } else {
            cameraPreview.surfaceTextureListener = surfaceListener
        }
    }

    private fun openCamera() {
        try {
            cameraManager().openCamera(cameraManager().backCameraId(), cameraCallback, null)
        } catch (ex: SecurityException) {
            ex.printStackTrace()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private val cameraCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            createPreviewSession(camera)
        }

        override fun onDisconnected(camera: CameraDevice) {

        }

        override fun onError(camera: CameraDevice, error: Int) {

        }

    }

    private fun createPreviewSession(cameraDevice: CameraDevice) {
        try {
            val surface = Surface(getSurfaceTexture())


            val previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            previewRequestBuilder.addTarget(surface)


            cameraDevice.createCaptureSession(listOf(surface),
                    object : CameraCaptureSession.StateCallback() {

                        override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                            previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)

                            val previewRequest = previewRequestBuilder.build()
                            cameraCaptureSession.setRepeatingRequest(previewRequest, null, null)
                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) {

                        }
                    }, null)
        } catch (e: CameraAccessException) {
            Log.e(null, e.toString())
        }

    }

//    private fun openBackgroundThread() {
//        val backgroundThread = HandlerThread("camera_background_thread")
//        backgroundThread.start()
//        backgroundHandler = Handler(backgroundThread.looper)
//    }

    private fun cameraManager() = context?.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    private fun getSurfaceTexture(): SurfaceTexture? {
        val texture = cameraPreview.surfaceTexture
        texture.setDefaultBufferSize(cameraPreview.width, cameraPreview.height)

        return texture
    }

}
