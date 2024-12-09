package com.thebrownfoxx.outcome

public fun <T> T.asSuccess(): Success<T> = Success(this)

public fun <T> T.asFailure(context: StackTrace = StackTrace()): Failure<T> =
    Failure(error = this, context = context)

public inline fun <T> runFailing(
    context: StackTrace = StackTrace(),
    function: () -> T,
): Outcome<T, Exception> {
    return try {
        Success(function())
    } catch (e: Exception) {
        Failure(e, context)
    }
}

public typealias UnitOutcome<E> = Outcome<Unit, E>

public val UnitSuccess: Success<Unit> = Success(Unit)