package ru.linkshare.web.models

import kotlinx.serialization.Serializable

@Serializable
class DeleteLinksDTO(val titles: List<String>)