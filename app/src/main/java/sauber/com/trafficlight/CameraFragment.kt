package sauber.com.trafficlight


import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraDevice
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_camera.*
import sauber.com.trafficlight.camera.*


class CameraFragment : Fragment() {
    private lateinit var camera: Camera

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        context?.also { camera = Camera(it) }
        cameraPreview.previewSettings =
            PreviewSettings(camera.sensorOrientation(camera.backCameraId()), deviceOrientation())
    }

    override fun onResume() {
        super.onResume()

        if (cameraPreview.isAvailable) {
            camera.openRearCamera { cameraOpened(it) }
        } else {
            cameraPreview.setSurfaceTextureListener { surfaceTexture ->
                openBackCamera(surfaceTexture)
            }
        }
    }

    private fun openBackCamera(surfaceTexture: SurfaceTexture) {
        camera.openRearCamera {
            it.setupPreviewSession(surfaceTexture)
        }
    }

    private fun cameraOpened(camera: CameraDevice) {
        setCameraButtonListener(camera)
    }

    private fun setCameraButtonListener(camera: CameraDevice) {
        button.setOnClickListener {
            val cameraCapture = CameraCapture()
            cameraCapture.setOnCaptureListener {
                //                val intent = Intent(context, ViewImageClass::class.java)
//                intent.putExtra("image", it)
//                startActivity(intent)
            }

            cameraCapture.createStillCaptureSession(camera, cameraPreview.height, cameraPreview.width)
        }

    }

    private fun deviceOrientation(): Int {
        val rotation = activity?.windowManager?.defaultDisplay?.rotation
        return rotation ?: 0
    }
}
