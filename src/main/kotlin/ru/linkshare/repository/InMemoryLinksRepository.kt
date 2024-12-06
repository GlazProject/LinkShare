package ru.linkshare.repository

import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import ru.linkshare.domain.models.LinkInfo
import ru.linkshare.domain.models.LinkTitle
import ru.linkshare.domain.models.UID
import ru.linkshare.domain.repository.LinksRepository

// TODO сделать нормальную БД
private data class LinkRecord(val data: LinkInfo, val expirationTime: Long)

class InMemoryLinksRepository : LinksRepository, Closeable {
    private val links = mutableMapOf<UID, MutableList<LinkRecord>>()
    private val scope = CoroutineScope(Dispatchers.IO)
    private val ttl: Int = 24*60*60*1000

    init {
        scope.launch{ cleaner() }
    }

    override fun close() {
        scope.cancel()
    }

    override suspend fun getLinks(userId: UID): List<LinkInfo> {
        return links[userId]?.map{it.data} ?: emptyList()
    }

    override suspend fun addLink(userId: UID, info: LinkInfo) {
        val record = LinkRecord(info, System.currentTimeMillis() + ttl)
        links.computeIfAbsent(userId) { mutableListOf() }.add(record)
    }

    override suspend fun deleteLink(userId: UID, title: LinkTitle) {
        links[userId]?.removeIf { it.data.title == title }
    }

    private suspend fun cleaner() {
        while (true) {
            val now = System.currentTimeMillis()
            links.map { it.value }
                .forEach { userLinks -> userLinks
                    .filter { it.expirationTime < now }
                    .forEach{ userLinks.remove(it) }}

            links.filter { it.value.isEmpty() }
                .forEach { links.remove(it.key) }

            delay(1000)
        }
    }
}