package ui

import korlibs.image.text.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import model.*

var lifeText: Text? = null
fun Container.displayLife(
    value: Int
) {
    val text = "Life: $value"
    when (lifeText) {
        null -> {
            lifeText = text(text = text)
            lifeText?.scaleXY = 5f
        }
        else -> lifeText?.text = text
    }
    lifeText?.horizontalAlign = HorizontalAlign.RIGHT
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
