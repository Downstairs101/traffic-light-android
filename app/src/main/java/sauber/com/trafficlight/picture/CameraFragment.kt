package sauber.com.trafficlight.picture


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_camera.*
import sauber.com.trafficlight.R


class CameraFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureActionBar()
        cameraPreview.start()
    }

    private fun configureActionBar() {
        toolbarIcon.setImageResource(R.drawable.ic_outline_close)
        toolbarIcon.setOnClickListener {
            appCompatActivity().onBackPressed()
        }
    }

    private fun appCompatActivity() = (activity as AppCompatActivity)

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
