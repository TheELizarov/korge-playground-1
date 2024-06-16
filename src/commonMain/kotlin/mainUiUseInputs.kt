import korlibs.korge.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.render.*
import model.*

private val content = mutableListOf<String>()

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

private fun Container.displayInputs() {

}

private fun Container.displayContent() {

}

private fun Container.getContentItemView(): View? {
   return null
}
