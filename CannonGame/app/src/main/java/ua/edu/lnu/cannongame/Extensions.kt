package ua.edu.lnu.cannongame

import android.graphics.Bitmap
import android.graphics.Matrix

// Extension function to rotate bitmap by specified angle
fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}


// Extension function to resize bitmap using new width value by keeping aspect ratio
fun Bitmap.resizeByWidth(width:Int): Bitmap {
    val ratio:Float = this.width.toFloat() / this.height.toFloat()
    val height:Int = Math.round(width / ratio)

    return Bitmap.createScaledBitmap(this, width, height, false)
}


// Extension function to resize bitmap using new height value by keeping aspect ratio
fun Bitmap.resizeByHeight(height:Int): Bitmap {
    val ratio:Float = this.height.toFloat() / this.width.toFloat()
    val width:Int = Math.round(height / ratio)

    return Bitmap.createScaledBitmap(this, width, height, false)
}