package ru.linkshare.domain.utils

import kotlin.random.Random


// TODO убрать длину
class CodesGenerator {
    fun getSecretCode(length: Int): String{
        return Random.nextInt(99999).toString()
    }
}