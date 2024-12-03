package ru.linkshare.routes.api.user

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import ru.linkshare.domain.exceptions.ApplicationException
import ru.linkshare.domain.models.UserSession
import ru.linkshare.domain.service.UserService
import ru.linkshare.routes.api.models.CodeDTO

class UserController(private val service: UserService) {
    // TODO сделать возможность генерации QR кода. Тo есть перенести код в query параметры
    /**
     * Позволяет получить одноразовый код аутентификации для нового пользователя.
     * Проставляет значение сессии
     */
    suspend fun getNewCode(call: ApplicationCall){
        val userInfo = service.createUserCode()
        call.sessions.clear<UserSession>()
        call.sessions.set(UserSession(userInfo.userId))
        call.respond(CodeDTO(userInfo.code))
    }

    /**
     * Позволяет получить код для текущего пользователя не изменяя сессию.
     * Если пользователь не был зарегистрирован, создаёт новую сессию и новый код.
     */
    suspend fun getCodeForCurrentSession(call: ApplicationCall) {
        val user = call.sessions.get<UserSession>()?.userId
            ?: return getNewCode(call)
        val userInfo = service.createUserCode(user)
        call.respond(CodeDTO(userInfo.code))
    }

    /**
     * В случае корректного кода находит пользователя и проставляет соответсвующую сессию
     */
    suspend fun logIn(call: ApplicationCall){
        val code = call.request.queryParameters["code"]
            ?: throw ApplicationException.forbidden()
        val userId = service.findUserByCode(code)
            ?: throw ApplicationException.forbidden()

        call.sessions.set(UserSession(userId))
        call.respondRedirect(call.request.queryParameters[backlink] ?: "/")
    }

    suspend fun logOut(call: ApplicationCall){
        call.sessions.clear<UserSession>()
        call.respondRedirect(call.request.queryParameters[backlink] ?: "/")
    }

    companion object{
        private const val backlink: String = "backlink";
    }
}