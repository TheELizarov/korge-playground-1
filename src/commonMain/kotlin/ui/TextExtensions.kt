package ui

import korlibs.korge.view.*
import korlibs.math.geom.*
import model.*

var lifeText: Text? = null
fun Container.displayLife(
    value: Int,
    screenWidth: Int? = null
) {
    val text = "Life: $value"
    when (lifeText) {
        null -> {
            lifeText = text(text = text)
            lifeText?.scaleXY = 5f
        }
        else -> lifeText?.text = text
    }
    /**
     * Display text on Top Right Of Screen
     */
    screenWidth?.let { width ->
        val textScale = lifeText?.scaleXY ?: 0f
        val widthText = (lifeText?.width ?: 0f) * textScale
        val y = lifeText?.y ?: 0f

        val point = Point(
            x = width - widthText,
            y = y
        )

        lifeText?.position(point)
    }
}

var gameOverText: Text? = null
fun Container.displayGameOver() {
    val text = "Game Over!"
    when (gameOverText) {
        null -> {
            gameOverText = text(text = text)
            gameOverText?.scaleXY = 5f
        }
        else -> gameOverText?.text = text
    }
    gameOverText?.position(
        Point(200f, 200f)
    )
}

var scoreText: Text? = null
fun Container.displayScore(
    value: Int
) {
    val text = "Score: $value"
    when (scoreText) {
        null -> {
            scoreText = text(text = text)
            scoreText?.scaleXY = 5f
        }
        else -> scoreText?.text = text
    }
}

var debugText: Text? = null
fun Container.debugLog(
    message: String
) {
    Config.debug {
        when (debugText) {
            null -> debugText = text(text = message)
            else -> debugText?.text = message
        }
    }
}
