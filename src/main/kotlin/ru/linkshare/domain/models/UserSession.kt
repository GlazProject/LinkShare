package ru.linkshare.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val userId: UID
)