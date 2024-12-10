package com.thebrownfoxx.outcome.map

import com.thebrownfoxx.outcome.Failure
import com.thebrownfoxx.outcome.Outcome
import com.thebrownfoxx.outcome.StackTrace
import com.thebrownfoxx.outcome.Success

public inline fun <RT, RE, T, EI, EO> Outcome<Outcome<T, EI>, EO>.flatMap(
    stackTrace: StackTrace = StackTrace(),
    onSuccess: (T) -> RT,
    onInnerFailure: (EI) -> RE,
    onOuterFailure: (EO) -> RE,
): Outcome<RT, RE> {
    return when (this) {
        is Success -> when (value) {
            is Success -> Success(onSuccess(value.value))
            is Failure -> FailureMapScope(value).Failure(onInnerFailure(value.error), stackTrace)
        }

        is Failure -> FailureMapScope(this).Failure(onOuterFailure(error), stackTrace)
    }
}

public inline fun <RT, RE, T, EI, EO> Outcome<Outcome<T, EI>, EO>.flatMap(
    stackTrace: StackTrace = StackTrace(),
    onSuccess: (T) -> RT,
    onFailure: (FlatMapFailure<EI, EO>) -> RE,
): Outcome<RT, RE> {
    return when (this) {
        is Success -> when (value) {
            is Success -> Success(onSuccess(value.value))
            is Failure -> FailureMapScope(value).Failure(onFailure(FlatMapFailure.Inner(value.error)), stackTrace)
        }

        is Failure -> FailureMapScope(this).Failure(onFailure(FlatMapFailure.Outer(error)), stackTrace)
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
    stackTrace: StackTrace = StackTrace(),
    onInnerFailure: (EI) -> RE,
    onOuterFailure: (EO) -> RE,
): Outcome<T, RE> {
    return when (this) {
        is Success -> when (value) {
            is Success -> value
            is Failure -> FailureMapScope(value).Failure(onInnerFailure(value.error), stackTrace)
        }

        is Failure -> FailureMapScope(this).Failure(onOuterFailure(error), stackTrace)
    }
}

public inline fun <RE, T, EI, EO> Outcome<Outcome<T, EI>, EO>.flatMapError(
    stackTrace: StackTrace = StackTrace(),
    onFailure: (FlatMapFailure<EI, EO>) -> RE,
): Outcome<T, RE> {
    return when (this) {
        is Success -> when (value) {
            is Success -> value
            is Failure -> FailureMapScope(value).Failure(onFailure(FlatMapFailure.Inner(value.error)), stackTrace)
        }

        is Failure -> FailureMapScope(this).Failure(onFailure(FlatMapFailure.Outer(error)), stackTrace)
    }
}

public inline fun <T, EI, EO> Outcome<Outcome<T, EI>, EO>.onInnerSuccess(
    function: (T) -> Unit,
): Outcome<Outcome<T, EI>, EO> {
    if (this is Success && value is Success) function(value.value)
    return this
}

public inline fun <T, EI, EO> Outcome<Outcome<T, EI>, EO>.onInnerFailure(
    function: FailureMapScope.(EI) -> Unit,
): Outcome<Outcome<T, EI>, EO> {
    if (this is Success && value is Failure) FailureMapScope(value).function(value.error)
    return this
}

public inline fun <T, EI, EO> Outcome<Outcome<T, EI>, EO>.onOuterFailure(
    function: FailureMapScope.(EO) -> Unit,
): Outcome<Outcome<T, EI>, EO> {
    if (this is Failure) FailureMapScope(this).function(error)
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