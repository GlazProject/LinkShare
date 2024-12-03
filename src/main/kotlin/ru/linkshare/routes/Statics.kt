package ru.linkshare.routes

import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.configureStatics() {
    staticResources("/", "/static")

    staticResources("tv/code", "/static/tv/code")
    authenticate(AuthConstants.tvSessionName) {
        staticResources("tv", "/static/tv")
    }

    authenticate(AuthConstants.editorSessionName) {
        staticResources("edit", "/static/edit")
    }

    get("edit") { this.call.respondRedirect("edit/links") }
    get("user") { this.call.respondRedirect("user/login") }
}