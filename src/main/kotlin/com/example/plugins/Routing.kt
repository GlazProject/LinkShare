package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get

fun Application.configureRouting() {
    routing {
        staticResources("/static", "static")
        get("/hello") {
            call.respondRedirect("/static/index.html")
        }

        get("/google") {
            call.respondRedirect(Url("https://google.com"))
        }

        get("/"){
            call.respondText("Admin controller")
        }
    }
}
