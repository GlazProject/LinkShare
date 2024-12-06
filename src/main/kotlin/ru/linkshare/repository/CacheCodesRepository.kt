package ru.linkshare.repository

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import ru.linkshare.domain.models.UID
import ru.linkshare.domain.repository.CodesRepository
import java.util.concurrent.TimeUnit

class CacheCodesRepository: CodesRepository {
    private val cache: Cache<String, UID> = CacheBuilder.newBuilder()
        .maximumSize(100_000)
        .expireAfterWrite(2, TimeUnit.MINUTES)
        .removalListener<String, UID> {
            println("${it.key} was removed from CacheCodesRepository cause ${it.cause}")
        }
        .build()

    override suspend fun setCodeOrException(userId: UID, code: String) {
        cache.put(code, userId)
    }

    override suspend fun getUserAndDeleteCode(code: String): UID? {
        val userId = cache.getIfPresent(code)
        cache.invalidate(code)
        return userId
    }
}