package ui

import korlibs.image.atlas.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.time.*
import model.*
import kotlin.math.*
import kotlin.random.*

/**
 * Create [Sprite] for animate destroy [Sprite] with [SpriteName.destroy] for any
 * in [position]
 * [SpriteAnimation] play once and after that [Sprite] remove from [Container]
 */
fun Container.displaySpriteDestroyOnce(
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
 * Create Sprite from [Atlas] with prefix [name] and
 * display it on Container with [Position].
 * Sprite can scale with [scaleXY] and delay for frame display with [displayTime]
 */
fun Container.displaySprite(
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
fun Container.movingRandom(
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

fun Float.isNotInContainerByWidth(): Boolean {
    return !isInContainerByWidth()
}

fun Float.isInContainerByWidth(): Boolean {
    return this in 0f..Config.windowSize.width
}

fun Float.isNotInContainerByHeight(): Boolean {
    return !isInContainerByHeight()
}

fun Float.isInContainerByHeight(): Boolean {
    return this in 0f..Config.windowSize.height
}

fun getRandomPosition(): Position {
    return Position(
        x = getRandom(max = Config.windowSize.width.toDouble()),
        y = getRandom(max = Config.windowSize.height.toDouble())
    )
}

fun getRandomPositionTop(): Position {
    return Position(
        x = getRandom(max = Config.windowSize.width.toDouble()),
        y = 0f
    )
}

fun getCenterPosition(): Position {
    return Position(
        x = Config.windowSize.width.div(2),
        y = Config.windowSize.height.div(2),
    )
}

fun getRandomMoving(
    min: Double = Config.randomMovingMin,
    max: Double = Config.randomMovingMax
): Float {
    return Random.nextDouble(min, max).toFloat()
}

fun Float.signInverse(): Float {
    return -1 * sign
}

fun getRandom(
    min: Double = 0.0,
    max: Double
): Float {
    return Random.nextDouble(min, max).toFloat()
}

/**
 * Calculate half width and half height of [Sprite] with [Sprite.scaleXY]
 * @return [Position] with half values
 */
fun Sprite.getSizeDiffs(): Position {
    val x = (width * scaleXY).div(2)
    val y = (height * scaleXY).div(2)
    return Position(x, y)
}

object SpriteName {
    const val eagle = "eagle"
    const val cherry = "cherry"
    const val destroy = "enemy-death"
    const val gem = "gem"
}

data class Position(
    val x: Float = 0f,
    val y: Float = 0f
)
