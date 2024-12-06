package com.thebrownfoxx.outcome

public fun <T> T.asSuccess(): Success<T> = Success(this)

public fun <T> T.asFailure(context: BlockContext? = null): Failure<T> =
    Failure(error = this, context = context)

public inline fun <T> runFailing(
    context: BlockContext? = null,
    function: () -> T,
): Outcome<T, Exception> {
    return try {
        Success(function())
    } catch (e: Exception) {
        Failure(e, context)
    }
}