import korlibs.event.*
import korlibs.image.atlas.*
import korlibs.image.color.*
import korlibs.io.file.std.*
import korlibs.korge.*
import korlibs.korge.animate.*
import korlibs.korge.input.*
import korlibs.korge.view.*
import korlibs.korge.view.collision.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import state.*
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

    /**
     * if you want to test different methods of control Sprite
     *  testInputsForControlling(spriteAtlas)
     */

    /**
     * If you want to test collisions of Sprite
     *  testCollisionsOfSprites(spriteAtlas)
     */

    startEagleAndCherryGame(spriteAtlas)
}

private fun Container.startEagleAndCherryGame(
    spriteAtlas: Atlas
) {
    val stateGame = EagleAndCherryGameState()

    val spriteEagle = displaySprite(
        atlas = spriteAtlas,
        name = SpriteName.eagle,
        position = getRandomPosition(),
        useRandomMoving = false
    )

    controlByKeys(spriteEagle)

    val score = text(
        text = "Score: ${stateGame.score}"
    )
    score.scaleXY = 5f

    /**
     * Per 5 seconds generate Cherry [Sprite]
     * in random [Position]
     *
     * If Eagle takes it [EagleAndCherryGameState.score] increment
     * and display on window
     *
     * If eagle does not take it, Cherry will be removing
     */
    var spriteCherry: Sprite? = null
    onEverySeconds {
        removeChild(spriteCherry)
        spriteCherry = null

        spriteCherry = displaySprite(
            atlas = spriteAtlas,
            name = SpriteName.cherry,
            position = getRandomPosition(),
            useRandomMoving = false
        )

        spriteEagle.onCollision { view ->
            when (view.name) {
                SpriteName.cherry -> {
                    onCollisionEagleAndCherry(
                        spriteAtlas,
                        spriteCherry,
                        score,
                        stateGame
                    )
                    spriteCherry = null
                }
            }
        }
    }
}

private fun Container.onEverySeconds(
    intervalInSeconds: Double = 5000.0,
    block: () -> Unit = {}
) {
    addFixedUpdater(
        timesPerSecond = Frequency.from(TimeSpan(intervalInSeconds))
    ) {
        block()
    }
}

private fun Container.onCollisionEagleAndCherry(
    spriteAtlas: Atlas,
    spriteCherry: Sprite?,
    score: Text,
    stateGame: EagleAndCherryGameState
) {
    spriteCherry?.let { sprite ->
        stateGame.increment()
        score.text = "Score: ${stateGame.score}"

        val spriteSizeDiffs = sprite.getSizeDiffs()
        displaySpriteDestroyOnce(
            spriteAtlas,
            position = Position(
                sprite.x + spriteSizeDiffs.x,
                sprite.y + spriteSizeDiffs.y
            )
        )

        removeChild(spriteCherry)
    }
}

/**
 * Create two [Sprite] and check it collisions
 */
private fun Container.testCollisionsOfSprites(
    spriteAtlas: Atlas
) {
    val spriteEagle = displaySprite(
        atlas = spriteAtlas,
        name = SpriteName.eagle,
        position = getRandomPosition(),
        useRandomMoving = false
    )

    val spriteCherry = displaySprite(
        atlas = spriteAtlas,
        name = SpriteName.cherry,
        position = getCenterPosition(),
        useRandomMoving = false
    )

    controlByKeys(spriteEagle)

    spriteEagle.onCollision { view ->
        when (view.name) {
            SpriteName.cherry -> {
                displaySpriteDestroyOnce(
                    spriteAtlas,
                    position = Position(
                        spriteCherry.x,
                        spriteCherry.y
                    )
                )
                removeChild(spriteCherry)
            }
        }
    }
}

/**
 * Create [Sprite] for animate destroy [Sprite] with [SpriteName.destroy] for any
 * in [position]
 * [SpriteAnimation] play once and after that [Sprite] remove from [Container]
 */
private fun Container.displaySpriteDestroyOnce(
    atlas: Atlas,
    position: Position
) {
    val sprite = displaySprite(
        atlas = atlas,
        name = SpriteName.destroy,
        position = position,
        useRandomMoving = false,
        playAnimationLooped = false
    )
    val spriteDiffs = sprite.getSizeDiffs()
    sprite.position(
        Point(
            position.x - spriteDiffs.x,
            position.y - spriteDiffs.y
        )
    )
    sprite.onAnimationCompleted.invoke {
        removeChild(sprite)
    }
}

/**
 * Control moving [Sprite] to [Point] on [Container]
 * in witch user click.
 * @see <a href="https://docs.korge.org/views/input/">KorGE:Input</a>
 */
private fun Container.testInputsForControlling(
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
    controlByVirtualController(sprite)
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
 * Control [Sprite] by [VirtualController]
 */
private fun Container.controlByVirtualController(
    sprite: Sprite
) {
    val buttonRadius = 110f
    val virtualController = virtualController(
        buttons = listOf(),
        buttonRadius = buttonRadius
    ).also { it.container.alpha(0.5f) }

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
        debugLog("Stick = $log, move to point = $toX, $toY")
    }


    virtualController.changed(GameButton.LX) { event ->
        if (event.newBool) {
            val dx = when {
                event.new > 0f -> diffs
                else -> -diffs
            }
            blockOnClick(dx, 0f, "horizontal")
        }
    }
    virtualController.changed(GameButton.LY) { event ->
        if (event.newBool) {
            val dy = when {
                event.new > 0f -> diffs
                else -> -diffs
            }
            blockOnClick(0f, dy, "vertical")
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
    useRandomMoving: Boolean = true,
    playAnimationLooped: Boolean = true
): Sprite {
    val spriteAnimation = atlas.getSpriteAnimation(name)

    val sprite = sprite(spriteAnimation) {
        scaleXY?.let {
            scale = Scale(it, it)
        }
        x = position.x
        y = position.y
    }
    sprite.name = name

    when {
        playAnimationLooped -> {
            sprite.playAnimationLooped(
                spriteDisplayTime = TimeSpan(displayTime ?: 0.0)
            )
        }
        else -> {
            sprite.playAnimation(
                spriteDisplayTime = TimeSpan(displayTime ?: 0.0)
            )
        }
    }


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

private fun getCenterPosition(): Position {
    return Position(
        x = Config.windowSize.width.div(2),
        y = Config.windowSize.height.div(2),
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
    const val cherry = "cherry"
    const val destroy = "enemy-death"
}
