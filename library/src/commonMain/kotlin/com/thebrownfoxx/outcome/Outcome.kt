package com.thebrownfoxx.outcome

sealed interface Outcome<out T, out E>

data class Success<out T>(val value: T) : Outcome<T, Nothing>

data class Failure<out E>(
    val error: E,
    val context: BlockContext,
    val cause: Failure<*>? = null,
) : Outcome<Nothing, E> {
    fun <E> mapError(error: E, context: BlockContext): Failure<E> {
        return Failure(error, context, this)
    }

    fun <E> E.errorMapped(context: BlockContext): Failure<E> {
        return Failure(this, context, this@Failure)
    }

    private val errorAtContext = "$error at $context"

    override fun toString() = "Failure($errorAtContext)"

    fun toStringWithHistory(): String = buildString {
        appendLine("Failure: $errorAtContext")
        var currentFailure: Failure<*> = this@Failure
        while (true) {
            val cause = currentFailure.cause ?: break
            appendLine("    Caused by: ${cause.errorAtContext}")
            currentFailure = cause
        }
    }
}

typealias UnitOutcome<E> = Outcome<Unit, E>

fun unitSuccess() = Success(Unit)

fun <T> T.asSuccess() = Success(this)

fun <E> E.asFailure(context: BlockContext) = Failure(this, context)

data class UnexpectedError(val cause: Any?)

inline fun <T> runFailing(context: BlockContext, function: () -> T): Outcome<T, Exception> {
    return try {
        Success(function())
    } catch (e: Exception) {
        Failure(e, context)
    }
}