package com.example.services.models

import io.ktor.http.*

data class LinkDto(
    val url: String,
    val title: String
)