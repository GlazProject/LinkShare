package ru.linkshare.web.controllers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import ru.linkshare.domain.models.UserSession
import ru.linkshare.domain.service.LinksService
import ru.linkshare.domain.service.UserService
import ru.linkshare.web.models.CodeDTO

class UserController(
    private val service: UserService,
    private val linksService: LinksService
) {
    suspend fun getCode(call: ApplicationCall){
        val userInfo = service.createUserCode()
        call.sessions.set(UserSession(userInfo.userId))
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
    }

    suspend fun logOut(call: ApplicationCall){
        linksService.clear(call.sessions.get<UserSession>())
        call.sessions.clear<UserSession>()
        call.respondRedirect("/edit")
    }
}