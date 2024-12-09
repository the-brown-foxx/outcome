package com.thebrownfoxx.outcome

@RequiresOptIn(
    message = "This API throws ",
    level = RequiresOptIn.Level.WARNING,
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CONSTRUCTOR)
public annotation class ThrowingApi