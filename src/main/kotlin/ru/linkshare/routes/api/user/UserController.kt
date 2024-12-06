package ru.linkshare.routes.api.user

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import ru.linkshare.domain.exceptions.ApplicationException
import ru.linkshare.domain.models.UserSession
import ru.linkshare.domain.service.UserService
import ru.linkshare.routes.api.models.CodeDTO

class UserController(private val service: UserService) {
    // TODO сделать возможность генерации QR кода на клиенте. Для этого код в квери параметрах
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
    suspend fun login(call: ApplicationCall){
        val code = call.request.queryParameters["code"]
            ?: throw ApplicationException.forbidden()
        val userId = service.findUserByCode(code)
            ?: throw ApplicationException.forbidden()

        call.sessions.set(UserSession(userId))
        backlinkRedirect(call)
    }

    suspend fun logout(call: ApplicationCall){
        call.sessions.clear<UserSession>()
        call.respondRedirect("/")
    }

    private suspend fun backlinkRedirect(call: ApplicationCall){
        val backlink = call.request.queryParameters[queryBacklink]
        call.respondRedirect( if (backlink.isNullOrEmpty()) "/" else backlink )
    }

    suspend fun getUserId(call: ApplicationCall) {
        val uid = call.sessions.get<UserSession>()?.userId
            ?: throw ApplicationException.authenticationFailed()

        call.respondText(uid.uid.toString().substring(0, 5))
    }

    companion object{
        private const val queryBacklink: String = "backlink"
    }
}