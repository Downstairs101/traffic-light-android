package sauber.com.trafficlight


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.media.ImageReader
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_camera.*
import sauber.com.trafficlight.camera.PreviewTextureListener
import sauber.com.trafficlight.extensions.backCameraId
import java.io.File
import java.io.FileOutputStream


class CameraFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onResume() {
        super.onResume()

        if (cameraPreview.isAvailable) {
            openCamera()
        } else {
            cameraPreview.surfaceTextureListener = PreviewTextureListener.listen { openCamera() }
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
            cameraOpened(camera)
        }

        override fun onDisconnected(camera: CameraDevice) {
            //see if an action is necessary
        }

        override fun onError(camera: CameraDevice, error: Int) {
            //todo: Show friendly screen to tells user that he cant use camera feature
        }

    }

    private fun cameraOpened(camera: CameraDevice) {
        createPreviewSession(camera)
        setCameraButtonListener(camera)
    }

    private fun setCameraButtonListener(camera: CameraDevice) {
        button.setOnClickListener { createStillCaptureSession(camera) }

    }

    private fun createStillCaptureSession(cameraDevice: CameraDevice) {
        val imageReaderSurface = getImageReader().surface
        val stillCaptureSession = createStillCaptureRequest(cameraDevice, imageReaderSurface)

        cameraDevice.createCaptureSession(listOf(imageReaderSurface), object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                session.capture(stillCaptureSession, object : CameraCaptureSession.CaptureCallback() {}, null)
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                Log.d("CONFIGURED", "falhou aqui em")
            }

        }, null)

    }

    private fun createPreviewSession(cameraDevice: CameraDevice) {
        val surface = Surface(getSurfaceTexture())
        val previewRequest = buildPreviewCaptureRequest(cameraDevice, surface)

        cameraDevice.createCaptureSession(
            listOf(surface), object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                    cameraCaptureSession.setRepeatingRequest(previewRequest, null, null)
                    //Todo: free camera button to take a picture
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    //Todo: Block camera button, show a friendly screen that tells the user that the camera is down
                }
            }, null
        )
    }

    private fun createStillCaptureRequest(cameraDevice: CameraDevice, surface: Surface): CaptureRequest {
        return cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            .apply {
                //set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START)
                set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                addTarget(surface)
            }.build()
    }

    private fun buildPreviewCaptureRequest(cameraDevice: CameraDevice, surface: Surface): CaptureRequest {
        return cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            .apply {
                set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                addTarget(surface)
            }.build()
    }


    private fun getImageReader() = ImageReader
        .newInstance(cameraPreview.width, cameraPreview.height, ImageFormat.JPEG, 2)
        .apply {
            setOnImageAvailableListener({
                val builder = StrictMode.VmPolicy.Builder()
                StrictMode.setVmPolicy(builder.build())

                val buffer = it.acquireNextImage().planes[0].buffer
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)

                val myBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
                val jpegImage= bitmapToJPEG(myBitmap)


                val intent = Intent(android.content.Intent.ACTION_SEND)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(jpegImage))
                intent.type = "image/jpeg"

                startActivity(Intent.createChooser(intent, "Menuzinho"))

            }, null)
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
