package ru.linkshare.domain.service

import ru.linkshare.domain.models.UID
import ru.linkshare.domain.models.UserInfo
import ru.linkshare.domain.repository.CodesRepository
import ru.linkshare.domain.utils.CodesGenerator
import java.lang.Exception
import java.util.*

class UserService(
    private val repository: CodesRepository,
    private val generator: CodesGenerator
) {
    /**
     * Создаёт код, для аутентификации с мобильного устройства
     * Если передать uid, то сгенерируется код для текущего пользователя
     */
    suspend fun createUserCode(user: UID? = null): UserInfo {
        val currentUser = user ?: UID(UUID.randomUUID())
        return UserInfo(currentUser, generateCode(currentUser, 3))
    }

    /**
     * Ищет пользователя по коду и при успехе возвращает ID
     */
    suspend fun findUserByCode(code: String): UID?{
        return repository.getUserAndDeleteCode(code)
    }

    private suspend fun generateCode(user: UID, attemptsLeft: Int): String {
        val code = generator.getSecretCode()
        try {
            repository.setCodeOrException(user, code)
        }
        catch(_: Exception) {
            if (attemptsLeft == 0)
                throw Exception("Failed to create authentication code")
            generateCode(user, attemptsLeft-1)
        }

        return code
    }
}