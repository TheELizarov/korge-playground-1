package ui

import korlibs.korge.view.*
import model.*

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
