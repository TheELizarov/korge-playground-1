import korlibs.event.*
import korlibs.image.atlas.*
import korlibs.io.file.std.*
import korlibs.korge.*
import korlibs.korge.input.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.time.*
import model.*
import kotlin.time.Duration.Companion.milliseconds

suspend fun initPersonAnimations() = Korge(
    title = Config.title,
    windowSize = Config.windowSize,
    virtualSize = Config.virtualSize,
    backgroundColor = Config.backgroundColors
) {
    val spriteAtlas = resourcesVfs["Sunny-Land/atlas/atlas.json"].readAtlas()

    val playerClimb = spriteAtlas.getSpriteAnimation("player/climb")
    val playerCrunch = spriteAtlas.getSpriteAnimation("player/crouch")
    val playerHurt = spriteAtlas.getSpriteAnimation("player/hurt")

    val playerIdle = spriteAtlas.getSpriteAnimation("player/idle")
    val playerJump = spriteAtlas.getSpriteAnimation("player/jump")
    val playerRun = spriteAtlas.getSpriteAnimation("player/run")

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
                playerRun
            }

            views.input.keys[Key.LEFT] -> {
                spritePlayer.x -= 1
                playerRun
            }

            views.input.keys[Key.UP] -> playerJump
            else -> playerIdle
        }
        spritePlayer.playAnimation(animation)
    }
}
