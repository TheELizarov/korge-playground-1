package manager

import state.*

/**
 * Manage different list of [GameControls]
 */
object GameControlsManager {
    val list: List<GameControls> = mutableListOf()

    fun withGameControls(
        type: String,
        block: GameControls.() -> Unit = {}
    ) {
        list.firstOrNull { gameControls ->
            gameControls.type == type
        }?.let(block)
    }
}
