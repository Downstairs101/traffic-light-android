package sauber.com.trafficlight

import android.Manifest.permission.CAMERA
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.camerakit.CameraKitView
import kotlinx.android.synthetic.main.activity_main.*
import sauber.com.trafficlight.extensions.hasPermissionGranted
import sauber.com.trafficlight.extensions.requestUserPermission
import android.view.Gravity
import android.R.attr.gravity
import android.animation.Animator
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat


class MainActivity : AppCompatActivity(), CameraCallbacks {
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

    private fun openCameraFragment(arguments: Bundle = Bundle()) {
        findNavController(fragmentHost).navigate(R.id.cameraFragment)
    }

    override fun opened() {
        bottomBar.animate().translationY(bottomBar.height.toFloat())
        addButton.animate().scaleX(1.3f).scaleY(1.3f).translationY(
                -(bottomBar.height + (bottomBar.height * 0.4)).toFloat()).setStartDelay(200).start()
    }

    override fun closed() {
        bottomBar.animate().translationY(0f)
        addButton.animate().scaleX(1f).scaleY(1f).translationY(0f).setStartDelay(200).start()
    }

    override fun error(exception: CameraKitView.CameraException) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}