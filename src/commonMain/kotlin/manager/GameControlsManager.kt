package manager

import state.*

/**
 * Manage different list of [GameControls]
 */
object GameControlsManager {
    /**
     * Default list of [GameControls]
     */
    private val list: MutableList<GameControls> = GameControls.Types.list.map { type ->
        GameControls(type)
    }.toMutableList()

    fun withGameControls(
        type: String,
        block: GameControls.() -> Unit = {}
    ) {
        list.firstOrNull { gameControls ->
            gameControls.type == type
        }?.let(block)
    }

    fun add(
        value: GameControls
    ) {
        list.add(value)
    }
}
