package com.thebrownfoxx.outcome

import kotlin.jvm.JvmInline

public typealias BlockContext = StackTrace

@JvmInline
public value class StackTrace(
    public val label: String = Throwable().stackTraceToString().split("\n")[3].trim(),
)

@Deprecated("No need to wrap in BlockContext")
public inline fun <T> blockContext(
    label: String,
    block: BlockContextScope.() -> T,
): T {
    val context = StackTrace(label)
    return with(BlockContextScope(context)) { block() }
}

@Deprecated("No need to wrap in BlockContext")
public inline fun <T> Any.memberBlockContext(
    label: String,
    block: BlockContextScope.() -> T,
): T {
    val context = StackTrace("${this::class.simpleName}::$label")
    return with(BlockContextScope(context)) { block() }
}

@Deprecated("No need to wrap in BlockContext")
public class BlockContextScope(public val context: StackTrace) {
    public fun <E> Failure(error: E): Failure<E> = Failure(
        error = error,
        context = context,
    )

    public fun <E> E.asFailure(): Failure<E> = Failure(this)

    public inline fun <T> runFailing(
        function: () -> T,
    ): Outcome<T, Exception> {
        return try {
            Success(function())
        } catch (e: Exception) {
            Failure(e, context)
        }
    }

    public fun <RE, E> Failure<E>.mapError(error: RE): Failure<RE> {
        return mapError(error = error, context = context)
    }

    public inline fun <RT, RE, T, E> Outcome<T, E>.map(
        onSuccess: (T) -> RT,
        onFailure: (E) -> RE,
    ): Outcome<RT, RE> {
        return when (this) {
            is Success -> Success(onSuccess(value))
            is Failure -> mapError(onFailure(error), context)
        }
    }

    public inline fun <RE, T, E> Outcome<T, E>.mapError(
        onFailure: (E) -> RE,
    ): Outcome<T, RE> {
        return when (this) {
            is Success -> this
            is Failure -> mapError(onFailure(error), context)
        }
    }

    public inline fun <RT, RE, T, EI, EO> Outcome<Outcome<T, EI>, EO>.flatMap(
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

    public inline fun <RE, T, EI, EO> Outcome<Outcome<T, EI>, EO>.flatMapError(
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
}