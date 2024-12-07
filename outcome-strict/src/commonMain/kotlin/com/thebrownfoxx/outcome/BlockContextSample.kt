package com.thebrownfoxx.outcome

private fun main() {
    val winner = selectPlayer(1).getOrElse { return println(log) }
    println("Player $winner won!")
}

private fun selectPlayer(playerCount: Int): Outcome<Int, SelectPlayerError> {
    blockContext("selectPlayer") {
        return rollDie(playerCount)
            .mapError(SelectPlayerError::fromRollDieError)
            .onSuccess { if (it !in 1..playerCount) return Failure(SelectPlayerError.IncorrectDieSides) }
    }
}

private enum class SelectPlayerError {
    IncorrectDieSides,
    DieWentMissing,
    EveryoneDied;

    companion object {
        fun fromRollDieError(it: RollDieError) = when (it) {
            RollDieError.DieTeleportedAway, RollDieError.DogAteDie -> DieWentMissing
            RollDieError.DieTurnedIntoBlackHole -> EveryoneDied
        }
    }
}

private fun rollDie(sides: Int): Outcome<Int, RollDieError> {
    blockContext("rollDie") {
        val roll = (-1..sides + 2).random()
        if (roll <= -1) return Failure(RollDieError.DieTeleportedAway)
        if (roll <= 0) return Failure(RollDieError.DogAteDie)
        if (roll > sides + 1) return Failure(RollDieError.DieTurnedIntoBlackHole)
        return Success(roll) // A 6-sided die could return a 7
    }
}

private enum class RollDieError {
    DieTeleportedAway,
    DogAteDie,
    DieTurnedIntoBlackHole,
}