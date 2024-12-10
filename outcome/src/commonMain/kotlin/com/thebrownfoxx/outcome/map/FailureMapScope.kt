package com.thebrownfoxx.outcome.map

import com.thebrownfoxx.outcome.Failure
import com.thebrownfoxx.outcome.StackTrace

public class FailureMapScope
@PublishedApi internal constructor(private val failure: Failure<*>) {
    public val log: String = failure.log

    public fun <E> Failure(error: E, stackTrace: StackTrace = StackTrace()): Failure<E> =
        with(failure) { error.asMappedFailure(stackTrace) }

    public fun <E> mapError(error: E, stackTrace: StackTrace = StackTrace()): Failure<E> =
        Failure(error, stackTrace)
}