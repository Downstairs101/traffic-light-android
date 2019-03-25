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
            requestUserPermission(1, CAMERA)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    openCameraFragment()
                } else {
                }
                return
            }
            else -> {
            }
        }
    }

    private fun openCameraFragment() {
        findNavController(fragmentHost).navigate(R.id.cameraFragment)
    }


}