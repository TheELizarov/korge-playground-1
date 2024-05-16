import korlibs.event.*
import korlibs.image.atlas.*
import korlibs.io.file.std.*
import korlibs.korge.*
import korlibs.korge.input.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.time.*
import model.*
import ui.*
import kotlin.math.*
import kotlin.time.Duration.Companion.milliseconds

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

    val spritePlayer = sprite(playerIdle)
    spritePlayer.position(Point(200f, 200f))
    spritePlayer.scaleXY = 7f
    spritePlayer.playAnimationLooped(
        spriteDisplayTime = TimeSpan(200.0)
    )

    addUpdater {
        val animation = when {
            views.input.keys[Key.RIGHT] -> {
                spritePlayer.x += 1
                spritePlayer.mirrorByX(reset = true)

                playerRun
            }

            views.input.keys[Key.LEFT] -> {
                spritePlayer.x -= 1
                spritePlayer.mirrorByX()

                playerRun
            }

            views.input.keys[Key.UP] -> playerJump
            else -> playerIdle
        }
        spritePlayer.playAnimation(animation)
    }
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
