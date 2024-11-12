package ru.linkshare

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.sessions.*
import ru.linkshare.domain.models.UserSession
import ru.linkshare.plugins.configureErrors
import ru.linkshare.plugins.configureRouting
import ru.linkshare.plugins.configureSerialization
import java.io.File

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
    configureErrors()
    install(Sessions){
        header<UserSession>("X-user-sid", directorySessionStorage(File("build/.sessions")))
    }
    install(Authentication) {
        session<UserSession>("editor-session") {
            validate { session -> session } // validate session presented
            challenge("/login")
        }
        session<UserSession>("show-session") {
            validate { session -> session }
            challenge ("/")
        }
    }
}
