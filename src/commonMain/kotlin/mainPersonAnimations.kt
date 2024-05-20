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

    val playerStates = getPlayerStates(spriteAtlas)

    val playerIdle = playerStates.get(State.IDLE)
    val playerRun = playerStates.get(State.RUN)

    val spritePlayer = getPlayerSprite(playerIdle.animation)

    addUpdater {
        val state = when {
            views.input.keys[Key.RIGHT] -> {
                spritePlayer.x += PlayerParams.movingVelocityX
                spritePlayer.mirrorByX(reset = true)

                playerRun
            }

            views.input.keys[Key.LEFT] -> {
                spritePlayer.x -= PlayerParams.movingVelocityX
                spritePlayer.mirrorByX()

                playerRun
            }

            views.input.keys[Key.UP] -> playerStates.get(State.JUMP)
            views.input.keys[Key.Q] -> playerStates.get(State.HURT)
            views.input.keys[Key.W] -> playerStates.get(State.CROUCH)
            views.input.keys[Key.E] -> playerStates.get(State.CLIMB)
            else -> playerIdle
        }
        spritePlayer.playAnimation(state.animation)
    }
}

private fun Container.getPlayerStates(
    spriteAtlas: Atlas
): Set<PlayerState> {
    val playerIdle = spriteAtlas.getSpriteAnimation("player/idle")
    val playerJump = spriteAtlas.getSpriteAnimation("player/jump")
    val playerRun = spriteAtlas.getSpriteAnimation("player/run")
    val playerHurt = spriteAtlas.getSpriteAnimation("player/hurt")
    val playerCrouch = spriteAtlas.getSpriteAnimation("player/crouch")
    val playerClimb = spriteAtlas.getSpriteAnimation("player/climb")

    return setOf(
        PlayerState(playerIdle, State.IDLE), PlayerState(playerRun, State.RUN),
        PlayerState(playerJump, State.JUMP), PlayerState(playerHurt, State.HURT),
        PlayerState(playerCrouch, State.CROUCH), PlayerState(playerClimb, State.CLIMB),
    )
}

private fun Set<PlayerState>.get(
    state: State
): PlayerState {
    return first { value ->
        value.state == state
    }
}

private fun Container.getPlayerSprite(
    animation: SpriteAnimation
): Sprite {
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
    IDLE, RUN, JUMP,
    HURT, CROUCH, CLIMB
}

/**
 * Init player [Sprite] for [getPlayerSprite]
 */
object PlayerParams {
    const val scale = 7f
    val position = Point(200f, 200f)
    val frameDelay = TimeSpan(200.0)
    val movingVelocityX = 1f
}
