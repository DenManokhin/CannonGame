package ua.edu.lnu.cannongame

import android.graphics.Bitmap

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
}