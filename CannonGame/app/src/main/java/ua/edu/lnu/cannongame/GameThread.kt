package ua.edu.lnu.cannongame

import android.graphics.Canvas
import android.view.SurfaceHolder

class GameThread (surfaceHolder: SurfaceHolder, gameSurface: GameSurface) : Thread() {
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
        var startTime = System.nanoTime()

        var canvas: Canvas?
        while (running) {
            canvas = null
            try {
                canvas = surfaceHolder!!.lockCanvas(null)
                synchronized(canvas!!) {
                    gameSurface!!.draw(canvas)
                }
            }finally {
                canvas?.let { surfaceHolder!!.unlockCanvasAndPost(it) }
            }
            val now = System.nanoTime()

            var waitTime = (now - startTime) / 1000000
            if (waitTime < 10) {
                waitTime = 10 // Millisecond.
            }

            try {
                sleep(waitTime)
            } catch (e: InterruptedException) {
            }
            startTime = System.nanoTime()
        }
    }
}