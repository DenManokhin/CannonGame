package ua.edu.lnu.cannongame

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView


class GameSurface: SurfaceView, SurfaceHolder.Callback {

    enum class Orientation {
        PORTRAIT, LANDSCAPE
    }

    private var gameThread: GameThread? = null
    private var cannon: Cannon? = null
    private var cannonBall: CannonBall? = null
    var orientation: Orientation

    init {
        holder.addCallback(this)
        orientation = if (width > height){
            Orientation.LANDSCAPE
        }else{
            Orientation.PORTRAIT
        }
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        Log.i("GameSurface", "SurfaceChanged width=${width}, height=${height}")

        if (width > height){
            orientation = Orientation.LANDSCAPE
            synchronized(cannon!!){
                cannon!!.changePosition(25, height/2-200)
            }
        }else{
            orientation = Orientation.PORTRAIT
            synchronized(cannon!!){
                cannon!!.changePosition(width/2-200, height - 275)
            }
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        Log.i("GameSurface", "SurfaceDestroyed width=${width}, height=${height}")

        var retry = true
        while (retry) {
            try {
                gameThread!!.setRunning(false)

                // Parent thread must wait until the end of GameThread.
                gameThread!!.join()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            retry = true
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        Log.i("GameSurface", "SurfaceCreated width=${width}, height=${height}")

        val cannonBitmap =
            BitmapFactory.decodeResource(this.resources, R.drawable.cannon)
        val cannonBallBitmap =
            BitmapFactory.decodeResource(this.resources, R.drawable.cannon_ball)

        cannon = if (orientation == Orientation.LANDSCAPE){
            Cannon(this, cannonBitmap, 25, height/2-200)
        }else{
            Cannon(this, cannonBitmap, width/2-200, height - 225)
        }
        cannonBall = CannonBall(this, cannonBallBitmap, 0, 0)

        gameThread = GameThread(this, holder)
        gameThread!!.setRunning(true)
        gameThread!!.start()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        cannon!!.draw(canvas!!)
    }

    fun update()  {
        cannon!!.update()
    }
}