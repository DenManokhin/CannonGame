package ua.edu.lnu.cannongame

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Html
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import androidx.appcompat.app.AlertDialog
import java.util.*


class GameSurface: SurfaceView, SurfaceHolder.Callback {

    enum class Orientation {
        PORTRAIT, LANDSCAPE
    }

    enum class Difficulty constructor(val value: Int){
        EASY(1),
        MEDIUM (2),
        HARD (3);

        companion object {
            operator fun invoke(rawValue: Int) = values().find { it.value == rawValue }
        }
    }

    private var gameThread: GameThread? = null
    private var cannon: Cannon? = null
    private var blocksArea: BlocksArea? = null
    var orientation: Orientation = Orientation.PORTRAIT
    var difficulty: Difficulty?

    private var cannonBall: CannonBall? = null

    var gameData: GameData? = null
        get() = field
        private set

    init {
        holder.addCallback(this)
        difficulty = Difficulty((context as GameActivity).getDifficulty())
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    private fun dot(U:List<Int>, V:List<Int>): Int {
        return U[0] * V[0] + U[1] * V[1]
    }

    private fun validateCoords() {
        val m: List<Int> = listOf(cannonBall!!.x, cannonBall!!.y)

        for(block in blocksArea!!.blocks) {
            val a:List<Int> = listOf(block.x + block.height, block.y)
            val b:List<Int> = listOf(block.x, block.y)
            val c:List<Int> = listOf(block.x, block.y + block.width)

            val ab:List<Int> = listOf(b[0]-a[0], b[1]-a[1])
            val bc:List<Int> = listOf(c[0]-b[0], c[1]-b[1])

            val am:List<Int> = listOf(m[0]-a[0], m[1]-a[1])
            val bm:List<Int> = listOf(m[0]-b[0], m[1]-b[1])

            if ((0 <= dot(ab,am) && dot(ab,am) <= dot(ab,ab)) &&
                    (0 <= dot(bc,bm) && dot(bc,bm) <= dot(bc,bc))) {
                blocksArea!!.removeBlock(block)
                gameData!!.hitShot()
                break
            }
        }

        if(blocksArea?.blocks.isNullOrEmpty()) {
            gameData!!.resultMessage = "win"
            gameThread!!.setRunning(false)
            val gameActivity = context as GameActivity
            gameActivity.showDialog()
        }
    }
    
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
        Log.i("GameSurface", "Difficulty: $difficulty")
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

        gameData = GameData(this)

        var num = 5

        if (difficulty == Difficulty.MEDIUM) {
            num = 7
        }
        else if (difficulty == Difficulty.HARD) {
            num = 9
        }

        blocksArea = if (orientation == Orientation.LANDSCAPE) {
            BlocksArea(this, width / 2, 0, width / 2, height, num, 4, 3)
        } else {
            BlocksArea(this, 0, 70, width, height / 2, num,3, 4)
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
            else {
                validateCoords()
            }
            cannon!!.update()
            cannonBall!!.update()
            blocksArea!!.update()
            
        }else {
            gameThread!!.setRunning(false)
            val gameActivity = context as GameActivity
            gameActivity.showDialog()
        }
    }

    fun pause(){
        try {
            gameThread!!.setRunning(false)
            gameThread!!.join()
        } catch (e: InterruptedException) {
        }
        Log.i("GameSurface", "paused")
    }

    fun resume(){
        Log.i("GameSurface", "resumed")
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