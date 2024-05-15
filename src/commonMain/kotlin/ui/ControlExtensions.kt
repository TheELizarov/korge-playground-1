package ui

import korlibs.event.*
import korlibs.korge.animate.*
import korlibs.korge.input.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import model.*

/**
 * Handling click of mouse and moving sprite to [Point] of clicks
 */
fun Container.controlByMouse(
    sprite: Sprite
) {
    mouse {
        click {
            val point = input.mousePos
            val spriteSizeDiffs = sprite.getSizeDiffs()

            /**
             * Calculate x position of [Sprite]
             * X is top left
             * If user click on x = 0, we must set x + [Sprite.width] / 2 because
             * [Sprite] will be visible full by width
             * And the same for click on end of the window
             */
            var x = point.x - spriteSizeDiffs.x
            x = when {
                x < 0 -> 0f
                x > Config.windowSize.width -> Config.windowSize.width - 2 * spriteSizeDiffs.x
                else -> x
            }

            /**
             * The same logic as for calculating X
             */
            var y = point.y - spriteSizeDiffs.y
            y = when {
                y < 0 -> 0f
                y > Config.windowSize.height -> Config.windowSize.height - 2 * spriteSizeDiffs.y
                else -> y
            }

            val animator = animator(parallel = false)
            animator.moveTo(
                sprite,
                x, y,
                time = TimeSpan(2000.0),
                easing = Easing.EASE_OUT
            )

            debugLog("Mouse click point = $point")
        }
    }
}

/**
 * Using drag-and-drop for [Sprite]
 */
fun Container.controlByDragAndDrop(
    sprite: Sprite
) {
    sprite.draggableCloseable(
        selector = sprite,
        autoMove = true
    ) { draggableInfo ->
        val x = draggableInfo.viewNextX
        val y = draggableInfo.viewNextY

        debugLog("Drag and drop to point = $x,$y ")

        /**
         * Correcting position [Sprite] if user
         * drag it from window
         * It return [Sprite] on Screen
         */
        val correctionX = when {
            x < 0 -> 0f
            x > Config.windowSize.width -> Config.windowSize.width - 2 * sprite.getSizeDiffs().x
            else -> x
        }
        val correctionY = when {
            y < 0 -> 0f
            y > Config.windowSize.height -> Config.windowSize.height - 2 * sprite.getSizeDiffs().y
            else -> y
        }
        if (correctionX != x || correctionY != y) {
            val animator = animator(parallel = false)
            animator.moveTo(
                view = sprite,
                x = correctionX,
                y = correctionY
            )
        }
    }
}

/**
 * Handling keyboard clicks for control to position of [Sprite]
 * Default value of changing position is 50f
 */
fun Container.controlByKeys(
    sprite: Sprite,
    useMirrorByX: Boolean = false,
    blockOnClickOuter: (Key) -> Unit = {}
) {
    val diffs = 50f
    val blockOnClick: (x: Float, y: Float, log: String) -> Unit = { x, y, log ->
        val animator = animator(parallel = false)

        val maxX = Config.windowSize.width - 2 * sprite.getSizeDiffs().x
        val maxY = Config.windowSize.height - 2 * sprite.getSizeDiffs().y

        var toX = sprite.x + x
        toX = when {
            toX < 0f -> 0f
            toX > maxX -> maxX
            else -> toX
        }

        var toY = sprite.y + y
        toY = when {
            toY < 0f -> 0f
            toY > maxY -> maxY
            else -> toY
        }

        animator.moveTo(
            view = sprite,
            x = toX,
            y = toY
        )
        debugLog("Click Key = $log, move to point = $toX, $toY")
    }
    keys {
        down(Key.LEFT) {
            if (useMirrorByX) {
                sprite.mirrorByX()
            }
            blockOnClick(-diffs,  0f, "left")
            blockOnClickOuter(Key.LEFT)
        }
        down(Key.RIGHT) {
            if (useMirrorByX) {
                sprite.mirrorByX(reset = true)
            }
            blockOnClick(diffs,  0f, "right")
            blockOnClickOuter(Key.RIGHT)
        }
        down(Key.UP) {
            blockOnClick(0f,  -diffs, "up")
            blockOnClickOuter(Key.UP)
        }
        down(Key.DOWN) {
            blockOnClick(0f,  diffs, "down")
            blockOnClickOuter(Key.DOWN)
        }
    }
}

/**
 * Control [Sprite] by [VirtualController]
 */
fun Container.controlByVirtualController(
    sprite: Sprite
) {
    val buttonRadius = 110f
    val virtualController = virtualController(
        buttons = listOf(),
        buttonRadius = buttonRadius
    ).also { it.container.alpha(0.5f) }

    val diffs = 50f
    val blockOnClick: (x: Float, y: Float, log: String) -> Unit = { x, y, log ->
        val animator = animator(parallel = false)

        val maxX = Config.windowSize.width - 2 * sprite.getSizeDiffs().x
        val maxY = Config.windowSize.height - 2 * sprite.getSizeDiffs().y

        var toX = sprite.x + x
        toX = when {
            toX < 0f -> 0f
            toX > maxX -> maxX
            else -> toX
        }

        var toY = sprite.y + y
        toY = when {
            toY < 0f -> 0f
            toY > maxY -> maxY
            else -> toY
        }

        animator.moveTo(
            view = sprite,
            x = toX,
            y = toY
        )
        debugLog("Stick = $log, move to point = $toX, $toY")
    }


    virtualController.changed(GameButton.LX) { event ->
        if (event.newBool) {
            val dx = when {
                event.new > 0f -> diffs
                else -> -diffs
            }
            blockOnClick(dx, 0f, "horizontal")
        }
    }
    virtualController.changed(GameButton.LY) { event ->
        if (event.newBool) {
            val dy = when {
                event.new > 0f -> diffs
                else -> -diffs
            }
            blockOnClick(0f, dy, "vertical")
        }
    }
}
