package ui

import korlibs.korge.view.*
import korlibs.time.*

fun Container.onEverySeconds(
    intervalInSeconds: Double = 5000.0,
    block: () -> Unit = {}
) {
    addFixedUpdater(
        timesPerSecond = Frequency.from(TimeSpan(intervalInSeconds))
    ) {
        block()
    }
}
