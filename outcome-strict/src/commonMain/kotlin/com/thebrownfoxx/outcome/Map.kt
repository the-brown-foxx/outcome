package com.thebrownfoxx.outcome

public inline fun <R, T, E> Outcome<T, E>.fold(
    onSuccess: (T) -> R,
    onFailure: Failure<E>.() -> R,
): R {
    return when (this) {
        is Success -> onSuccess(value)
        is Failure -> onFailure()
    }
}

public inline fun <R, T: R, E> Outcome<T, E>.getOrElse(
    onFailure: Failure<E>.() -> R,
): R {
    return when (this) {
        is Success -> value
        is Failure -> onFailure(this)
    }
}

public fun <T, E> Outcome<T, E>.getOrNull(): T? {
    return getOrElse { null }
}

public fun <T, E> Outcome<T, E>.getOrThrow(): T {
    return getOrElse {
        throw IllegalArgumentException("Cannot get value from $log")
    }
}

public inline fun <RT, RE, T, E> Outcome<T, E>.map(
    context: BlockContext,
    onSuccess: (T) -> RT,
    onFailure: (E) -> RE,
): Outcome<RT, RE> {
    return when (this) {
        is Success -> Success(onSuccess(value))
        is Failure -> mapError(onFailure(error), context)
    }
}

public inline fun <R, T, E> Outcome<T, E>.map(transform: (T) -> R): Outcome<R, E> {
    return when (this) {
        is Success -> Success(transform(value))
        is Failure -> this
    }
}

public inline fun <RE, T, E> Outcome<T, E>.mapError(
    context: BlockContext,
    onFailure: (E) -> RE,
): Outcome<T, RE> {
    return when (this) {
        is Success -> this
        is Failure -> mapError(onFailure(error), context)
    }
}

public inline fun <T, E> Outcome<T, E>.onSuccess(
    function: (T) -> Unit,
): Outcome<T, E> {
    if (this is Success) function(value)
    return this
}

public inline fun <T, E> Outcome<T, E>.onFailure(
    function: Failure<E>.() -> Unit,
): Outcome<T, E> {
    if (this is Failure) function()
    return this
}