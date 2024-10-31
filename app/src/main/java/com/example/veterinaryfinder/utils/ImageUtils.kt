package com.example.veterinaryfinder.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

object ImageUtils {
    // Guarda un Bitmap orientado correctamente en el almacenamiento interno y devuelve la URI
    fun saveCorrectlyOrientedImageToInternalStorage(context: Context, uri: Uri): Uri? {
        return try {
            // Obtener el Bitmap con la orientación corregida
            val rotatedBitmap = getCorrectlyOrientedImage(context, uri) ?: return null

            // Definir la ruta de almacenamiento interno
            val file = File(context.filesDir, "myImage_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)

            // Comprimir y guardar el bitmap en el archivo
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()

            // Devolver la URI del archivo guardado
            Uri.fromFile(file)
        } catch (e: Exception) {
            Timber.tag("ImageUtils").e(e, "Error saving oriented image to internal storage")
            null
        }
    }

    // Corrige la orientación de la imagen usando los metadatos EXIF
    private fun getCorrectlyOrientedImage(context: Context, uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            // Obtener la orientación EXIF de la imagen
            val exifInputStream = context.contentResolver.openInputStream(uri)
            val exif = ExifInterface(exifInputStream!!)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )

            val rotatedBitmap = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
                else -> bitmap
            }

            inputStream?.close()
            exifInputStream.close()

            rotatedBitmap
        } catch (e: Exception) {
            Timber.tag("ImageUtils").e(e, "Error correcting image orientation")
            null
        }
    }

    // Rota un Bitmap según el ángulo especificado
    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }
}