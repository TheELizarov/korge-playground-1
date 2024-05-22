
import korlibs.korge.*
import korlibs.korge.animate.*
import korlibs.korge.box2d.*
import korlibs.korge.input.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import model.*
import org.jbox2d.dynamics.*
import ui.*

suspend fun initPhysics() = Korge(
    title = Config.title,
    windowSize = Config.windowSize,
    virtualSize = Config.virtualSize,
    backgroundColor = Config.backgroundColors
) {
    generateCircles()
}

private fun Container.generateCircles() {
    solidRect(920, 100)
        .xy(0, 620)
        .registerBodyWithFixture(
            type = BodyType.STATIC,
            friction = 0.2,
            restitution = 0.2
        )

    for (n in 0 until 5) {
        val item = circle(50f)
            .xy(120 + 140 * n, 246)
            .anchor(Anchor.CENTER)
            .registerBodyWithFixture(
                type = BodyType.DYNAMIC,
                linearVelocityY = 6.0,
                friction = 0.2,
                restitution = 0.3 + (n * 0.1)
            )
        controlByDragAndDrop(item)
    }
}

fun Container.controlByMouse(
    view: View
) {
    mouse {
        click {
            val point = input.mousePos
            val x = point.x
            val y = point.y

            val animator = animator(parallel = false)
            animator.moveTo(
                view,
                x, y,
                time = TimeSpan(2000.0),
                easing = Easing.EASE_OUT
            )

            debugLog("Mouse click point = $point")
        }
    }
}

fun Container.controlByDragAndDrop(
    view: View
) {
    view.draggableCloseable(
        selector = view,
        autoMove = true
    ) { draggableInfo ->
        val x = draggableInfo.viewNextX
        val y = draggableInfo.viewNextY
        view.x = x
        view.y = y

//        val animator = animator(parallel = false)
//        animator.moveTo(
//            view = view,
//            x = x,
//            y = y
//        )
    }
}
