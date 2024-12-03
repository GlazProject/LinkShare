package ru.linkshare.routes.api.models

import kotlinx.serialization.Serializable

@Serializable
class DeleteLinksDTO(val titles: List<String>)