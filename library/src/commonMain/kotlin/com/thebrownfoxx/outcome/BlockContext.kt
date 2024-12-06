package com.thebrownfoxx.outcome

// TODO: MAKE BLOCK CONTEXT OPTIONAL!
//  Maybe make BlockContext like its own separate feature? like all the normal map and whatever
//  don't support BlockContext. Then, BlockContext just has its own functions that supports it.
//  Try starting a new project where you can test Outcome in isolation

inline fun <T> blockContext(label: String, block: BlockContext.() -> T): T {
    return with(BlockContext(label)) { block() }
}

inline fun <T> Any.blockContext(label: String, block: BlockContext.() -> T): T {
    return with(BlockContext("${this::class.simpleName}: $label")) { block() }
}

data class BlockContext(val label: String) {
    val context get() = this // Handy when you're inside blockContext's block

    fun <E> Failure(error: E) = Failure(
        error = error,
        context = this,
    )

    fun <E> E.asFailure() = Failure(this)

    fun <E> Failure<*>.mapError(error: E): Failure<E> {
        return Failure(error, this@BlockContext, this)
    }

    inline fun <T> runFailing(function: () -> T): Outcome<T, Exception> {
        return try {
            Success(function())
        } catch (e: Exception) {
            Failure(e, this)
        }
    }

    inline fun <RT, RE, T, E> Outcome<T, E>.map(
        onSuccess: (T) -> RT,
        onFailure: (E) -> RE,
    ): Outcome<RT, RE> {
        return when (this) {
            is Success -> Success(onSuccess(value))
            is Failure -> mapError(onFailure(error), this@BlockContext)
        }
    }

    inline fun <RE, T, E> Outcome<T, E>.mapError(onFailure: (E) -> RE): Outcome<T, RE> {
        return when (this) {
            is Success -> this
            is Failure -> mapError(onFailure(error), this@BlockContext)
        }
    }

    inline fun <RT, RE, T, EI, EO> Outcome<Outcome<T, EI>, EO>.flatMap(
        onSuccess: (T) -> RT,
        onInnerFailure: (EI) -> RE,
        onOuterFailure: (EO) -> RE,
    ): Outcome<RT, RE> {
        return when (this) {
            is Success -> when (value) {
                is Success -> Success(onSuccess(value.value))
                is Failure -> value.mapError(onInnerFailure(value.error), this@BlockContext)
            }
            is Failure -> mapError(onOuterFailure(error), context)
        }
    }

    inline fun <RT, RE, T, EI, EO> Outcome<Outcome<T, EI>, EO>.flatMap(
        onSuccess: (T) -> RT,
        onFailure: (FlatMapFailure<EI, EO>) -> RE
    ): Outcome<RT, RE> {
        return when (this) {
            is Success -> when (value) {
                is Success -> Success(onSuccess(value.value))
                is Failure -> value.mapError(
                    error = onFailure(FlatMapFailure.Inner(value.error)),
                    context = this@BlockContext,
                )
            }
            is Failure -> mapError(onFailure(FlatMapFailure.Outer(error)), context)
        }
    }

    inline fun <RE, T, EI, EO> Outcome<Outcome<T, EI>, EO>.flatMapError(
        onInnerFailure: (EI) -> RE,
        onOuterFailure: (EO) -> RE,
    ): Outcome<T, RE> {
        return when (this) {
            is Success -> when (value) {
                is Success -> value
                is Failure -> value.mapError(onInnerFailure(value.error), this@BlockContext)
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
}