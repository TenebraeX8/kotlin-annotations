package com.tenebraex8.kotlin.annotations.example1

fun main() {
    Executor.executeThemAll(ExecutableClass())
}

class ExecutableClass { //Test class
    @ExecuteMe
    fun run() = println("I was executed!")

    @ExecuteMe
    fun runToo() = println("Me too!")

    fun dontRunMe(){
        throw IllegalStateException()
    }
}