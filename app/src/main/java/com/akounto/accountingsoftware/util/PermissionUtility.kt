package com.akounto.accountingsoftware.util

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import java.util.*

class PermissionUtility {

    companion object {
        private val REQUEST_CODE_MULTIPLE_PERMISSION = 100;

        private val PERMISSIONS = arrayOf(
            //Manifest.permission.ACCESS_FINE_LOCATION,
            //Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )

        fun checkPermissions(context: Context): Boolean {
            for (permission in PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(context, permission) !=
                    PackageManager.PERMISSION_GRANTED
                ) return false
            }
            return true
        }

        fun requestMultiplePermissions(context: Context) {
            val remainingPermissions: MutableList<String> = ArrayList()
            for (permission in PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(context, permission) !=
                    PackageManager.PERMISSION_GRANTED
                ) {
                    remainingPermissions.add(permission)
                }
            }

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Need Permissions")
            builder.setMessage("This app needs these permissions.")

            builder.setPositiveButton("Grant") { dialog, _ ->
                dialog.cancel()
                ActivityCompat.requestPermissions(
                    context as Activity,
                    remainingPermissions.toTypedArray(),
                    REQUEST_CODE_MULTIPLE_PERMISSION
                )
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            builder.show()
        }

        fun onRequestPermissionsResult(
            context: Context, requestCode: Int, permissions: Array<String>, grantResults: IntArray
        ): Boolean {

            if (requestCode == REQUEST_CODE_MULTIPLE_PERMISSION) {

                for (i in grantResults.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                (context as Activity), permissions[i]
                            )
                        ) {
                            requestMultiplePermissions(context)
                        } else {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri: Uri = Uri.fromParts("package", context.packageName, null)
                            intent.data = uri
                            context.startActivity(intent)
                        }
                        return false
                    }
                }
            }
            return true
        }
    }
}