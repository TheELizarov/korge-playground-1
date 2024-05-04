import korlibs.image.atlas.*
import korlibs.io.file.std.*
import korlibs.korge.*
import korlibs.korge.view.*
import korlibs.korge.view.collision.*
import model.*
import state.*
import ui.*

suspend fun initGameEagleCollectCherry() = Korge(
    title = Config.title,
    windowSize = Config.windowSize,
    virtualSize = Config.virtualSize,
    backgroundColor = Config.backgroundColors
) {
    val spriteAtlas = resourcesVfs["Sunny-Land/atlas/atlas.json"].readAtlas()

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

    displayScore(stateGame.score)

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
                        stateGame
                    )
                    spriteCherry = null
                }
            }
        }
    }
}

private fun Container.onCollisionEagleAndCherry(
    spriteAtlas: Atlas,
    spriteCherry: Sprite?,
    stateGame: EagleAndCherryGameState
) {
    spriteCherry?.let { sprite ->
        stateGame.increment()
        displayScore(stateGame.score)

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
