package model

import korlibs.image.color.*
import korlibs.math.geom.*

object Config {
    const val title = "Code Every Day - KorGE - Sunny Land"

    const val width = 800
    const val height = 600

    val windowSize = Size(width, height)
    val virtualSize = Size(width, height)

    const val backgroundColorValue = "#2b2b2b"
    val backgroundColors = Colors[backgroundColorValue]

    const val randomMovingMin = 1.5
    const val randomMovingMax = 5.0

    const val isDebug = true

    fun debug(
        block: () -> Unit = {}
    ) {
        if (isDebug) {
            block()
        }
    }
}
