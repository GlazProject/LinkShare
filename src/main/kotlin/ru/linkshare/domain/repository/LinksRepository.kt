package ru.linkshare.domain.repository

import ru.linkshare.domain.models.LinkInfo
import ru.linkshare.domain.models.LinkTitle
import ru.linkshare.domain.models.UID

interface LinksRepository {
    suspend fun getLinks(userId: UID): List<LinkInfo>
    suspend fun addLink(userId: UID, info: LinkInfo)
    suspend fun deleteLink(userId: UID, title: LinkTitle)
}