import korlibs.event.*
import korlibs.image.atlas.*
import korlibs.image.color.*
import korlibs.io.file.std.*
import korlibs.korge.*
import korlibs.korge.animate.*
import korlibs.korge.input.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import kotlin.math.*
import kotlin.random.*

object Config {
    const val title = "Code Every Day - KorGE - Sunny Land"

    const val width = 800
    const val height = 600

    val windowSize = Size(width, height)
    val virtualSize = Size(width, height)

    const val backgroundColorValue = "#2b2b2b"
    val backgroundColors = Colors[backgroundColorValue]

    const val randomMovingMin = 1.5
    const val randomMovingMax = 5.0

    const val isDebug = true

    fun debug(
        block: () -> Unit = {}
    ) {
        if (isDebug) {
            block()
        }
    }
}

suspend fun main() = Korge(
    title = Config.title,
    windowSize = Config.windowSize,
    virtualSize = Config.virtualSize,
    backgroundColor = Config.backgroundColors
) {
    val spriteAtlas = resourcesVfs["Sunny-Land/atlas/atlas.json"].readAtlas()
    /**
     * If you want display with random moving
     *  testDisplayEagles(spriteAtlas)
     */

    testInputMouseClicks(spriteAtlas)
}

/**
 * Control moving [Sprite] to [Point] on [Container]
 * in witch user click.
 * @see <a href="https://docs.korge.org/views/input/">KorGE:Input</a>
 */
private fun Container.testInputMouseClicks(
    spriteAtlas: Atlas,
) {
    val sprite = displaySprite(
        atlas = spriteAtlas,
        name = SpriteName.eagle,
        position = getRandomPosition(),
        useRandomMoving = false
    )

    controlByMouse(sprite)
    controlByDragAndDrop(sprite)
    controlByKeys(sprite)
}

/**
 * Handling click of mouse and moving sprite to [Point] of clicks
 */
private fun Container.controlByMouse(
    sprite: Sprite
) {
    mouse {
        click {
            val point = input.mousePos
            val spriteSizeDiffs = sprite.getSizeDiffs()

            /**
             * Calculate x position of [Sprite]
             * X is top left
             * If user click on x = 0, we must set x + [Sprite.width] / 2 because
             * [Sprite] will be visible full by width
             * And the same for click on end of the window
             */
            var x = point.x - spriteSizeDiffs.x
            x = when {
                x < 0 -> 0f
                x > Config.windowSize.width -> Config.windowSize.width - 2 * spriteSizeDiffs.x
                else -> x
            }

            /**
             * The same logic as for calculating X
             */
            var y = point.y - spriteSizeDiffs.y
            y = when {
                y < 0 -> 0f
                y > Config.windowSize.height -> Config.windowSize.height - 2 * spriteSizeDiffs.y
                else -> y
            }

            val animator = animator(parallel = false)
            animator.moveTo(
                sprite,
                x, y,
                time = TimeSpan(2000.0),
                easing = Easing.EASE_OUT
            )

            debugLog("Mouse click point = $point")
        }
    }
}

/**
 * Using drag-and-drop for [Sprite]
 */
private fun Container.controlByDragAndDrop(
    sprite: Sprite
) {
    sprite.draggableCloseable(
        selector = sprite,
        autoMove = true
    ) { draggableInfo ->
        val x = draggableInfo.viewNextX
        val y = draggableInfo.viewNextY

        debugLog("Drag and drop to point = $x,$y ")

        /**
         * Correcting position [Sprite] if user
         * drag it from window
         * It return [Sprite] on Screen
         */
        val correctionX = when {
            x < 0 -> 0f
            x > Config.windowSize.width -> Config.windowSize.width - 2 * sprite.getSizeDiffs().x
            else -> x
        }
        val correctionY = when {
            y < 0 -> 0f
            y > Config.windowSize.height -> Config.windowSize.height - 2 * sprite.getSizeDiffs().y
            else -> y
        }
        if (correctionX != x || correctionY != y) {
            val animator = animator(parallel = false)
            animator.moveTo(
                view = sprite,
                x = correctionX,
                y = correctionY
            )
        }
    }
}

/**
 * Handling keyboard clicks for control to position of [Sprite]
 * Default value of changing position is 50f
 */
