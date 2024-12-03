package ru.linkshare.repository

import kotlinx.coroutines.*
import ru.linkshare.domain.models.UID
import ru.linkshare.domain.repository.CodesRepository
import java.io.Closeable

// TODO сделать нормальную БД
private data class CodeRecord(val userId: UID, val expirationTime: Long)
class InMemoryCodesRepository : CodesRepository, Closeable {
    private val codes = mutableMapOf<String, CodeRecord>()
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        scope.launch{ cleaner() }
    }

    override fun close() {
        scope.cancel()
    }

    override suspend fun setCodeOrException(userId: UID, code: String, ttlSecs: Int) {
        codes[code] = CodeRecord(userId, System.currentTimeMillis() + ttlSecs * 1000)
    }

    override suspend fun getUserAndDeleteCode(code: String): UID? {
        val now = System.currentTimeMillis()
        val record = codes[code] ?: return null
        codes.remove(code)
        if (now > record.expirationTime)
            return null

        return record.userId
    }

    private suspend fun cleaner() {
        while (true) {
            val now = System.currentTimeMillis()
            codes.filter { it.value.expirationTime < now }.forEach { codes.remove(it.key) }
            delay(1000)
        }
    }
}