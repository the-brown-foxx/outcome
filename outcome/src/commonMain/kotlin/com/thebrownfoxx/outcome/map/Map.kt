package com.thebrownfoxx.outcome.map

import com.thebrownfoxx.outcome.*

public inline fun <R, T, E> Outcome<T, E>.fold(
    onSuccess: (T) -> R,
    onFailure: FailureMapScope.(E) -> R,
): R {
    return when (this) {
        is Success -> onSuccess(value)
        is Failure -> FailureMapScope(this).onFailure(error)
    }
}

public inline fun <R, T: R, E> Outcome<T, E>.getOrElse(
    onFailure: FailureMapScope.(E) -> R,
): R {
    return when (this) {
        is Success -> value
        is Failure -> FailureMapScope(this).onFailure(error)
    }
}

public fun <T, E> Outcome<T, E>.getOrNull(): T? {
    return getOrElse { null }
}

@ThrowingApi
public fun <T, E> Outcome<T, E>.getOrThrow(): T {
    return getOrElse {
        throw IllegalArgumentException("Cannot get value from $log")
    }
}

public inline fun <RT, RE, T, E> Outcome<T, E>.map(
    stackTrace: StackTrace = StackTrace(),
    onSuccess: (T) -> RT,
    onFailure: (E) -> RE,
): Outcome<RT, RE> {
    return when (this) {
        is Success -> Success(onSuccess(value))
        is Failure -> FailureMapScope(this).Failure(onFailure(error), stackTrace)
    }
}

public inline fun <R, T, E> Outcome<T, E>.map(onSuccess: (T) -> R): Outcome<R, E> {
    return when (this) {
        is Success -> Success(onSuccess(value))
        is Failure -> this
    }
}

public inline fun <RE, T, E> Outcome<T, E>.mapError(
    stackTrace: StackTrace = StackTrace(),
    onFailure: (E) -> RE,
): Outcome<T, RE> {
    return when (this) {
        is Success -> this
        is Failure -> FailureMapScope(this).Failure(onFailure(error), stackTrace)
    }
}

public inline fun <RT, RE, E, T> Outcome<T, E>.transform(
    onSuccess: (T) -> Outcome<RT, RE>,
    onFailure: FailureMapScope.(E) -> Outcome<RT, RE>,
): Outcome<RT, RE> {
    return when (this) {
        is Success -> onSuccess(value)
        is Failure -> FailureMapScope(this).onFailure(error)
    }
}

public inline fun <T, E> Outcome<T, E>.onSuccess(
    function: (T) -> Unit,
): Outcome<T, E> {
    if (this is Success) function(value)
    return this
}

public inline fun <T, E> Outcome<T, E>.onFailure(
    function: FailureMapScope.(E) -> Unit,
): Outcome<T, E> {
    if (this is Failure) FailureMapScope(this).function(error)
    return this
}