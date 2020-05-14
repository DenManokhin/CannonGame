package ua.edu.lnu.cannongame

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import kotlin.math.PI

class CannonBall(private val gameSurface: GameSurface, image: Bitmap?, x: Int, y: Int) :
        GameObject(image!!, 4, 3, x, y) {
    private val VELOCITY = 0.1f
    private var lastDrawNanoTime: Long = -1

    fun update(){
        // Current time in nanoseconds
        val now = System.nanoTime()

        // Never once did draw.
        if(lastDrawNanoTime == -1L) {
            lastDrawNanoTime = now;
        }

        val deltaTime = ((now - lastDrawNanoTime) / 100000).toInt()
    }

    fun draw(canvas: Canvas) {}

    fun changePosition(x: Int, y: Int){
        this.x = x
        this.y = y
    }
}