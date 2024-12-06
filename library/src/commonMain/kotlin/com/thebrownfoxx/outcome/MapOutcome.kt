package com.thebrownfoxx.outcome

@Deprecated(
    message = "Use fold instead to get access to the whole Failure object and its history",
    replaceWith = ReplaceWith(
        expression = "this.fold<R,T,E>(onSuccess, onFailure)",
        "com.thebrownfoxx.outcome.fold",
    )
)
inline fun <R, T, E> Outcome<T, E>.oldFold(onSuccess: (T) -> R, onFailure: (E) -> R): R {
    return when (this) {
        is Success -> onSuccess(value)
        is Failure -> onFailure(error)
    }
}

inline fun <R, T, E> Outcome<T, E>.fold(onSuccess: (T) -> R, onFailure: Failure<E>.() -> R): R {
    return when (this) {
        is Success -> onSuccess(value)
        is Failure -> onFailure()
    }
}

@Deprecated(
    message = "Use getOrElse instead to get access to the whole Failure object and its history",
    replaceWith = ReplaceWith(
        expression = "this.getOrElse<R,T,E>(onFailure)",
        "com.thebrownfoxx.outcome.getOrElse",
    )
)
inline fun <R, T: R, E> Outcome<T, E>.oldGetOrElse(onFailure: (E) -> R): R {
    return when (this) {
        is Success -> value
        is Failure -> onFailure(error)
    }
}

inline fun <R, T: R, E> Outcome<T, E>.getOrElse(onFailure: Failure<E>.() -> R): R {
    return when (this) {
        is Success -> value
        is Failure -> onFailure(this)
    }
}

fun <T, E> Outcome<T, E>.getOrNull(): T? {
    return getOrElse { null }
}

fun <T, E> Outcome<T, E>.getOrThrow(): T {
    return getOrElse {
        throw IllegalArgumentException("Cannot get value from failure outcome ${this.toStringWithHistory()}")
    }
}

inline fun <RT, RE, T, E> Outcome<T, E>.map(
    context: BlockContext,
    onSuccess: (T) -> RT,
    onFailure: (E) -> RE,
): Outcome<RT, RE> {
    return when (this) {
        is Success -> Success(onSuccess(value))
        is Failure -> mapError(onFailure(error), context)
    }
}

inline fun <R, T, E> Outcome<T, E>.map(transform: (T) -> R): Outcome<R, E> {
    return when (this) {
        is Success -> Success(transform(value))
        is Failure -> this
    }
}

inline fun <RE, T, E> Outcome<T, E>.mapError(
    context: BlockContext,
    onFailure: (E) -> RE,
): Outcome<T, RE> {
    return when (this) {
        is Success -> this
        is Failure -> mapError(onFailure(error), context)
    }
}

inline fun <T, E> Outcome<T, E>.onSuccess(function: (T) -> Unit): Outcome<T, E> {
    if (this is Success) function(value)
    return this
}

@Deprecated(
    message = "Use onFailure instead to get access to the whole Failure object and its history",
    replaceWith = ReplaceWith(
        expression = "this.onFailure(function)",
        "com.thebrownfoxx.outcome.onFailure",
    )
)
inline fun <T, E> Outcome<T, E>.oldOnFailure(function: (E) -> Unit): Outcome<T, E> {
    if (this is Failure) function(error)
    return this
}

inline fun <T, E> Outcome<T, E>.onFailure(function: Failure<E>.() -> Unit): Outcome<T, E> {
    if (this is Failure) function()
    return this
}