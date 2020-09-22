package com.tenebraex8.kotlin.annotations.example2

import java.lang.IllegalArgumentException
import java.lang.StringBuilder
import kotlin.reflect.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties


object CSVParser {
    fun serialize(instance: Any, separator: String = ","): String{
        val metaClass = instance::class
        if(!metaClass.hasAnnotation<CSVClass>())
            throw IllegalArgumentException("Cannot serialize classes if they do not have the @CSVClass Annotation!")

        val properties: List<KProperty<*>> = metaClass.memberProperties
            .filter { it.hasAnnotation<CSVProperty>() }
            .filter { it.visibility == KVisibility.PUBLIC }
            .sortedBy { it.findAnnotation<CSVProperty>()!!.order }

        val builder = StringBuilder()
        var first = true
        properties.forEach {
            if(first) first = false
            else builder.append(separator)

            val value = it.getter.call(instance)
            builder.append(value.toString())
        }
        return builder.toString()
    }

    fun <T: Any> deserialize(csv: String, targetClass: KClass<T>, separator: String = ","): T{
        if(!targetClass.hasAnnotation<CSVClass>())
            throw IllegalArgumentException("Cannot deserialize into classes if they do not have the @CSVClass Annotation!")

        val properties: List<KMutableProperty<*>> = targetClass.memberProperties
            .filter { it.hasAnnotation<CSVProperty>() }
            .filter { it.visibility == KVisibility.PUBLIC }
            .filterIsInstance<KMutableProperty<*>>()
            .sortedBy { it.findAnnotation<CSVProperty>()!!.order }

        val csvParts = csv.split(separator)
        if(csvParts.count() != properties.count())
            throw IllegalArgumentException("The count of the csv-fields has to match the count of the @CSVProperty-Properties in the class!")

        val metaConstructor = targetClass.constructors.find { it.parameters.count() == 0 }
            ?: throw IllegalArgumentException("Target class needs an empty default-constructor!")

        val instance = metaConstructor.call()

        for(idx in 0 until csvParts.count()){
            val targetType = properties[idx].returnType
            val value = csvParts[idx]
            val injectionValue = when {
                targetType == typeOf<String>() -> value
                targetType == typeOf<Int>() -> value.toInt()
                targetType == typeOf<Long>() -> value.toLong()
                targetType == typeOf<Boolean>() -> value.toBoolean()
                targetType == typeOf<Double>() -> value.toDouble()
                else -> throw IllegalArgumentException("Properties of type $targetType are not supported at the moment!")
            }
            properties[idx].setter.call(instance, injectionValue)
        }
        return instance
    }
}