import korlibs.event.*
import korlibs.image.atlas.*
import korlibs.io.file.std.*
import korlibs.korge.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.time.*
import model.*
import ui.*

suspend fun initPersonAnimations() = Korge(
    title = Config.title,
    windowSize = Config.windowSize,
    virtualSize = Config.virtualSize,
    backgroundColor = Config.backgroundColors
) {
    val spriteAtlas = resourcesVfs["Sunny-Land/atlas/atlas.json"].readAtlas()

    val playerIdle = spriteAtlas.getSpriteAnimation("player/idle")
    val playerJump = spriteAtlas.getSpriteAnimation("player/jump")
    val playerRun = spriteAtlas.getSpriteAnimation("player/run")

    val playerStates = setOf(
        PlayerState(
            animation = playerIdle,
            state = State.IDLE
        ),
        PlayerState(
            animation = playerRun,
            state = State.RUN
        ),
        PlayerState(
            animation = playerJump,
            state = State.JUMP
        )
    )

    val spritePlayer = getPlayerSprite(playerIdle)

    addUpdater {
        val animation = when {
            views.input.keys[Key.RIGHT] -> {
                spritePlayer.x += PlayerParams.movingSpeedX
                spritePlayer.mirrorByX(reset = true)

                playerRun
            }

            views.input.keys[Key.LEFT] -> {
                spritePlayer.x -= PlayerParams.movingSpeedX
                spritePlayer.mirrorByX()

                playerRun
            }

            views.input.keys[Key.UP] -> playerJump
            else -> playerIdle
        }
        spritePlayer.playAnimation(animation)
    }
}

private fun Container.getPlayerSprite(
    animation: SpriteAnimation
) : Sprite {
    val result = sprite(animation)
    result.position(PlayerParams.position)
    result.scaleXY = PlayerParams.scale
    result.playAnimationLooped(
        spriteDisplayTime = PlayerParams.frameDelay
    )
    return result
}

data class PlayerState(
    val animation: SpriteAnimation,
    val state: State
)

enum class State {
    IDLE,
    RUN,
    JUMP
}

/**
 * Init player [Sprite] for [getPlayerSprite]
 */
object PlayerParams {
    const val scale = 7f
    val position = Point(200f, 200f)
    val frameDelay = TimeSpan(200.0)
    val movingSpeedX = 1f
}
