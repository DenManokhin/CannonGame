package ua.edu.lnu.cannongame

import android.graphics.Canvas
import android.util.Log
import android.view.SurfaceHolder

class GameThread (gameSurface: GameSurface, surfaceHolder: SurfaceHolder?) : Thread() {
    private var surfaceHolder: SurfaceHolder? = null
    private var gameSurface: GameSurface? = null
    private var running = false

    init {
        this.surfaceHolder = surfaceHolder
        this.gameSurface = gameSurface
    }

    fun setRunning(running: Boolean){
        this.running = running
    }

    override fun run() {
        Log.i("GameThread", "Thread started")

        var startTime = System.nanoTime()

        var canvas: Canvas?
        while (running) {
            canvas = null
            try {
                canvas = surfaceHolder!!.lockCanvas(null)
                synchronized(canvas!!) {
                    gameSurface!!.update()
                    gameSurface!!.postInvalidate()
                }
            }finally {
                canvas?.let { surfaceHolder!!.unlockCanvasAndPost(it) }
            }
            val now = System.nanoTime()

            var waitTime = (now - startTime) / 1000000
            if (waitTime < 100) {
                waitTime = 100 // Millisecond.
            }

            try {
                sleep(waitTime)
                gameSurface!!.gameData!!.updateTime(waitTime)
            } catch (e: InterruptedException) {
            }
            startTime = System.nanoTime()
        }

        Log.i("GameThread", "Thread ended")
    }
}