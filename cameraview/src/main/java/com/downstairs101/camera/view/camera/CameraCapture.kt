package com.downstairs101.camera.view.camera

import android.graphics.ImageFormat
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CaptureRequest
import android.media.ImageReader
import android.util.Log
import android.view.Surface

class CameraCapture {

    private var captureListener: (ByteArray) -> Unit = {}

    fun setOnCaptureListener(captureListener: (ByteArray) -> Unit) {
        this.captureListener = captureListener
    }

    fun createStillCaptureSession(cameraDevice: CameraDevice, width: Int, height: Int) {
        val imageReaderSurface = imageReader(width, height).surface
        val stillCaptureSession = createStillCaptureRequest(cameraDevice, imageReaderSurface)

        cameraDevice.createCaptureSession(listOf(imageReaderSurface), object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                session.capture(stillCaptureSession, null, null)
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                Log.d("CONFIGURED", "falhou aqui em")
            }

        }, null)

    }

    private fun imageReader(width: Int, height: Int) = ImageReader
        .newInstance(height, width, ImageFormat.JPEG, 2)
        .apply {
            setOnImageAvailableListener({
                val buffer = it.acquireNextImage().planes[0].buffer
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)

                captureListener(bytes)
            }, null)
        }

    private fun createStillCaptureRequest(cameraDevice: CameraDevice, surface: Surface): CaptureRequest {
        return cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            .apply {
                set(CaptureRequest.JPEG_ORIENTATION, 90)
                set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                addTarget(surface)
            }.build()
    }
}


//    val builder = StrictMode.VmPolicy.Builder()
//    StrictMode.setVmPolicy(builder.build())
//
//    private fun bitmapToJPEG(bitmap: Bitmap): File {
//        val file = File(externalCacheStoragePath(), "traffic-light.jpeg")
//
//        return compressToPng(bitmap, file)
//    }
//
//    private fun externalCacheStoragePath(): String {
//        val builder = StrictMode.VmPolicy.Builder()
//        StrictMode.setVmPolicy(builder.build())
//        return context?.externalCacheDir.toString()
//    }
//
//    private fun compressToPng(bitmap: Bitmap, file: File): File {
//        val fileOutput = FileOutputStream(file)
//        if (file.setReadable(true, true)) {
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutput)
//        }
//        return file
//    }

//                  val myBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
//                val jpegImage = bitmapToJPEG(myBitmap)
//
//
//                val intent = Intent(android.content.Intent.ACTION_SEND)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(jpegImage))
//                intent.type = "image/jpeg"
//