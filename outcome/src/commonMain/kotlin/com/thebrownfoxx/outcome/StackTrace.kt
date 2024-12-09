package com.thebrownfoxx.outcome

import kotlin.jvm.JvmInline

@JvmInline
public value class StackTrace(
    public val label: String = Throwable().stackTraceToString().split("\n")[3].trim(),
)