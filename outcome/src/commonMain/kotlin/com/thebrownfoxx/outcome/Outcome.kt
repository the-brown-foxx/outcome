package com.thebrownfoxx.outcome

import kotlin.jvm.JvmInline

public sealed interface Outcome<out T, out E>

@JvmInline
public value class Success<out T>(public val value: T) : Outcome<T, Nothing> {
    override fun toString(): String = "Success($value)"
}

public class Failure<out E> private constructor(
    public val error: E,
    private val context: BlockContext? = null,
    private val cause: Failure<*>? = null,
) : Outcome<Nothing, E> {
    public constructor(error: E, context: BlockContext) :
            this(error = error, context = context, cause = null)

    @ContextlessFailureApi
    public constructor(error: E) :
            this(error = error, context = null, cause = null)

    public fun <RE> RE.asMappedFailure(context: BlockContext): Failure<RE> {
        return Failure(error = this, context = context, cause = this@Failure)
    }

    @ContextlessFailureApi
    public fun <RE> RE.asMappedFailure(): Failure<RE> {
        return Failure(error = this, context = null, cause = this@Failure)
    }

    private val errorAtContext = if (context != null) "$error at ${context.label}" else "$error"

    override fun toString(): String = "Failure($errorAtContext)"

    public val log: String
        get() = buildString {
            append(this@Failure)
            var currentFailure: Failure<*> = this@Failure
            while (true) {
                val cause = currentFailure.cause ?: break
                append(" <- ${cause.errorAtContext}")
                currentFailure = cause
            }
        }
}