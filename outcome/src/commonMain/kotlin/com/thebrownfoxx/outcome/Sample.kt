package com.thebrownfoxx.outcome

import com.thebrownfoxx.outcome.map.getOrElse
import com.thebrownfoxx.outcome.map.map
import com.thebrownfoxx.outcome.map.onFailure
import com.thebrownfoxx.outcome.map.onSuccess

private fun main() {
    flipCoin()
        .onSuccess { println("Successfully flipped ${it.name}") }
        .onFailure { println("The coin broke") }

    val player = flipCoin().map { face ->
        when (face) {
            CoinFace.Heads -> Player.X
            CoinFace.Tails -> Player.O
        }
    }.getOrElse { return println("The coin broke :/") }
    println("Player ${player.name} won")
}

private enum class Player {
    X,
    O,
}

private fun flipCoin(): Outcome<CoinFace, CoinBrokeError> {
    val face = (1..3).random()
    return when (face) {
        1 -> Success(CoinFace.Heads)
        2 -> Success(CoinFace.Tails)
        else -> Failure(CoinBrokeError)
    }
}

private enum class CoinFace {
    Heads,
    Tails,
}

private data object CoinBrokeError