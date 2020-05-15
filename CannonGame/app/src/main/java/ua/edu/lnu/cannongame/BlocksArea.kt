package ua.edu.lnu.cannongame

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.Log

class BlocksArea(
    private val gameSurface: GameSurface,
    private var x: Int,
    private var y: Int,
    private var width: Int,
    private var height: Int,
    private var rows: Int,
    private var cols: Int
) {
    var blocks: MutableList<Block> = mutableListOf()
        private set

    private val paddingX: Int = (0.05 * width / cols).toInt()
    private val paddingY: Int = (0.05 * height / rows).toInt()

    private val blockWidth: Int = width / cols - 2 * paddingX
    private val blockHeight: Int = height / rows - 2 * paddingY

    init {
        Log.i("BlockArea created", "x=${x}, y=${y}, width=${width}, height=${height}, blockWidth=${blockWidth}, blockHeight=${blockHeight}")

        val blockBitmapOrigin =
            BitmapFactory.decodeResource(gameSurface.resources, R.drawable.target)

        val blockBitmap = if (gameSurface.orientation == GameSurface.Orientation.LANDSCAPE) {
            Bitmap.createScaledBitmap(blockBitmapOrigin, blockHeight, blockWidth, false).rotate(90f)
        } else {
            Bitmap.createScaledBitmap(blockBitmapOrigin, blockWidth, blockHeight, false)
        }

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val block = if (gameSurface.orientation == GameSurface.Orientation.LANDSCAPE) {
                    Block(gameSurface,
                        blockBitmap,
                        x + width - (blockWidth + paddingX + j * (paddingX + blockWidth)),
                        y + paddingY + i * (paddingY + blockHeight),
                        blockWidth,
                        blockHeight)
                } else {
                    Block(gameSurface,
                        blockBitmap,
                        x + paddingX + j * (paddingX + blockWidth),
                        y + paddingY + i * (paddingY + blockHeight),
                        blockWidth,
                        blockHeight)
                }
                blocks.add(block)
            }
        }
    }

    fun changeOrientation(from: GameSurface.Orientation, to: GameSurface.Orientation) {
        if (from == GameSurface.Orientation.PORTRAIT && to == GameSurface.Orientation.LANDSCAPE) {
            width = height.also { height = width }
            val oldY = y
            y = x
            x = gameSurface.width - width - oldY
            rows = cols.also { cols = rows }

            for (block in blocks) {
                block.changeOrientation(from, to)
            }
        } else if (from == GameSurface.Orientation.LANDSCAPE && to == GameSurface.Orientation.PORTRAIT) {
            width = height.also { height = width }
            val oldX = x
            x = y
            y = gameSurface.height - oldX - height
            rows = cols.also { cols = rows }

            for (block in blocks) {
                block.changeOrientation(from, to)
            }
        }

        Log.i("BlockArea updated", "x=${x}, y=${y}, width=${width}, height=${height}, blockWidth=${blockWidth}, blockHeight=${blockHeight}")
    }

    fun update() {
        for (block in blocks) {
            block.update()
        }
    }

    fun draw(canvas: Canvas) {
        for (block in blocks) {
            block.draw(canvas)
        }
    }
}