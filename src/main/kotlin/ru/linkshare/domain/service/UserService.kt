package ru.linkshare.domain.service

import ru.linkshare.domain.models.UID
import ru.linkshare.domain.models.UserInfo
import ru.linkshare.domain.repository.abstractions.CodesRepository
import ru.linkshare.domain.utils.CodesGenerator
import java.lang.Exception
import java.util.*

class UserService(
    private val repository: CodesRepository,
    private val generator: CodesGenerator
) {
    /**
     * Создаёт код, для аутентификации с мобильного устройства
     */
    suspend fun createUserCode(): UserInfo {
        val user = UID(UUID.randomUUID())
        return UserInfo(user, generateCode(user, 3))

    }

    /**
     * Ищет пользователя по коду и при успехе возвращает ID
     */
    suspend fun findUserByCode(code: String): UID?{
        return repository.getUserAndDeleteCode(code)
    }

    private suspend fun generateCode(user: UID, attemptLeft: Int): String {
        val code = generator.getSecretCode(4)
        try {
            repository.setCodeOrException(user, code, 120)
        }
        catch(_: Exception) {
            if (attemptLeft == 0)
                throw Exception("Failed to create authentication code")
            generateCode(user, attemptLeft-1)
        }

        return code
    }
}