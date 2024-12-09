package com.thebrownfoxx.outcome

private fun main() {
    println(println(do3().log))
}

private fun do3(): Failure<Error3> {
    return do2().mapError(Error3)
}

private fun do2(): Failure<Error2> {
    return do1().mapError(Error2)
}

private fun do1(): Failure<Error1> {
    return Failure(Error1)
}

private data object Error3
private data object Error2
private data object Error1
