import korlibs.korge.*
import korlibs.korge.annotations.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.render.*
import model.*
import ui.*

private val content: MutableList<String> = mutableListOf()
private var contentUiVerticalStack: UIVerticalStack? = null

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
    uiHorizontalStack {
        displayInputs()
        displayContent()
    }
}

/**
 * Display input for create new item
 */
@OptIn(KorgeExperimental::class)
private fun Container.displayInputs() {
    uiVerticalStack {
        val input = uiTextInput()
        uiButton(label = "Добавить") {
            clicked {
                onInput(input)
            }
        }
    }
}

/**
 * On click [UIButton] from [displayInputs]
 * for add content for [displayContent]
 */
@OptIn(KorgeExperimental::class)
private fun Container.onInput(
    input: UITextInput
) {
    val value = input.text
    if (value.isNotEmpty()) {
        content.add(0, value)
        input.text = ""

        updateContent()
    }
}

private fun Container.updateContent() {
    contentUiVerticalStack?.let { stack ->
        stack.removeChildren()
        stack.addChildren(
            content.map { value ->
                uiText(value)
            }
        )
    }
}

/**
 * Display list of created items from [displayInputs]
 */
private fun Container.displayContent() {
    contentUiVerticalStack = uiVerticalStack{
        content.map { value ->
            uiText(value)
        }
    }
}
