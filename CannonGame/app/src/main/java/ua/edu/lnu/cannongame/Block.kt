package ua.edu.lnu.cannongame

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log

class Block (private val gameSurface: GameSurface, image: Bitmap?, x: Int, y: Int,
    private var w: Int,
    private var h: Int
) : GameObject(image!!, 20, 10, x, y) {

    fun update() {

    }

    fun draw(canvas: Canvas) {
        Log.i("Block data", "x=${x}, y=${y}")
        canvas.drawBitmap(image, x.toFloat(), y.toFloat(), null)
    }

    fun changeOrientation(from: GameSurface.Orientation, to: GameSurface.Orientation) {
        if (from == GameSurface.Orientation.PORTRAIT && to == GameSurface.Orientation.LANDSCAPE) {
            image = image.rotate(90f)
            w = h.also { h = w }
            val oldY = y
            y = x
            x = gameSurface.width - w - oldY
        } else if (from == GameSurface.Orientation.LANDSCAPE && to == GameSurface.Orientation.PORTRAIT) {
            image = image.rotate(-90f)
            w = h.also { h = w }
            val oldX = x
            x = y
            y = gameSurface.height - oldX - h
        }

        Log.e("Block updated", "x=${x}, y=${y}, width=${w}, height=${h}")
    }
}