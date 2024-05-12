package state

import korlibs.korge.view.*

/**
 * List of controls for different states of Game
 */
class GameControls(
    /**
     * Use const from [GameControls.Types]
     */
    val type: String
) {
    val list: List<View?>
        get() = _list
    private val _list: MutableList<View?> = mutableListOf()

    fun add(
        value: List<View?>
    ) {
        _list.addAll(value)
    }

    fun add(
        value: View?
    ) {
        _list.add(value)
    }

    fun remove(
        value: View?
    ) {
        _list.remove(value)
    }

    fun clear() {
        _list.clear()
    }

    object Types {
        const val launch = "launch"
        const val playing = "playing"
        const val settings = "settings"
        const val pause = "pause"
        const val gameover = "gameover"

        val list = setOf(
            launch, playing, settings,
            pause, gameover
        )
    }
}
