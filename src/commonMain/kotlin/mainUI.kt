import korlibs.korge.*
import korlibs.render.*
import model.*

suspend fun initUI() = Korge(
    title = Config.title,
    windowSize = Config.windowSize,
    virtualSize = Config.virtualSize,
    backgroundColor = Config.backgroundColors,
    quality = GameWindow.Quality.PERFORMANCE
) {

}
