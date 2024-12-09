package com.thebrownfoxx.outcome

public inline fun <RT, RE, T, EI, EO> Outcome<Outcome<T, EI>, EO>.flatMap(
    context: StackTrace = StackTrace(),
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

public inline fun <RT, RE, T, EI, EO> Outcome<Outcome<T, EI>, EO>.flatMap(
    context: StackTrace = StackTrace(),
    onSuccess: (T) -> RT,
    onFailure: (FlatMapFailure<EI, EO>) -> RE,
): Outcome<RT, RE> {
    return when (this) {
        is Success -> when (value) {
            is Success -> Success(onSuccess(value.value))
            is Failure -> value.mapError(onFailure(FlatMapFailure.Inner(value.error)), context)
        }
        is Failure -> mapError(onFailure(FlatMapFailure.Outer(error)), context)
    }
}

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

public inline fun <RE, T, EI, EO> Outcome<Outcome<T, EI>, EO>.flatMapError(
    context: StackTrace = StackTrace(),
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

public inline fun <RE, T, EI, EO> Outcome<Outcome<T, EI>, EO>.flatMapError(
    context: StackTrace = StackTrace(),
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

public inline fun <T, EI, EO> Outcome<Outcome<T, EI>, EO>.onInnerSuccess(
    function: (T) -> Unit,
): Outcome<Outcome<T, EI>, EO> {
    if (this is Success && value is Success) function(value.value)
    return this
}

public inline fun <T, EI, EO> Outcome<Outcome<T, EI>, EO>.onInnerFailure(
    function: Failure<EI>.() -> Unit,
): Outcome<Outcome<T, EI>, EO> {
    if (this is Success && value is Failure) value.function()
    return this
}

public inline fun <T, EI, EO> Outcome<Outcome<T, EI>, EO>.onOuterFailure(
    function: Failure<EO>.() -> Unit,
): Outcome<Outcome<T, EI>, EO> {
    if (this is Failure) function()
    return this
}

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