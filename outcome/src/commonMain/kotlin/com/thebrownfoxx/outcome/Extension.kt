package com.thebrownfoxx.outcome

public fun <T> T.asSuccess(): Success<T> = Success(this)

public fun <T> T.asFailure(context: BlockContext): Failure<T> =
    Failure(error = this, context = context)

@ContextlessFailureApi
public fun <T> T.asFailure(): Failure<T> =
    Failure(error = this)

public inline fun <T> runFailing(
    context: BlockContext,
    function: () -> T,
): Outcome<T, Exception> {
    return try {
        Success(function())
    } catch (e: Exception) {
        Failure(e, context)
    }
}

@ContextlessFailureApi
public inline fun <T> runFailing(
    function: () -> T,
): Outcome<T, Exception> {
    return try {
        Success(function())
    } catch (e: Exception) {
        Failure(e)
    }
}

public typealias UnitOutcome<E> = Outcome<Unit, E>

public val UnitSuccess: Success<Unit> = Success(Unit)