@file:Suppress("DEPRECATION")

package com.thebrownfoxx.outcome

@Deprecated(message = "Use the equivalent in the map package instead.")
public fun <RE, E> Failure<E>.mapError(error: RE, stackTrace: StackTrace = StackTrace()): Failure<RE> {
    return error.asMappedFailure(stackTrace)
}

@Deprecated(
    message = "Use the equivalent in the map package instead.",
    replaceWith = ReplaceWith(
        expression = "this.fold(onSuccess, { error -> onFailure() },)",
        "com.thebrownfoxx.outcome.map.fold",
    ),
)
public inline fun <R, T, E> Outcome<T, E>.fold(
    onSuccess: (T) -> R,
    onFailure: Failure<E>.() -> R,
): R {
    return when (this) {
        is Success -> onSuccess(value)
        is Failure -> onFailure()
    }
}

@Deprecated(
    message = "Use the equivalent in the map package instead.",
    replaceWith = ReplaceWith(
        expression = "this.getOrElse { error -> onFailure() }",
        "com.thebrownfoxx.outcome.map.getOrElse",
    ),
)
public inline fun <R, T: R, E> Outcome<T, E>.getOrElse(
    onFailure: Failure<E>.() -> R,
): R {
    return when (this) {
        is Success -> value
        is Failure -> onFailure(this)
    }
}

@Deprecated(
    message = "Use the equivalent in the map package instead.",
    replaceWith = ReplaceWith(
        expression = "this.getOrNull()",
        "com.thebrownfoxx.outcome.map.getOrNull",
    ),
)
public fun <T, E> Outcome<T, E>.getOrNull(): T? {
    return getOrElse { null }
}

@ThrowingApi
@Deprecated(
    message = "Use the equivalent in the map package instead.",
    replaceWith = ReplaceWith(
        expression = "this.getOrThrow()",
        "com.thebrownfoxx.outcome.map.getOrThrow",
    ),
)
public fun <T, E> Outcome<T, E>.getOrThrow(): T {
    return getOrElse {
        throw IllegalArgumentException("Cannot get value from $log")
    }
}

@Deprecated(
    message = "Use the equivalent in the map package instead.",
    replaceWith = ReplaceWith(
        expression = "this.map(stackTrace, onSuccess, onFailure,)",
        "com.thebrownfoxx.outcome.map.map",
    ),
)
public inline fun <RT, RE, T, E> Outcome<T, E>.map(
    stackTrace: StackTrace = StackTrace(),
    onSuccess: (T) -> RT,
    onFailure: (E) -> RE,
): Outcome<RT, RE> {
    return when (this) {
        is Success -> Success(onSuccess(value))
        is Failure -> mapError(onFailure(error), stackTrace)
    }
}

@Deprecated(
    message = "Use the equivalent in the map package instead.",
    replaceWith = ReplaceWith(
        expression = "this.map(transform)",
        "com.thebrownfoxx.outcome.map.map",
    ),
)
public inline fun <R, T, E> Outcome<T, E>.map(transform: (T) -> R): Outcome<R, E> {
    return when (this) {
        is Success -> Success(transform(value))
        is Failure -> this
    }
}

@Deprecated(
    message = "Use the equivalent in the map package instead.",
    replaceWith = ReplaceWith(
        expression = "this.mapError(stackTrace, onFailure)",
        "com.thebrownfoxx.outcome.map.mapError",
    ),
)
public inline fun <RE, T, E> Outcome<T, E>.mapError(
    stackTrace: StackTrace = StackTrace(),
    onFailure: (E) -> RE,
): Outcome<T, RE> {
    return when (this) {
        is Success -> this
        is Failure -> mapError(onFailure(error), stackTrace)
    }
}

@Deprecated(
    message = "Use the equivalent in the map package instead.",
    replaceWith = ReplaceWith(
        expression = "this.transform(onSuccess, { error -> onFailure() },)",
        "com.thebrownfoxx.outcome.map.transform",
    ),
)
public inline fun <RT, RE, E, T> Outcome<T, E>.transform(
    onSuccess: (T) -> Outcome<RT, RE>,
    onFailure: Failure<E>.() -> Outcome<RT, RE>,
): Outcome<RT, RE> {
    return when (this) {
        is Success -> onSuccess(value)
        is Failure -> onFailure()
    }
}

@Deprecated(
    message = "Use the equivalent in the map package instead.",
    replaceWith = ReplaceWith(
        expression = "this.onSuccess(function)",
        "com.thebrownfoxx.outcome.map.onSuccess",
    ),
)
public inline fun <T, E> Outcome<T, E>.onSuccess(
    function: (T) -> Unit,
): Outcome<T, E> {
    if (this is Success) function(value)
    return this
}

@Deprecated(
    message = "Use the equivalent in the map package instead.",
    replaceWith = ReplaceWith(
        expression = "this.onFailure { error -> function() }",
        "com.thebrownfoxx.outcome.map.onFailure",
    ),
)
public inline fun <T, E> Outcome<T, E>.onFailure(
    function: Failure<E>.() -> Unit,
): Outcome<T, E> {
    if (this is Failure) function()
    return this
}