import korlibs.image.color.*
import korlibs.korge.*
import korlibs.korge.annotations.*
import korlibs.korge.input.*
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

    /**
     * Example for learning base properties of button
     *  displayButton()
     */

    /**
     * Example for learning [UICheckBox]
     * displayCheckBoxes()
     */

    /**
     * Example for learning [UIComboBox]
     *  displayComboBox()
     */

    /**
     * Example for learning [UIRadioButtonGroup]
     *  displayRadioGroup()
     */

    /**
     * Example for using [UIProgressBar]
     *  displayProgressBar()
     */

    displayTree()
}

private val words = listOf(
    "Start", "Code", "Every", "Day"
)

/**
 * Example for display [UITreeView]
 */
@OptIn(KorgeExperimental::class)
private fun Container.displayTree() {

    val innerNodes = words.mapIndexed { index, value ->
        UITreeViewNode(
            value,
            UITreeViewNode("index $index")
        )
    }

    val outerNodes = UITreeViewNode(
        "KorGE",
        innerNodes
    )

    uiTreeView(
        UITreeViewList(
            listOf(outerNodes)
        )
    )
}

/**
 * Example for animate [UIProgressBar]
 * by using [onEverySeconds] callback for
 * changing progress
 */
private fun Container.displayProgressBar() {
    val min = 0f
    val max = 100f

    val uiProgressBar =  uiProgressBar(
        size = Size(400f, 50f),
        current = min,
        maximum = max
    )
    uiProgressBar.position(Point(100f, 20f))

    onEverySeconds(10.0) {
        var current = uiProgressBar.current
        current = when {
            current < max -> current + 0.25f
            else -> min
        }
        uiProgressBar.current = current
    }
}

/**
 * Example for display [UIRadioButtonGroup] and [UIRadioButton]
 */
private fun Container.displayRadioGroup() {
    withDebugLogs {
        val group = UIRadioButtonGroup()
        words.map {
            uiRadioButton(
                text = it,
                group = group
            ) {
                onClick {
                    debugLog("Set $text")
                }
            }
        }
    }
}

/**
 * Container witch add logs above container from block
 */
private fun Container.withDebugLogs(
    block: @ViewDslMarker UIVerticalStack.() -> Unit = {}
) {
    Config.textColor = Colors.WHITE
    uiVerticalStack {
        debugLog("Select item")
        block()
    }
}


/**
 * Example for display [UIComboBox]
 */
private fun Container.displayComboBox() {
    withDebugLogs {
        uiComboBox(
            items = words
        ) {
            onSelectionUpdate {
                debugLog(
                    message = "${this.selectedItem}"
                )
            }
        }
    }
}

/**
 * Example for  display list of check boxes
 */
private fun Container.displayCheckBoxes() {
    Config.textColor = Colors.WHITE
    uiVerticalStack {
        debugLog("debug log")

        words.map { word ->
            uiCheckBox(
                text = word,
                checked = true
            ) {
                onChange {
                    debugLog("check $text = $checked")
                }
            }
        }
    }
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

/**
 * Animate change [UIButton] background corners radius
 * on any updating frames using [onEverySeconds]
 */
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

/**
 * Animate change label text of [UIButton] on any updating frames
 */
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
