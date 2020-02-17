package org.br.storage

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class PhotoStorageDao(private val ctx: Context) {
    fun saveThumbBitmap(photoId: String, photoBitmap: Bitmap): String =
            saveBitmap(photoBitmap, "${photoId}-thumb.png")

    fun saveOriginalBitmap(photoId: String, photoBitmap: Bitmap): String =
            saveBitmap(photoBitmap, "${photoId}-original.png")

    private fun saveBitmap(photoBitmap: Bitmap, name: String): String {
        val ctxWrapper = ContextWrapper(ctx)
        val directory = ctxWrapper.getDir("imagesDir", Context.MODE_PRIVATE)

        val file = File(directory, name)

        if (file.exists()) {
            file.delete()
        }

        val fileOutputStream = FileOutputStream(file)

        photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)

        fileOutputStream.close()

        return file.path
    }

    fun readPhotoBitmap(path: String): Bitmap? {
        val file = File(path)

        if (!file.exists()) {
            return null
        }

        return BitmapFactory.decodeStream(FileInputStream(file))
    }
}