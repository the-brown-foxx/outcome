package com.thebrownfoxx.outcome

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.cancellation.CancellationException

public fun <T> T.asSuccess(): Success<T> = Success(this)

public fun <T> T.asFailure(stackTrace: StackTrace = StackTrace()): Failure<T> =
    Failure(error = this, stackTrace = stackTrace)

@OptIn(ExperimentalContracts::class)
public inline fun <T> runFailing(
    stackTrace: StackTrace = StackTrace(),
    function: () -> T,
): Outcome<T, Exception> {
    contract {
        callsInPlace(function, InvocationKind.AT_MOST_ONCE)
    }

    return try {
        Success(function())
    } catch (cancellationException: CancellationException) {
        throw cancellationException
    } catch (e: Exception) {
        Failure(e, stackTrace)
    }
}

public typealias UnitOutcome<E> = Outcome<Unit, E>

public val UnitSuccess: Success<Unit> = Success(Unit)