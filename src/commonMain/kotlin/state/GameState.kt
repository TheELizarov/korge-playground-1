package state

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
    private var _life: Int = 0

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
        const val max = 10
        const val damage = 1
    }
}
