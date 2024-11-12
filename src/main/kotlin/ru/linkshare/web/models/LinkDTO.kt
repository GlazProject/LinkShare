package ru.linkshare.web.models

import kotlinx.serialization.Serializable

@Serializable
data class LinkDTO(
    val title: String,
    val url: String
)