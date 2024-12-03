package ru.linkshare.routes.api.links

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import ru.linkshare.domain.models.*
import ru.linkshare.domain.service.LinksService
import ru.linkshare.routes.api.models.DeleteLinksDTO
import ru.linkshare.routes.api.models.LinkDTO

class LinksController(private val service: LinksService) {
    suspend fun saveLink(call: ApplicationCall) {
        val request = call.receive<LinkDTO>()
        require(request.title.isNotEmpty()) { "Title should not be empty" }
        require(request.url.isNotEmpty()) { "Url should not be empty" }

        service.saveLink(
            call.sessions.get<UserSession>(),
            LinkInfo(LinkTitle(request.title), LinkUrl(request.url))
        )

        call.respond(HttpStatusCode.Created)
    }

    suspend fun getLinks(call: ApplicationCall) {
        val links = service.getLinks(call.sessions.get<UserSession>())
        call.respond(links.map { LinkDTO(it.title.title, it.url.url) })
    }

    suspend fun deleteLinks(call: ApplicationCall) {
        val request = call.receive<DeleteLinksDTO>()
        service.deleteLinks(call.sessions.get<UserSession>(), request.titles.map { LinkTitle(it) })
        call.respond(HttpStatusCode.NoContent)
    }

    suspend fun clear(call: ApplicationCall) {
        service.clear(call.sessions.get<UserSession>())
        call.respond(HttpStatusCode.NoContent)
    }
}