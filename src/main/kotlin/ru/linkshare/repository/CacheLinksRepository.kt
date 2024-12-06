package ru.linkshare.repository

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import ru.linkshare.domain.models.LinkInfo
import ru.linkshare.domain.models.LinkTitle
import ru.linkshare.domain.models.UID
import ru.linkshare.domain.repository.LinksRepository
import java.util.concurrent.TimeUnit

class CacheLinksRepository: LinksRepository {
    private val cache: Cache<UID, UserLinkRecord> = CacheBuilder.newBuilder()
        .expireAfterWrite(1, TimeUnit.DAYS)
        .maximumSize(1_000_000)
        .removalListener<UID, UserLinkRecord> {
            println("${it.key} was removed from CacheLinksRepository cause ${it.cause}")
        }
        .build()

    override suspend fun getLinks(userId: UID): List<LinkInfo> =
        cache.getIfPresent(userId)?.links ?: listOf()

    override suspend fun addLink(userId: UID, info: LinkInfo) {
        cache.get(userId, ::loader).links.add(info)
    }

    override suspend fun deleteLink(userId: UID, title: LinkTitle) {
        cache.getIfPresent(userId)?.links?.removeAll { it.title == title }
    }

    private fun loader(): UserLinkRecord = UserLinkRecord(mutableListOf())
}

private data class UserLinkRecord(val links: MutableList<LinkInfo>)