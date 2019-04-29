package sauber.com.trafficlight.picture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.camerakit.CameraKitView
import kotlinx.android.synthetic.main.fragment_camera.*
import sauber.com.trafficlight.CameraCallbacks
import sauber.com.trafficlight.R


class CameraFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureActionBar()
        setupCameraListeners()
    }

    private fun setupCameraListeners() {
        cameraPreview.cameraListener = object : CameraKitView.CameraListener {
            override fun onOpened() {
                cameraCallbacks().opened()
            }

            override fun onClosed() {
            }
        }

        cameraPreview.setErrorListener { cameraKitView, cameraException ->
            cameraKitView.onStop()
            cameraCallbacks().error(cameraException)
        }
    }

    override fun onStart() {
        super.onStart()
        cameraPreview.onStart()
    }

    override fun onResume() {
        super.onResume()
        cameraPreview.onResume()
    }

    override fun onPause() {
        cameraPreview.onPause()
        super.onPause()
    }

    override fun onStop() {
        cameraCallbacks().closed()
        cameraPreview.onStop()
        super.onStop()
    }

    private fun configureActionBar() {
        toolbarIcon.setImageResource(R.drawable.ic_outline_close)
        toolbarIcon.setOnClickListener {
            appCompatActivity().onBackPressed()
        }
    }

    private fun appCompatActivity() = (activity as AppCompatActivity)

    private fun cameraCallbacks() = appCompatActivity() as CameraCallbacks

//    private fun setCameraButtonListener(camera: CameraDevice) {
////        button.setOnClickListener {
////            val cameraCapture = CameraCapture()
////            cameraCapture.setOnCaptureListener {
////                //                val intent = Intent(context, ViewImageClass::class.java)
//////                intent.putExtra("image", it)
//////                startActivity(intent)
////            }
////
////            cameraCapture.createStillCaptureSession(camera, cameraPreview.height, cameraPreview.width)
////        }
//
//    }
}
