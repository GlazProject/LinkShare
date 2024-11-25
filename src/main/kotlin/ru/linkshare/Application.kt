package ru.linkshare

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.kodein.di.*
import ru.linkshare.domain.exceptions.ApplicationException
import ru.linkshare.domain.models.UserSession
import ru.linkshare.domain.repository.InMemoryCodesRepository
import ru.linkshare.domain.repository.InMemoryLinksRepository
import ru.linkshare.domain.repository.abstractions.CodesRepository
import ru.linkshare.domain.repository.abstractions.LinksRepository
import ru.linkshare.domain.service.LinksService
import ru.linkshare.domain.service.UserService
import ru.linkshare.domain.utils.CodesGenerator
import ru.linkshare.web.controllers.LinksController
import ru.linkshare.web.controllers.UserController
import ru.linkshare.web.links
import ru.linkshare.web.users
import java.io.File

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() = module(DI {
    bindSingleton<CodesRepository> { InMemoryCodesRepository() }
    bindSingleton<LinksRepository> { InMemoryLinksRepository() }
    bindSingleton { CodesGenerator() }
    bindSingleton { UserService(instance(), instance()) }
    bindSingleton { LinksService(instance()) }
    bindSingleton { LinksController(instance()) }
    bindSingleton { UserController(instance(), instance()) }
})

fun Application.module(kodein: DI) {
    install(ContentNegotiation) {
        json()
    }
    install(StatusPages) {
        exception<ApplicationException> { call, cause ->
            call.respondText(text = cause.errorMessage ?: "Unknown error" , status = cause.code)
        }
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }
    install(Sessions){
        header<UserSession>("X-user-sid", directorySessionStorage(File("build/.sessions")))
    }
    install(Authentication) {
        session<UserSession>("editor-session") {
            validate { session -> session } // validate session presented
            challenge("/user/login")
        }
        session<UserSession>("show-session") {
            validate { session -> session }
            challenge ("/tv/code")
        }
    }

    val userController by kodein.instance<UserController>()
    val linksController by kodein.instance<LinksController>()
    routing {
        users(userController)
        links(linksController)
    }
}