private fun Container.controlByKeys(
    sprite: Sprite
) {
    val diffs = 50f
    val blockOnClick: (x: Float, y: Float, log: String) -> Unit = { x, y, log ->
        val animator = animator(parallel = false)

        val maxX = Config.windowSize.width - 2 * sprite.getSizeDiffs().x
        val maxY = Config.windowSize.height - 2 * sprite.getSizeDiffs().y

        var toX = sprite.x + x
        toX = when {
            toX < 0f -> 0f
            toX > maxX -> maxX
            else -> toX
        }

        var toY = sprite.y + y
        toY = when {
            toY < 0f -> 0f
            toY > maxY -> maxY
            else -> toY
        }

        animator.moveTo(
            view = sprite,
            x = toX,
            y = toY
        )
        debugLog("Click Key = $log, move to point = $toX, $toY")
    }
    keys {
        down(Key.LEFT) {
            blockOnClick(-diffs,  0f, "left")
        }
        down(Key.RIGHT) {
            blockOnClick(diffs,  0f, "right")
        }
        down(Key.UP) {
            blockOnClick(0f,  -diffs, "up")
        }
        down(Key.DOWN) {
            blockOnClick(0f,  diffs, "down")
        }
    }
}

/**
 * Calculate half width and half height of [Sprite] with [Sprite.scaleXY]
 * @return [Position] with half values
 */
private fun Sprite.getSizeDiffs(): Position {
    val x = (width * scaleXY).div(2)
    val y = (height * scaleXY).div(2)
    return Position(x, y)
}

var debugText: Text? = null
private fun Container.debugLog(
    message: String
) {
    Config.debug {
        when (debugText) {
            null -> debugText = text(text = message)
            else -> debugText?.text = message
        }
    }
}

/**
 * Generate [count] Eagle Sprite from [Atlas]
 * @return  list of Sprite with can random moving on the Window
 */
private fun Container.testDisplayEagles(
    spriteAtlas: Atlas,
    count: Int = 3
): List<Sprite> {
    return List(count) {
        displaySprite(
            atlas = spriteAtlas,
            name = SpriteName.eagle,
            position = getRandomPosition()
        )
    }
}

/**
 * Create Sprite from [Atlas] with prefix [name] and
 * display it on Container with [Position].
 * Sprite can scale with [scaleXY] and delay for frame display with [displayTime]
 */
private fun Container.displaySprite(
    atlas: Atlas,
    name: String,
    position: Position = Position(),
    scaleXY: Int? = 5,
    displayTime: Double? = 100.0,
    useRandomMoving: Boolean = true
): Sprite {
    val spriteAnimation = atlas.getSpriteAnimation(name)

    val sprite = sprite(spriteAnimation) {
        scaleXY?.let {
            scale = Scale(it, it)
        }
        x = position.x
        y = position.y
    }

    sprite.playAnimationLooped(
        spriteDisplayTime = TimeSpan(displayTime ?: 0.0)
    )

    if (useRandomMoving) {
        movingRandom(sprite)
    }

    return sprite
}

/**
 * Linear random moving [Sprite] on [Container] from 0 to 512
 */
private fun Container.movingRandom(
    sprite: Sprite
) {
    var dx = getRandomMoving()
    var dy = getRandomMoving()
    addUpdater {
        if (sprite.x.isNotInContainerByWidth()) {
            dx = dx.signInverse() * getRandomMoving()
        }
        if (sprite.y.isNotInContainerByHeight()) {
            dy = dy.signInverse() * getRandomMoving()
        }
        sprite.x += dx
        sprite.y += dy
    }
}

private fun Float.isNotInContainerByWidth(): Boolean {
    return !isInContainerByWidth()
}

private fun Float.isInContainerByWidth(): Boolean {
    return this in 0f..Config.windowSize.width
}

private fun Float.isNotInContainerByHeight(): Boolean {
    return !isInContainerByHeight()
}

private fun Float.isInContainerByHeight(): Boolean {
    return this in 0f..Config.windowSize.height
}

private fun getRandomPosition(): Position {
    return Position(
        x = getRandom(max = Config.windowSize.width.toDouble()),
        y = getRandom(max = Config.windowSize.height.toDouble())
    )
}

private fun getRandomMoving(
    min: Double = Config.randomMovingMin,
    max: Double = Config.randomMovingMax
): Float {
    return Random.nextDouble(min, max).toFloat()
}

private fun Float.signInverse(): Float {
    return -1 * sign
}

private fun getRandom(
    min: Double = 0.0,
    max: Double
): Float {
    return Random.nextDouble(min, max).toFloat()
}

data class Position(
    val x: Float = 0f,
    val y: Float = 0f
)

object SpriteName {
    const val eagle = "eagle"
}
