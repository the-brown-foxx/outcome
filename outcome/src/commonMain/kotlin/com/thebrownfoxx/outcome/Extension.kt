package com.thebrownfoxx.outcome

public fun <T> T.asSuccess(): Success<T> = Success(this)

public fun <T> T.asFailure(stackTrace: StackTrace = StackTrace()): Failure<T> =
    Failure(error = this, stackTrace = stackTrace)

public inline fun <T> runFailing(
    stackTrace: StackTrace = StackTrace(),
    function: () -> T,
): Outcome<T, Exception> {
    return try {
        Success(function())
    } catch (e: Exception) {
        Failure(e, stackTrace)
    }
}

public typealias UnitOutcome<E> = Outcome<Unit, E>

public val UnitSuccess: Success<Unit> = Success(Unit)