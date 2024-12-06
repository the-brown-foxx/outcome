package com.thebrownfoxx.outcome

inline fun <RT, RE, T, EI, EO> Outcome<Outcome<T, EI>, EO>.flatMap(
    context: BlockContext,
    onSuccess: (T) -> RT,
    onInnerFailure: (EI) -> RE,
    onOuterFailure: (EO) -> RE,
): Outcome<RT, RE> {
    return when (this) {
        is Success -> when (value) {
            is Success -> Success(onSuccess(value.value))
            is Failure -> value.mapError(onInnerFailure(value.error), context)
        }
        is Failure -> mapError(onOuterFailure(error), context)
    }
}

inline fun <RT, RE, T, EI, EO> Outcome<Outcome<T, EI>, EO>.flatMap(
    context: BlockContext,
    onSuccess: (T) -> RT,
    onFailure: (FlatMapFailure<EI, EO>) -> RE
): Outcome<RT, RE> {
    return when (this) {
        is Success -> when (value) {
            is Success -> Success(onSuccess(value.value))
            is Failure -> value.mapError(onFailure(FlatMapFailure.Inner(value.error)), context)
        }
        is Failure -> mapError(onFailure(FlatMapFailure.Outer(error)), context)
    }
}

inline fun <RE, T, EI, EO> Outcome<Outcome<T, EI>, EO>.flatMapError(
    context: BlockContext,
    onInnerFailure: (EI) -> RE,
    onOuterFailure: (EO) -> RE,
): Outcome<T, RE> {
    return when (this) {
        is Success -> when (value) {
            is Success -> value
            is Failure -> value.mapError(onInnerFailure(value.error), context)
        }
        is Failure -> mapError(onOuterFailure(error), context)
    }
}

inline fun <RE, T, EI, EO> Outcome<Outcome<T, EI>, EO>.flatMapError(
    context: BlockContext,
    onFailure: (FlatMapFailure<EI, EO>) -> RE,
): Outcome<T, RE> {
    return when (this) {
        is Success -> when (value) {
            is Success -> value
            is Failure -> value.mapError(onFailure(FlatMapFailure.Inner(value.error)), context)
        }
        is Failure -> mapError(onFailure(FlatMapFailure.Outer(error)), context)
    }
}

inline fun <T, EI, EO> Outcome<Outcome<T, EI>, EO>.onInnerSuccess(
    function: (T) -> Unit,
): Outcome<Outcome<T, EI>, EO> {
    if (this is Success && value is Success) function(value.value)
    return this
}

@Deprecated(
    message = "Use onInnerFailure instead to get access to the whole Failure object and its history",
    replaceWith = ReplaceWith(
        expression = "this.onInnerFailure(function)",
        "com.thebrownfoxx.outcome.onInnerFailure",
    )
)
inline fun <T, EI, EO> Outcome<Outcome<T, EI>, EO>.oldOnInnerFailure(
    function: (EI) -> Unit,
): Outcome<Outcome<T, EI>, EO> {
    if (this is Success && value is Failure) function(value.error)
    return this
}

inline fun <T, EI, EO> Outcome<Outcome<T, EI>, EO>.onInnerFailure(
    function: Failure<EI>.() -> Unit,
): Outcome<Outcome<T, EI>, EO> {
    if (this is Success && value is Failure) value.function()
    return this
}

@Deprecated(
    message = "Use onOuterFailure instead to get access to the whole Failure object and its history",
    replaceWith = ReplaceWith(
        expression = "this.onOuterFailure(function)",
        "com.thebrownfoxx.outcome.onOuterFailure",
    )
)
inline fun <T, EI, EO> Outcome<Outcome<T, EI>, EO>.oldOnOuterFailure(
    function: (EO) -> Unit,
): Outcome<Outcome<T, EI>, EO> {
    if (this is Failure) function(error)
    return this
}

inline fun <T, EI, EO> Outcome<Outcome<T, EI>, EO>.onOuterFailure(
    function: Failure<EO>.() -> Unit,
): Outcome<Outcome<T, EI>, EO> {
    if (this is Failure) function()
    return this
}

inline fun <T, EI, EO> Outcome<Outcome<T, EI>, EO>.onAnyFailure(
    function: (FlatMapFailure<EI, EO>) -> Unit,
): Outcome<Outcome<T, EI>, EO> {
    if (this is Success && value is Failure) function(FlatMapFailure.Inner(value.error))
    if (this is Failure) function(FlatMapFailure.Outer(error))
    return this
}

sealed interface FlatMapFailure<out EI, out EO> {
    data class Inner<out EI>(val error: EI) : FlatMapFailure<EI, Nothing>
    data class Outer<out EO>(val error: EO) : FlatMapFailure<Nothing, EO>
}