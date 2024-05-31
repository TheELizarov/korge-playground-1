import korlibs.image.color.*
import korlibs.korge.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.render.*
import model.*
import ui.*
import kotlin.random.*

suspend fun initUI() = Korge(
    title = Config.title,
    windowSize = Config.windowSize,
    virtualSize = Config.virtualSize,
    backgroundColor = Config.backgroundColors,
    quality = GameWindow.Quality.PERFORMANCE
) {
    /**
     * Example for button click counter
     *  displayButtonClickCounter()
     */

    displayButton()
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

/**
 * Example for learn base properties of [UIButton]
 */
private var isClicked = false
private fun Container.displayButton() {
    val button = uiButton(
        label = "Start Code Every Day"
    ) {
        width = 200f
        height = 100f
        bgColorOver = Colors.DARKCYAN
        textSize = 20f

        clicked {
            isClicked = isClicked.not()
        }
    }

    animateChangingButtonRadius(button)
    animateChangingButtonTextColor(button)
}

private fun Container.animateChangingButtonRadius(
    button: UIButton
) {
    val maxRadius = 45f
    val minRadius = 0f
    var stepChangingRadius = 0.5
    onEverySeconds(25.0) {
        if (isClicked) {
            val currentRadius = button.background.radius
            if (currentRadius.topLeft > maxRadius || currentRadius.topLeft < minRadius) {
                stepChangingRadius *= -1
            }
            button.background.radius = RectCorners(
                currentRadius.topLeft + stepChangingRadius
            )
        }
    }
}

private fun Container.animateChangingButtonTextColor(
    button: UIButton
) {
    val colors = Colors.colorsByName.values.toList()
    onEverySeconds(500.0) {
        if (isClicked) {
            val index = Random.nextInt(0, colors.size)
            button.textColor = colors[index]
        }
    }
}
