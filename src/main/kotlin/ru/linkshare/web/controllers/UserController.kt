package ru.linkshare.web.controllers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import ru.linkshare.domain.models.UID
import ru.linkshare.domain.models.UserSession
import ru.linkshare.domain.service.LinksService
import ru.linkshare.domain.service.UserService
import ru.linkshare.web.models.CodeDTO
import kotlin.math.log

class UserController(
    private val service: UserService,
    private val linksService: LinksService
) {
    // TODO сделать возможность генерации QR кода. ТО есть перенести код в query параметры
    suspend fun getNewCode(call: ApplicationCall){
        val userInfo = service.createUserCode()
        call.sessions.clear<UserSession>()
        call.sessions.set(UserSession(userInfo.userId))
        call.respond(CodeDTO(userInfo.code))
    }

    suspend fun getCodeForCurrentSession(call: ApplicationCall){
        val user = call.sessions.get<UserSession>()?.userId ?: return getNewCode(call)
        val userInfo = service.createUserCode(user)
        call.respond(CodeDTO(userInfo.code))
    }

    suspend fun logIn(call: ApplicationCall){
        val code = call.receive<CodeDTO>().code
        val userId = service.findUserByCode(code)
        if (userId == null){
            call.respond(HttpStatusCode.Forbidden)
            return
        }

        call.sessions.set(UserSession(userId))
        call.respondRedirect(call.request.queryParameters["backlink"] ?: "/edit")
    }

    suspend fun logOut(call: ApplicationCall){
        call.sessions.clear<UserSession>()
        call.respondRedirect(call.request.queryParameters["backlink"] ?: "/")
    }

    suspend fun logOutAndCleanUp(call: ApplicationCall){
        linksService.clear(call.sessions.get<UserSession>())
        logOut(call)
    }
}