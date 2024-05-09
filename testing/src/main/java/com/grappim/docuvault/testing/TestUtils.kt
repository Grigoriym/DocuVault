package com.grappim.docuvault.testing

import java.time.OffsetDateTime
import kotlin.random.Random

val nowDate = OffsetDateTime.now()

fun getRandomLong(): Long = Random.nextLong()

@Suppress("MagicNumber")
fun getRandomString(): String = List(15) { // Generate a list of 10 characters
    Random.nextInt(97, 123) // ASCII range for lowercase letters a-z
        .toChar() // Convert ASCII value to char
}.joinToString("")

val testException = IllegalStateException("error")
