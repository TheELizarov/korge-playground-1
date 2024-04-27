
import korlibs.image.atlas.*
import korlibs.image.color.*
import korlibs.io.file.std.*
import korlibs.korge.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.time.*
import kotlin.math.*
import kotlin.random.*

suspend fun main() = Korge(
    title = "Code Every Day - KorGE - Sunny Land",
    windowSize = Size(800, 600),
    virtualSize = Size(800, 600),
    backgroundColor = Colors["#2b2b2b"]
) {
    val spriteAtlas = resourcesVfs["Sunny-Land/atlas/atlas.json"].readAtlas()

    List(3) {
        displaySprite(
            atlas = spriteAtlas,
            name = SpriteName.eagle,
            position = Position(
                x = Random.nextDouble(0.0, 512.0).toFloat(),
                y = Random.nextDouble(0.0, 512.0).toFloat()
            )
        )
    }
}

private fun Container.displaySprite(
    atlas: Atlas,
    name: String,
    position: Position = Position(),
    scaleXY: Int? = 5,
    displayTime: Double? = 100.0
) {
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

    movingRandom(sprite)
}

private fun Container.movingRandom(
    sprite: Sprite
) {
    var dx = Random.nextDouble(1.5, 4.5)
    var dy = Random.nextDouble(1.5, 4.5)
    addUpdater {
        if (sprite.x !in 0f..512f) {
            dx = -1 * dx.sign * Random.nextDouble(1.5, 7.5)
        }
        if (sprite.y !in 0f..512f) {
            dy = -1 * dy.sign * Random.nextDouble(1.5, 7.5)
        }
        sprite.x += dx.toFloat()
        sprite.y += dy.toFloat()
    }
}

data class Position(
    val x: Float = 0f,
    val y: Float = 0f
)

object SpriteName {
    const val eagle = "eagle"
}
