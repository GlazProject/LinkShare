package ru.linkshare.domain.repository

import ru.linkshare.domain.models.LinkInfo
import ru.linkshare.domain.models.LinkTitle
import ru.linkshare.domain.models.UID
import ru.linkshare.domain.repository.abstractions.LinksRepository

// TODO сделать нормальную БД
// TODO добавить время жизни записей
class InMemoryLinksRepository : LinksRepository {
    private val links = mutableMapOf<UID, MutableList<LinkInfo>>()

    override suspend fun getLinks(userId: UID): List<LinkInfo> {
        return links[userId] ?: emptyList()
    }

    override suspend fun addLink(userId: UID, info: LinkInfo) {
        links.computeIfAbsent(userId) { mutableListOf() }.add(info)
    }

    override suspend fun deleteLink(userId: UID, title: LinkTitle) {
        links[userId]?.removeIf { it.title == title }
    }
}