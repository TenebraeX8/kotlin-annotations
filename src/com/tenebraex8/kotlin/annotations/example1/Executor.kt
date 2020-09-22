package com.tenebraex8.kotlin.annotations.example1

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation

object Executor {
    fun executeThemAll(instance: Any){
        val metaClass: KClass<out Any> = instance::class
        val metaFunctions: Collection<KFunction<*>> = metaClass.functions
            .filter { it.hasAnnotation<ExecuteMe>()}

        metaFunctions.forEach { it.call(instance) }
    }
}