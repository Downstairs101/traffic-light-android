package sauber.com.trafficlight.picture


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.camerakit.CameraKitView
import kotlinx.android.synthetic.main.fragment_camera.*
import sauber.com.trafficlight.CameraCallbacks
import sauber.com.trafficlight.R
import java.io.File
import java.io.FileOutputStream

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
                cameraCallbacks().closed()
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

    fun stillPicture() {
        cameraPreview.captureImage { cameraKitView, capturedImage ->
            cameraKitView.previewResolution
            parseToBitMap(capturedImage)
        }
    }

    private fun parseToBitMap(capturedImage: ByteArray) {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        val originalImage = BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.size, null)
        val jpegImage = bitmapToJPEG(originalImage)

        val intent = Intent(Intent.ACTION_SEND)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(jpegImage))
        intent.type = "image/jpeg"

        startActivity(intent)
    }


    private fun bitmapToJPEG(bitmap: Bitmap): File {
        val file = File(externalCacheStoragePath(), "traffic-light.jpeg")

        return compressToPng(bitmap, file)
    }

    private fun externalCacheStoragePath(): String {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        return context?.externalCacheDir.toString()
    }

    private fun compressToPng(bitmap: Bitmap, file: File): File {
        val fileOutput = FileOutputStream(file)
        if (file.setReadable(true, true)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutput)
        }
        return file
    }

    private fun appCompatActivity() = (activity as AppCompatActivity)

    private fun cameraCallbacks() = appCompatActivity() as CameraCallbacks
}
