package state

import kotlin.math.*

/**
 * Logic model for simple game "Eagle and Cherries"
 * GamePlay (part 1):
 *  - Eagle can fly on the window, user control Eagle by keyboard
 *  left, right, up, down keys
 *  - Cherries display random on the window
 *  - Eagle must fly to cherry and eat it, after that score increment
 */
class GameState {
    val score: Int
        get() = _score
    private var _score: Int = 0

    val life: Int
        get() = _life
    private var _life: Int = Life.max

    val state: State
        get() = _state
    private var _state: State = State.LAUNCH

    fun increment(
        value: Int = 1
    ) {
        _score += value
    }

    fun changeState(
        state: State
    ) {
        _state = state
    }

    fun onPlaying(
        block: () -> Unit = {}
    ) {
        when (state) {
            State.PLAY -> block()
            else -> Unit
        }
    }

    fun onGameOver(
        block: () -> Unit = {}
    ) {
        when (state) {
            State.GAME_OVER -> block()
            else -> Unit
        }
    }

    fun health(
        value: Int = Life.health
    ) {
        _life += value
        checkLife()
    }

    fun damage(
        value: Int = Life.damage
    ) {
        _life -= value
        checkLife()
    }

    private fun checkLife() {
        if (life <= Life.min) {
            changeState(State.GAME_OVER)
        }
    }

    fun reset() {
        _score = 0
        _life = Life.max
        _state = State.LAUNCH
    }

    /**
     * Available states of game
     */
    enum class State {
        /**
         * First state of the game
         */
        LAUNCH,

        /**
         * Base state when user playing
         */
        PLAY,

        /**
         * User set pause for game
         */
        PAUSE,

        /**
         * User go to menu of the game
         */
        MENU,

        /**
         * User lose
         */
        GAME_OVER,
    }

    object Life {
        const val min = 0
        const val max = 3

        const val damage = 1
        const val health = 1
    }
}
