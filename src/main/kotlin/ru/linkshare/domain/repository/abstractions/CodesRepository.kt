package ru.linkshare.domain.repository.abstractions

import ru.linkshare.domain.models.UID

interface CodesRepository {
    suspend fun setCodeOrException(userId: UID, code: String, ttlSecs: Int)
    suspend fun getUserAndDeleteCode(code: String): UID?
}