package ua.edu.lnu.cannongame

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import kotlin.math.PI

class CannonBall(private val gameSurface: GameSurface, image: Bitmap?, x: Int, y: Int) :
        GameObject(image!!, 4, 3, x, y) {
    private val VELOCITY = 1.5f
    private var rotateDeg = 0f
    private var sightLine: Cannon.SightLine? = null
    private var lastDrawNanoTime: Long = -1

    fun update(){
        if(!gameSurface.isCannonActive()) {
            // Current time in nanoseconds
            val now = System.nanoTime()

            // Never once did draw.
            if (lastDrawNanoTime == -1L) {
                lastDrawNanoTime = now;
            }

            val deltaTime = ((now - lastDrawNanoTime) / 1000000).toInt()
            val distance = (VELOCITY * deltaTime);
            val movingVectorX = sightLine!!.stopX - sightLine!!.startX
            val movingVectorY = sightLine!!.stopY - sightLine!!.startY
            val movingVectorLength = kotlin.math.sqrt(movingVectorX * movingVectorX +
                    movingVectorY * movingVectorY)

            // Calculate the new position of the ball.
            this.x = x + (distance * movingVectorX / movingVectorLength).toInt()
            this.y = y + (distance * movingVectorY / movingVectorLength).toInt()

            Log.i("Cannonball data", "x: ${x}, y: ${y}, distance: ${distance}, movingVectorLength: ${movingVectorLength}")

            if (gameSurface.orientation == GameSurface.Orientation.LANDSCAPE) {
                if (this.y <= 0) {
                    this.y = 0
                    this.sightLine!!.startX = this.x.toFloat()
                    this.sightLine!!.startY = 0f

                    this.sightLine!!.stopY = (gameSurface.height).toFloat()
                    val h = (gameSurface.height).toFloat() * kotlin.math.tan((90-rotateDeg) * PI / 180)
                    rotateDeg *=- 1

                    Log.i("Wall hit up", "h: $h, stopX: ${sightLine!!.stopX}")

                    sightLine!!.stopX = this.x.toFloat() - h.toFloat()
                } else if (this.y >= gameSurface.height) {
                    this.y = gameSurface.height
                    this.sightLine!!.startX = this.x.toFloat()
                    this.sightLine!!.startY = (gameSurface.height).toFloat()

                    this.sightLine!!.stopY = 0f
                    val h = (gameSurface.height).toFloat() * kotlin.math.tan((90-rotateDeg) * PI / 180)
                    rotateDeg *=- 1

                    Log.i("Wall hit down", "h: $h, stopX: ${sightLine!!.stopX}")

                    sightLine!!.stopX = this.x.toFloat() + h.toFloat()
                }
            }
            else {
                if (this.x <= 0) {
                    this.x = 0
                    this.sightLine!!.startX = 0f
                    this.sightLine!!.startY = this.y.toFloat()
                    this.sightLine!!.stopX = (gameSurface.width).toFloat()
                    val h = (gameSurface.width).toFloat() * kotlin.math.tan((90-rotateDeg) * PI / 180)
                    rotateDeg *=- 1

                    Log.i("Wall hit left", "h: $h, stopY: ${sightLine!!.stopY}")

                    sightLine!!.stopY = this.y.toFloat() + h.toFloat()
                } else if (this.x >= gameSurface.width) {
                    this.x = gameSurface.width
                    this.sightLine!!.startX = gameSurface.width.toFloat()
                    this.sightLine!!.startY = this.y.toFloat()
                    this.sightLine!!.stopX = 0f
                    val h = (gameSurface.width).toFloat() * kotlin.math.tan((90-rotateDeg) * PI / 180)
                    rotateDeg *=- 1

                    Log.i("Wall hit right", "h: $h, stopY: ${sightLine!!.stopY}")

                    sightLine!!.stopY = this.y.toFloat() - h.toFloat()
                }
            }
        }
    }

    fun draw(canvas: Canvas) {
        if(!gameSurface.isCannonActive()) {
            Log.i("Cannon ball data", "x=${x}, y=${y}")

            canvas.drawBitmap(image, x.toFloat(), y.toFloat(), null)

            // Last draw time.
            lastDrawNanoTime = System.nanoTime()
        }
    }


    fun updateMovingVector(newLine: Cannon.SightLine, newDeg: Float) {
        this.sightLine = newLine
        this.rotateDeg = newDeg

        x = newLine.startX.toInt()
        y = newLine.startY.toInt()

        // Last draw time.
        lastDrawNanoTime = -1L

        Log.i("Cannonball data update", "x: $x, y: $y")
    }
}