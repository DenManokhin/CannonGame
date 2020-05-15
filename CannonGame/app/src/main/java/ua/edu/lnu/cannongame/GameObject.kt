package ua.edu.lnu.cannongame

import android.graphics.Bitmap
import android.graphics.Matrix

abstract class GameObject(
    protected var image: Bitmap,
    protected val rowCount: Int,
    protected val colCount: Int,
    var x: Int,
    var y: Int
) {
    protected val WIDTH: Int
    protected val HEIGHT: Int
    val width: Int
    val height: Int

    init {
        WIDTH = image.width
        HEIGHT = image.height
        width = WIDTH / colCount
        height = HEIGHT / rowCount
    }

    fun changePosition(x: Int, y: Int){
        this.x = x
        this.y = y
    }
}

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}