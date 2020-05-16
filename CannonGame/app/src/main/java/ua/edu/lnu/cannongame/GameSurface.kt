package ua.edu.lnu.cannongame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView


class GameSurface: SurfaceView, SurfaceHolder.Callback {

    enum class Orientation {
        PORTRAIT, LANDSCAPE
    }

    private var gameThread: GameThread? = null
    private var cannon: Cannon? = null
    private var blocksArea: BlocksArea? = null
    var orientation: Orientation = Orientation.PORTRAIT

    private var cannonBall: CannonBall? = null

    var gameData: GameData? = null
        get() = field
        private set

    init {
        holder.addCallback(this)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        Log.i("GameSurface", "SurfaceChanged width=${width}, height=${height}")

        if (width > height){
            if (orientation == Orientation.PORTRAIT) {
                synchronized(blocksArea!!) {
                    blocksArea!!.changeOrientation(Orientation.PORTRAIT, Orientation.LANDSCAPE)
                }
            }
            orientation = Orientation.LANDSCAPE
            synchronized(cannon!!){
                cannon!!.changePosition(25, height/2-200)
            }
        }else{
            if (orientation == Orientation.LANDSCAPE) {
                synchronized(blocksArea!!) {
                    blocksArea!!.changeOrientation(Orientation.LANDSCAPE, Orientation.PORTRAIT)
                }
            }
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
        orientation = if (width > height){
            Orientation.LANDSCAPE
        }else{
            Orientation.PORTRAIT
        }
        Log.i("GameSurface", "SurfaceCreated width=${width}, height=${height}, orientation=${orientation}")

        val cannonBitmap =
            BitmapFactory.decodeResource(this.resources, R.drawable.cannon)
        val cannonBallBitmap =
            BitmapFactory.decodeResource(this.resources, R.drawable.cannon_ball)

        cannon = if (orientation == Orientation.LANDSCAPE){
            Cannon(this, cannonBitmap, 25, height/2-200, -1, -1)
        }else{
            Cannon(this, cannonBitmap, width/2-200, height - 225, -1, -1)
        }
        cannonBall = CannonBall(this, cannonBallBitmap, 0, 0, 30, 30)

        gameData = GameData()

        blocksArea = if (orientation == Orientation.LANDSCAPE) {
            BlocksArea(this, width / 2, 0, width / 2, height, 4, 3)
        } else {
            BlocksArea(this, 0, 0, width, height / 2, 3, 4)
        }

        gameThread = GameThread(this, holder)
        gameThread!!.setRunning(true)
        gameThread!!.start()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        cannon!!.draw(canvas!!)
        cannonBall!!.draw(canvas)
        gameData!!.draw(canvas)
        blocksArea!!.draw(canvas)
    }

    fun update()  {
        if (gameData!!.isGameRunning()){
            if (gameData!!.canMakeNewShot()){
                cannon!!.startRotate()
            }
            cannon!!.update()
            cannonBall!!.update()
            blocksArea!!.update()
            
        }else{
            pause()
        }
    }

    fun pause(){
        try {
            gameThread!!.setRunning(false)
            gameThread!!.join()
        } catch (e: InterruptedException) {
        }
    }

    fun resume(){
        gameThread = GameThread(this, holder)
        gameThread!!.setRunning(true)
        gameThread!!.start()
    }

    fun isCannonActive(): Boolean {
        return cannon!!.isRotating
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.i("GameSurface", "Touch ${event.toString()}")

        if (event!!.action == MotionEvent.ACTION_DOWN){
            if (isCannonActive()){
                cannon!!.stopRotate()
                gameData!!.trackNewShot()
                cannonBall!!.updateMovingVector(cannon!!.sightLine, cannon!!.rotateDeg)
            }else{
                if (gameData!!.canMakeNewShot()){
                    cannon!!.startRotate()
                }
            }
        }

        return super.onTouchEvent(event)
    }
}