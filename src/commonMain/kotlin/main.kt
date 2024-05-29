
import korlibs.image.atlas.*
import korlibs.io.file.std.*
import korlibs.korge.*
import korlibs.korge.view.*
import korlibs.korge.view.collision.*
import korlibs.math.geom.*
import model.*
import ui.*

//suspend fun main() = initGameEagleCollectCherry()
//suspend fun main() = initPersonAnimations()
//suspend fun main() = initPhysics()
suspend fun main() = initUI()

suspend fun initSimpleDisplaySpritesAndWorkWithIt() = Korge(
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

    testCollisionsOfSprites(spriteAtlas)
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


