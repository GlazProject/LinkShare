package ru.linkshare.domain.utils

import kotlin.random.Random

class CodesGenerator {
    fun getSecretCode(): String{
        return Random.nextInt(99999).toString()
    }
}