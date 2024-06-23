import korlibs.korge.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.render.*
import model.*

private val content = mutableListOf<String>()

/**
 * Simple example for using inputs for creating
 * list of items
 *
 * Items can be deleting
 */
suspend fun initUiUseInputs() = Korge(
    title = Config.title,
    windowSize = Config.windowSize,
    virtualSize = Config.virtualSize,
    backgroundColor = Config.backgroundColors,
    quality = GameWindow.Quality.PERFORMANCE
) {
    displayInputs()
    displayContent()
}

/**
 * Display input for create new item
 */
private fun Container.displayInputs() {

}

/**
 * Display list of created items from [displayInputs]
 */
private fun Container.displayContent() {

}

private fun Container.getContentItemView(): View? {
   return null
}
