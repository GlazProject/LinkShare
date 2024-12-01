package ru.linkshare.routes

import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Routing.setupTvStatics() {
    staticResources("user/login", "/static/user/login")

    staticResources("tv/code", "/static/tv/code")
    authenticate("show-session") {
        staticResources("tv", "/static/tv")
    }

    authenticate("editor-session") {
        staticResources("edit/links", "/static/edit/links")
    }
}

fun Routing.setupLinksStatics(){

}

fun Routing.setupUserStatics(){
    staticResources("user/login", "/static/user/login")
}