package org.br.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.fragment.app.Fragment

fun hasWritePermissions(context: Context?): Boolean {
    return hasPermissions(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
}

fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        return true
    }
    var hasPermission = true

    context?.let {
        for (permission in permissions) {
            if (it.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                hasPermission = false
                break
            }
        }
    }

    return hasPermission
}

fun requestWritePermissions(fragment: Fragment, code: Int) {
    fragment.requestPermissions(
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            code
    )
}