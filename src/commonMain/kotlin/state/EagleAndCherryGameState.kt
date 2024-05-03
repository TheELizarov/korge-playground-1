package state

/**
 * Logic model for simple game "Eagle and Cherries"
 * GamePlay (part 1):
 *  - Eagle can fly on the window, user control Eagle by keyboard
 *  left, right, up, down keys
 *  - Cherries display random on the window
 *  - Eagle must fly to cherry and eat it, after that score increment
 */
class EagleAndCherryGameState {
    val score: Int
        get() = _score
    private var _score: Int = 0

    fun increment(
        value: Int = 1
    ) {
        _score += value
    }

    fun reset() {
        _score = 0
    }
}
