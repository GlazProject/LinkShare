package ru.linkshare.web.controllers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import ru.linkshare.domain.models.*
import ru.linkshare.domain.service.LinksService
import ru.linkshare.web.models.DeleteLinksDTO
import ru.linkshare.web.models.LinkDTO

class LinksController(private val service: LinksService) {

    suspend fun saveLink(call: ApplicationCall){
        val request = call.receive<LinkDTO>()
        require(request.title.isNotEmpty()) { "Title should not be empty" }
        require(request.url.isNotEmpty()) { "Url should not be empty" }

        service.saveLink(
            call.sessions.get<UserSession>(),
            LinkInfo(LinkTitle(request.title), LinkUrl(request.url)))

        call.respond(HttpStatusCode.OK)
    }

    suspend fun getLinks(call: ApplicationCall){
        service.getLinks(call.sessions.get<UserSession>()).apply { call.respond(this) }
    }

    suspend fun deleteLinks(call: ApplicationCall){
        call.receive<DeleteLinksDTO>().apply {
            service.deleteLinks(call.sessions.get<UserSession>(), this.titles.map { LinkTitle(it) })
        }
    }
}