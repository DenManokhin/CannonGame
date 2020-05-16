package ua.edu.lnu.cannongame

import android.graphics.Bitmap
import android.graphics.Canvas

// Abstract class for all game objects
// if width or height == -1 they are inferred from image if it is not null, otherwise NPE is thrown
abstract class GameObject(
    protected var image: Bitmap?,
    x: Int,
    y: Int,
    width: Int,
    height: Int
) {
    var x: Int = x
        protected set

    var y: Int = y
        protected set

    var width: Int = width
        protected set

    var height: Int = height
        protected set

    init {
        if (width == -1 && height == -1) {
            this.width = image!!.width
            this.height = image!!.height
        } else if (width == -1) {
            image = image!!.resizeByHeight(height)
            this.width = image!!.width
        } else if (height == -1) {
            image = image!!.resizeByWidth(width)
            this.height = image!!.height
        } else {
            image = if (image != null) {
                Bitmap.createScaledBitmap(image!!, width, height, false)
            } else null
        }
    }

    abstract fun update()

    abstract fun draw(canvas: Canvas)

    fun changePosition(x: Int, y: Int){
        this.x = x
        this.y = y
    }
}