@file:Suppress("DEPRECATION")

package com.thebrownfoxx.outcome

@Deprecated(
    message = "Use the equivalent in the map package instead.",
    replaceWith = ReplaceWith(
        expression = "this.flatMap(stackTrace, onSuccess, onInnerFailure, onOuterFailure,)",
        "com.thebrownfoxx.outcome.map.flatMap",
    ),
)
public inline fun <RT, RE, T, EI, EO> Outcome<Outcome<T, EI>, EO>.flatMap(
    stackTrace: StackTrace = StackTrace(),
    onSuccess: (T) -> RT,
    onInnerFailure: (EI) -> RE,
    onOuterFailure: (EO) -> RE,
): Outcome<RT, RE> {
    return when (this) {
        is Success -> when (value) {
            is Success -> Success(onSuccess(value.value))
            is Failure -> value.mapError(onInnerFailure(value.error), stackTrace)
        }
        is Failure -> mapError(onOuterFailure(error), stackTrace)
    }
}

@Deprecated(
    message = "Use the equivalent in the map package instead.",
    replaceWith = ReplaceWith(
        expression = "this.flatMap(stackTrace, onSuccess, onFailure,)",
        "com.thebrownfoxx.outcome.map.flatMap",
    ),
)
public inline fun <RT, RE, T, EI, EO> Outcome<Outcome<T, EI>, EO>.flatMap(
    stackTrace: StackTrace = StackTrace(),
    onSuccess: (T) -> RT,
    onFailure: (FlatMapFailure<EI, EO>) -> RE,
): Outcome<RT, RE> {
    return when (this) {
        is Success -> when (value) {
            is Success -> Success(onSuccess(value.value))
            is Failure -> value.mapError(onFailure(FlatMapFailure.Inner(value.error)), stackTrace)
        }
        is Failure -> mapError(onFailure(FlatMapFailure.Outer(error)), stackTrace)
    }
}

@Deprecated(
    message = "Use the equivalent in the map package instead.",
    replaceWith = ReplaceWith(
        expression = "this.flatMap(onSuccess)",
        "com.thebrownfoxx.outcome.map.flatMap",
    ),
)
public inline fun <RT, T, E> Outcome<Outcome<T, E>, E>.flatMap(
    onSuccess: (T) -> RT,
): Outcome<RT, E> {
    return when (this) {
        is Success -> when (value) {
            is Success -> Success(onSuccess(value.value))
            is Failure -> value
        }
        is Failure -> this
    }
}

@Deprecated(
    message = "Use the equivalent in the map package instead.",
    replaceWith = ReplaceWith(
        expression = "this.flatMapError(stackTrace, onInnerFailure, onOuterFailure,)",
        "com.thebrownfoxx.outcome.map.flatMapError",
    ),
)
public inline fun <RE, T, EI, EO> Outcome<Outcome<T, EI>, EO>.flatMapError(
    stackTrace: StackTrace = StackTrace(),
    onInnerFailure: (EI) -> RE,
    onOuterFailure: (EO) -> RE,
): Outcome<T, RE> {
    return when (this) {
        is Success -> when (value) {
            is Success -> value
            is Failure -> value.mapError(onInnerFailure(value.error), stackTrace)
        }
        is Failure -> mapError(onOuterFailure(error), stackTrace)
    }
}

@Deprecated(
    message = "Use the equivalent in the map package instead.",
    replaceWith = ReplaceWith(
        expression = "this.flatMapError(stackTrace, onFailure)",
        "com.thebrownfoxx.outcome.map.flatMapError",
    ),
)
public inline fun <RE, T, EI, EO> Outcome<Outcome<T, EI>, EO>.flatMapError(
    stackTrace: StackTrace = StackTrace(),
    onFailure: (FlatMapFailure<EI, EO>) -> RE,
): Outcome<T, RE> {
    return when (this) {
        is Success -> when (value) {
            is Success -> value
            is Failure -> value.mapError(onFailure(FlatMapFailure.Inner(value.error)), stackTrace)
        }
        is Failure -> mapError(onFailure(FlatMapFailure.Outer(error)), stackTrace)
    }
}

@Deprecated(
    message = "Use the equivalent in the map package instead.",
    replaceWith = ReplaceWith(
        expression = "this.onInnerSuccess(function)",
        "com.thebrownfoxx.outcome.map.onInnerSuccess",
    ),
)
public inline fun <T, EI, EO> Outcome<Outcome<T, EI>, EO>.onInnerSuccess(
    function: (T) -> Unit,
): Outcome<Outcome<T, EI>, EO> {
    if (this is Success && value is Success) function(value.value)
    return this
}

@Deprecated(
    message = "Use the equivalent in the map package instead.",
    replaceWith = ReplaceWith(
        expression = "this.onInnerFailure { error -> function() }",
        "com.thebrownfoxx.outcome.map.onInnerFailure",
    ),
)
public inline fun <T, EI, EO> Outcome<Outcome<T, EI>, EO>.onInnerFailure(
    function: Failure<EI>.() -> Unit,
): Outcome<Outcome<T, EI>, EO> {
    if (this is Success && value is Failure) value.function()
    return this
}

@Deprecated(
    message = "Use the equivalent in the map package instead.",
    replaceWith = ReplaceWith(
        expression = "this.onOuterFailure { error -> function() }",
        "com.thebrownfoxx.outcome.map.onOuterFailure",
    ),
)
public inline fun <T, EI, EO> Outcome<Outcome<T, EI>, EO>.onOuterFailure(
    function: Failure<EO>.() -> Unit,
): Outcome<Outcome<T, EI>, EO> {
    if (this is Failure) function()
    return this
}

@Deprecated(
    message = "Use the equivalent in the map package instead.",
    replaceWith = ReplaceWith(
        expression = "this.onAnyFailure(function)",
        "com.thebrownfoxx.outcome.map.onAnyFailure",
    ),
)
public inline fun <T, EI, EO> Outcome<Outcome<T, EI>, EO>.onAnyFailure(
    function: (FlatMapFailure<EI, EO>) -> Unit,
): Outcome<Outcome<T, EI>, EO> {
    if (this is Success && value is Failure) function(FlatMapFailure.Inner(value.error))
    if (this is Failure) function(FlatMapFailure.Outer(error))
    return this
}

public sealed interface FlatMapFailure<out EI, out EO> {
    public data class Inner<out EI>(val error: EI) : FlatMapFailure<EI, Nothing>
    public data class Outer<out EO>(val error: EO) : FlatMapFailure<Nothing, EO>
}