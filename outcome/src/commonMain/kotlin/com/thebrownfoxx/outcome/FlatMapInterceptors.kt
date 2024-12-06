@file:Suppress("UnusedReceiverParameter", "UNUSED_PARAMETER")

package com.thebrownfoxx.outcome

// These functions cause intentional overload ambiguity so you don't accidentally use
// Outcome<T, E>.onAction on a nested outcome

public inline fun <T, EI, EO> Outcome<Outcome<T, EI>, EO>.onSuccess(
    function: (T) -> Unit,
): Nothing {
    error("Use onInnerSuccess instead")
}

public inline fun <T, EI, EO> Outcome<Outcome<T, EI>, EO>.onFailure(
    function: (FlatMapFailure<EI, EO>) -> Unit,
): Outcome<Outcome<T, EI>, EO> {
    error("Use onInnerFailure, onOuterFailure, or onAnyFailure instead")
}