package ru.linkshare.routes

import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.configureStatics(){
    useLinksStatics()
    useTvStatics()
    useUserStatics()
}

private fun Routing.useTvStatics() {
    staticResources("tv/code", "/static/tv/code")
    staticResources("/", "static/main")

    authenticate(AuthConstants.tvSessionName) {
        staticResources("tv", "/static/tv")
    }
}

private fun Routing.useLinksStatics(){
    get("edit") { this.call.respondRedirect("edit/links") }

    authenticate(AuthConstants.editorSessionName) {
        staticResources("edit/links", "/static/edit/links")
    }
}

private fun Routing.useUserStatics(){
    get("user") {this.call.respondRedirect("user/login")}

    staticResources("user/login", "/static/user/login")
}