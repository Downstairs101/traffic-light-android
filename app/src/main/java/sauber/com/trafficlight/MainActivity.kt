package sauber.com.trafficlight

import android.Manifest.permission.CAMERA
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import sauber.com.trafficlight.extensions.hasPermissionGranted
import sauber.com.trafficlight.extensions.requestUserPermission


class MainActivity : AppCompatActivity() {
    companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addButton.setOnClickListener {
            requestCameraPermissions()
        }
    }


    private fun requestCameraPermissions() {
        if (hasPermissionGranted(CAMERA)) {
            openCameraFragment()
        } else {
            requestUserPermission(CAMERA_PERMISSION_REQUEST_CODE, CAMERA)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED)) {
                    openCameraFragment()
                } else {
                    //todo: show the user reasons that tells he why we need to get this permission
                }
                return
            }
        }
    }

    private fun openCameraFragment() {
        findNavController(fragmentHost).navigate(R.id.cameraFragment)
    }


}