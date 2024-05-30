import korlibs.korge.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.render.*
import model.*

suspend fun initUI() = Korge(
    title = Config.title,
    windowSize = Config.windowSize,
    virtualSize = Config.virtualSize,
    backgroundColor = Config.backgroundColors,
    quality = GameWindow.Quality.PERFORMANCE
) {
    displayButtonClickCounter()
}

private var clickCounterByButton = 0
private val clickCounterButtonLabel: String
    get() = "Clicks count $clickCounterByButton"

/**
 * Example for creating button of counter clicks
 */
private fun Container.displayButtonClickCounter() {
    uiButton(
        label = clickCounterButtonLabel
    ) {
        clicked {
            ++clickCounterByButton
            text = clickCounterButtonLabel
        }
    }
}
