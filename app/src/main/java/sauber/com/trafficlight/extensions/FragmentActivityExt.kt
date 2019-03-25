package sauber.com.trafficlight.extensions

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.hasPermissionGranted(permission: String) =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun FragmentActivity.shouldShowRequestPermission(permission: String) =
    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

fun FragmentActivity.requestUserPermission(requestCode: Int, vararg permissions: String) =
    ActivityCompat.requestPermissions(this, permissions, requestCode)
