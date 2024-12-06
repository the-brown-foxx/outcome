# Outcome

Outcome is an alternative to Kotlin's Result type that lets you add types to your errors.

```kotlin
fun main() {
    val coinFace = flipCoin().getOrElse {
        when (error) {
            FlipCoinError.CoinBroke -> changeGame()
            FlipCoinError.CoinExploded -> cleanup()
        }
        return
    }
    println("Flipped ${coinFace.name}")
}

fun flipCoin(): Outcome<CoinFace, FlipCoinError>

enum class FlipCoinError {
    CoinBroke,
    CoinExploded,
}
```

> See [Sample.kt](https://github.com/the-brown-foxx/outcome/blob/master/library/src/commonMain/kotlin/com/thebrownfoxx/outcome/Sample.kt) for a more concrete example. 

> See [BlockContextSample.kt](https://github.com/the-brown-foxx/outcome/blob/master/library/src/commonMain/kotlin/com/thebrownfoxx/outcome/BlockContextSample.kt) to learn how to use `BlockContext`, a feature that allows you to log the source of errors.

## Functions
Please refer to [Map.kt](https://github.com/the-brown-foxx/outcome/blob/master/library/src/commonMain/kotlin/com/thebrownfoxx/outcome/Map.kt) and [FlatMap.kt](https://github.com/the-brown-foxx/outcome/blob/master/library/src/commonMain/kotlin/com/thebrownfoxx/outcome/FlatMap.kt) to see the available functions.