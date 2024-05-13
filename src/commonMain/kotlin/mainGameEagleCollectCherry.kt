import korlibs.image.atlas.*
import korlibs.image.bitmap.*
import korlibs.image.format.*
import korlibs.io.file.std.*
import korlibs.korge.*
import korlibs.korge.animate.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.korge.view.collision.*
import korlibs.math.geom.*
import korlibs.time.*
import manager.*
import model.*
import state.*
import ui.*

val gameState = GameState()
val gameControlsManager = GameControlsManager

suspend fun initGameEagleCollectCherry() = Korge(
    title = Config.title,
    windowSize = Config.windowSize,
    virtualSize = Config.virtualSize,
    backgroundColor = Config.backgroundColors
) {
    val spriteAtlas = resourcesVfs["Sunny-Land/atlas/atlas.json"].readAtlas()
    displayBackground()
    startEagleAndCherryGame(spriteAtlas)
}

private fun Container.startEagleAndCherryGame(
    spriteAtlas: Atlas
) {

    gameState.changeState(GameState.State.PLAY)

    val spriteEagle = displaySprite(
        atlas = spriteAtlas,
        name = SpriteName.eagle,
        position = getRandomPosition(),
        useRandomMoving = false
    )

    controlByKeys(spriteEagle)

    displayScore(gameState.score)

    /**
     * If you want display simple text with counter of life
     * use  displayLife(gameState.life, Config.windowSize.width.toInt())
     */
    displayLifeControl(
        atlas = spriteAtlas,
        name = SpriteName.eagle,
        screenWidth = Config.windowSize.width.toInt(),
        value = gameState.life
    )

    /**
     * Per 5 seconds generate Cherry [Sprite]
     * in random [Position]
     *
     * If Eagle takes it [GameState.score] increment
     * and display on window
     *
     * If eagle does not take it, Cherry will be removing
     */
    var spriteCherry: Sprite? = null
    onEverySeconds {
        gameState.onPlaying {
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
                            gameState
                        )
                        spriteCherry = null
                    }
                }
            }
        }
    }

    /**
     * Per 3 seconds generate [Sprite] with [SpriteName.gem]
     * witch will be fall from top to bottom of screen
     *
     * If Eagle shall have collision with any Gem
     * it will be game over
     */
    onEverySeconds(Intervals.generateGem) {
        gameState.onPlaying {
            displayAndMovingGem(spriteAtlas)
            spriteEagle.onCollision { view ->
                when (view.name) {
                    SpriteName.gem -> {
                        removeChild(view)

                        gameState.damage()
                        /**
                         * If you want display simple text with counter of life
                         * use  displayLife(gameState.life)
                         */
                        displayLifeControl(
                            atlas = spriteAtlas,
                            name = SpriteName.eagle,
                            screenWidth = Config.windowSize.width.toInt(),
                            value = gameState.life
                        )

                        onCollisionEagleAndGem(
                            spriteAtlas,
                            spriteEagle,
                            spriteCherry
                        )
                    }
                }
            }
        }
    }

    gameControlsManager.withGameControls(
        type = GameControls.Types.playing
    ) {
        add(
            listOf(
                spriteEagle, spriteCherry, scoreText,
                lifeText, lifeUIHorizontalStack
            )
        )
    }
}

/**
 * Change [GameState.score] and display it [displayScore]
 */
private fun Container.onCollisionEagleAndCherry(
    spriteAtlas: Atlas,
    spriteCherry: Sprite?,
    stateGame: GameState
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

/**
 * Apply game over logic and display it [displayGameOver]
 */
private fun Container.onCollisionEagleAndGem(
    spriteAtlas: Atlas,
    spriteEagle: Sprite,
    spriteCherry: Sprite?
) {
    val spriteSizeDiffs = spriteEagle.getSizeDiffs()
    displaySpriteDestroyOnce(
        spriteAtlas,
        position = Position(
            spriteEagle.x + spriteSizeDiffs.x,
            spriteEagle.y + spriteSizeDiffs.y
        )
    )
    gameState.onGameOver {
        gameControlsManager.withGameControls(
            type = GameControls.Types.playing
        ) {
            list.forEach { gameControl ->
                removeChild(gameControl)
            }
        }

        displayGameOver()
    }
}

/**
 * Create [Sprite] with [SpriteName.gem] and display it on random position of top screen
 * Moving it down, change only Y coordinate
 */
private fun Container.displayAndMovingGem(
    spriteAtlas: Atlas
): Sprite {
    val spriteGem = displaySprite(
        atlas = spriteAtlas,
        name = SpriteName.gem,
        position = getRandomPositionTop(),
        useRandomMoving = false
    )

    val animator = animator(parallel = false)
    animator.moveTo(
        spriteGem,
        x = spriteGem.x,
        y = spriteGem.y + Config.windowSize.height,
        time = TimeSpan(Intervals.fallGem)
    )

    return spriteGem
}

/**
 * Display background for game
 */
private suspend fun Container.displayBackground() {
    val background = resourcesVfs["Sunny-Land/atlas/back.png"].readBitmapSlice()
    uiImage(
        size = Config.windowSize,
        bitmap = background,
        scaleMode = ScaleMode.FILL,
        contentAnchor = Anchor.CENTER
    )
}

object Intervals {
    const val fallGem = 5000.0
    const val generateGem = 3000.0
}
