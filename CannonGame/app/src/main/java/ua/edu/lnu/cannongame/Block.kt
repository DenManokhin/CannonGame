package ua.edu.lnu.cannongame

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log

class Block (
    private val gameSurface: GameSurface,
    image: Bitmap?,
    x: Int,
    y: Int,
    width: Int,
    height: Int
) : GameObject(image!!, x, y, width, height)
{
    override fun update() {

    }

    override fun draw(canvas: Canvas) {
        Log.i("Block data", "x=${x}, y=${y}")
        canvas.drawBitmap(image!!, x.toFloat(), y.toFloat(), null)
    }

    fun changeOrientation(from: GameSurface.Orientation, to: GameSurface.Orientation) {
        if (from == GameSurface.Orientation.PORTRAIT && to == GameSurface.Orientation.LANDSCAPE) {
            image = image!!.rotate(90f)
            width = height.also { height = width }
            val oldY = y
            y = x
            x = gameSurface.width - width - oldY
        } else if (from == GameSurface.Orientation.LANDSCAPE && to == GameSurface.Orientation.PORTRAIT) {
            image = image!!.rotate(-90f)
            width = height.also { height = width }
            val oldX = x
            x = y
            y = gameSurface.height - oldX - height
        }

        Log.i("Block updated", "x=${x}, y=${y}, width=${width}, height=${height}")
    }
}