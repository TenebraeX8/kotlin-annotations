package com.tenebraex8.kotlin.annotations.example2

fun main() {
    val example = ExampleClass(1, "", "Tenebrae", "tenebrae@somewhere.com")
    val csv = CSVParser.serialize(example)
    println(csv)
    val deserialized = CSVParser.deserialize(csv, ExampleClass::class)
    println("${deserialized.id} - ${deserialized.name} (${deserialized.email})")
}

@CSVClass
class ExampleClass(@CSVProperty(4) var id: Int,
                   var irrelevant: String,
                   @CSVProperty(2) var name: String,
                   @CSVProperty(3) var email: String){
    constructor(): this(0, "", "", "")
}