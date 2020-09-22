package com.tenebraex8.kotlin.annotations.example2

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class CSVProperty(val order: Int)