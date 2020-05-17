package ua.edu.lnu.cannongame

import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.Log
import kotlin.random.Random

class BlocksArea(
    private val gameSurface: GameSurface,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    num: Int,
    unbreakableNum: Int,
    private var rows: Int,
    private var cols: Int
) : GameObject(null, x, y, width, height)
{
    var blocks: MutableList<Block> = mutableListOf()
        private set

    private val paddingX: Int = (0.05 * width / cols).toInt()
    private val paddingY: Int = (0.05 * height / rows).toInt()

    private val blockWidth: Int = width / cols - 2 * paddingX
    private val blockHeight: Int = height / rows - 2 * paddingY

    init {
        Log.i(
            "BlockArea created",
            "x=${x}, y=${y}, width=${width}, height=${height}, blockWidth=${blockWidth}, blockHeight=${blockHeight}"
        )

        val blockBitmap = if (gameSurface.orientation == GameSurface.Orientation.LANDSCAPE) {
            BitmapFactory.decodeResource(gameSurface.resources, R.drawable.target).rotate(90f)
        } else {
            BitmapFactory.decodeResource(gameSurface.resources, R.drawable.target)
        }

        val unbreakableBlockBitmap = if (gameSurface.orientation == GameSurface.Orientation.LANDSCAPE) {
            BitmapFactory.decodeResource(gameSurface.resources, R.drawable.block).rotate(90f)
        } else {
            BitmapFactory.decodeResource(gameSurface.resources, R.drawable.block)
        }

        if (num + unbreakableNum > rows * cols) {
            throw IllegalArgumentException("Can not create ${num+unbreakableNum} blocks on ${rows}x${cols} grid")
        }

        var gridCoordinates = mutableSetOf<Pair<Int, Int>>()
        while (gridCoordinates.size != num) {
            val coordinate = Pair(Random.nextInt(0, rows), Random.nextInt(0, cols))
            gridCoordinates.add(coordinate)
        }

        for ((i, j) in gridCoordinates) {
            val block = if (gameSurface.orientation == GameSurface.Orientation.LANDSCAPE) {
                Block(
                    gameSurface,
                    false,
                    blockBitmap,
                    x + width - (blockWidth + paddingX + j * (paddingX + blockWidth)),
                    y + paddingY + i * (paddingY + blockHeight),
                    blockWidth,
                    blockHeight
                )
            } else {
                Block(
                    gameSurface,
                    false,
                    blockBitmap,
                    x + paddingX + j * (paddingX + blockWidth),
                    y + paddingY + i * (paddingY + blockHeight),
                    blockWidth,
                    blockHeight
                )
            }
            blocks.add(block)
        }

        var unbreakableGridCoordinates = mutableSetOf<Pair<Int, Int>>()
        while (unbreakableGridCoordinates.size != unbreakableNum) {
            val coordinate = Pair(Random.nextInt(0, rows), Random.nextInt(0, cols))
            if(!gridCoordinates.contains(coordinate))
            {
                unbreakableGridCoordinates.add(coordinate)
            }
        }

        for ((i, j) in unbreakableGridCoordinates) {
            val unbreakableBlock = if (gameSurface.orientation == GameSurface.Orientation.LANDSCAPE) {
                Block(
                    gameSurface,
                    true,
                    unbreakableBlockBitmap,
                    x + width - (blockWidth + paddingX + j * (paddingX + blockWidth)),
                    y + paddingY + i * (paddingY + blockHeight),
                    blockWidth,
                    blockHeight
                )
            } else {
                Block(
                    gameSurface,
                    true,
                    unbreakableBlockBitmap,
                    x + paddingX + j * (paddingX + blockWidth),
                    y + paddingY + i * (paddingY + blockHeight),
                    blockWidth,
                    blockHeight
                )
            }
            blocks.add(unbreakableBlock)
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

    override fun update() {
        for (block in blocks) {
            block.update()
        }
    }

    override fun draw(canvas: Canvas) {
        for (block in blocks) {
            block.draw(canvas)
        }
    }

    fun removeBlock(x: Block){
        blocks.remove(x)
    }
}