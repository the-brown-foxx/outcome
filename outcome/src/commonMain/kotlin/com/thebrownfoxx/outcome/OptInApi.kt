package com.thebrownfoxx.outcome

@RequiresOptIn(
    message = "This API does not provide your Failure a context. Please provide a BlockContext.",
    level = RequiresOptIn.Level.WARNING,
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CONSTRUCTOR)
public annotation class ContextlessFailureApi

@RequiresOptIn(
    message = "This API throws ",
    level = RequiresOptIn.Level.WARNING,
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CONSTRUCTOR)
public annotation class ThrowingApi