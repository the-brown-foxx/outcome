package com.thebrownfoxx.outcome

import kotlin.jvm.JvmInline

public sealed interface Outcome<out T, out E>

@JvmInline
public value class Success<out T>(public val value: T) : Outcome<T, Nothing> {
    override fun toString(): String = "Success($value)"
}

public class Failure<out E> private constructor(
    public val error: E,
    private val stackTrace: StackTrace?,
    private val cause: Failure<*>? = null,
) : Outcome<Nothing, E> {
    public constructor(error: E, stackTrace: StackTrace = StackTrace()) :
            this(error = error, stackTrace = stackTrace, cause = null)

    public fun <RE> RE.asMappedFailure(stackTrace: StackTrace = StackTrace()): Failure<RE> {
        return Failure(error = this, stackTrace = stackTrace, cause = this@Failure)
    }

    private val errorAtContext = if (stackTrace != null) "$error ${stackTrace.label}" else "$error"

    override fun toString(): String = "Failure($errorAtContext)"

    public val log: String
        get() = buildString {
            appendLine(this@Failure)
            var currentFailure: Failure<*> = this@Failure
            while (true) {
                val cause = currentFailure.cause ?: break
                appendLine("    <- ${cause.errorAtContext}")
                currentFailure = cause
            }
        }
}