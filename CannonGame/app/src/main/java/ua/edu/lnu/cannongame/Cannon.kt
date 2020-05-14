package ua.edu.lnu.cannongame

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import kotlin.math.PI


class Cannon (private val gameSurface: GameSurface, image: Bitmap?, x: Int, y: Int) :
    GameObject(image!!, 4, 3, x, y) {

    class SightLine(var startX: Float, var startY: Float, var stopX: Float, var stopY: Float){
        override fun toString(): String {
            return "SightLine {startX=${startX}, startY=${startY}, stopX=${stopX}, stopY=${stopY}}"
        }
    }

    private val sightLine: SightLine = SightLine(0f, 0f, 0f,0f)
        get() = field

    private val paint: Paint = Paint()
    private var rotateDeg: Float = -60F
        get() = field
    var isRotating: Boolean = true
        get() = field
        private set

    // Rotate velocity (deg/millisecond)
    private val VELOCITY = 0.01f
    private var ROTATE_COEF = 1

    private var lastDrawNanoTime: Long = -1

    init {
        paint.color = Color.RED
        paint.strokeWidth = 10F
    }

    fun update(){
        // Current time in nanoseconds
        val now = System.nanoTime()

        // Never once did draw.
        if(lastDrawNanoTime == -1L) {
            lastDrawNanoTime = now;
        }

        val deltaTime = ((now - lastDrawNanoTime) / 1000000).toInt()
        Log.i("Cannon update", "delta=${deltaTime}")

        if (isRotating){
            rotateDeg += ROTATE_COEF* (VELOCITY * deltaTime) * 1.5f
            if (rotateDeg > 60) {
                rotateDeg = 60f
                ROTATE_COEF *= -1
            }else if(rotateDeg < -60){
                rotateDeg = -60f
                ROTATE_COEF *= -1
            }
        }
    }

    fun draw(canvas: Canvas) {
        Log.i("Cannon data", "x=${x}, y=${y}, deg=${rotateDeg}")

        if (gameSurface.orientation == GameSurface.Orientation.LANDSCAPE){
            canvas.drawBitmap(image.rotate(rotateDeg), x.toFloat(), y.toFloat(), null)

            if (isRotating){
                sightLine.startX = 200f
                sightLine.startY = (gameSurface.height/2).toFloat()
                sightLine.stopX = (gameSurface.width).toFloat()
                val h = (sightLine.stopX - sightLine.startX) * kotlin.math.sin(-rotateDeg*PI/180)
                sightLine.stopY = (gameSurface.height/2 - h).toFloat()

                Log.i("Line data (LANDSCAPE)", "${sightLine}, h=${h}")

                canvas.drawLine(sightLine.startX, sightLine.startY, sightLine.stopX, sightLine.stopY, paint)
            }
        }else{
            canvas.drawBitmap(image.rotate(rotateDeg-90), x.toFloat(), y.toFloat(), null)

            if (isRotating){
                sightLine.startX = (gameSurface.width/2).toFloat()
                sightLine.startY = (gameSurface.height - 200).toFloat()
                sightLine.stopY = 0f
                val h = sightLine.startY * kotlin.math.sin(-rotateDeg*PI/180)
                sightLine.stopX = (gameSurface.width/2 - h).toFloat()

                Log.i("Line data (PORTRAIT)", "${sightLine}, h=${h}")

                canvas.drawLine(sightLine.startX, sightLine.startY, sightLine.stopX, sightLine.stopY, paint)
            }
        }

        // Last draw time.
        lastDrawNanoTime = System.nanoTime()
    }

    fun changePosition(x: Int, y: Int){
        this.x = x
        this.y = y
    }

    fun startRotate() {
        isRotating = true
    }

    fun stopRotate() {
        isRotating = false
    }
}