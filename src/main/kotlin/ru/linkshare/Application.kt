package ru.linkshare

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import ru.linkshare.repository.InMemoryCodesRepository
import ru.linkshare.repository.InMemoryLinksRepository
import ru.linkshare.domain.repository.CodesRepository
import ru.linkshare.domain.repository.LinksRepository
import ru.linkshare.domain.service.LinksService
import ru.linkshare.domain.service.UserService
import ru.linkshare.domain.utils.CodesGenerator
import ru.linkshare.routes.api.links.LinksController
import ru.linkshare.routes.api.links.configureLinksRouting
import ru.linkshare.routes.api.user.UserController
import ru.linkshare.routes.api.user.configureUserRouting
import ru.linkshare.routes.configureStatics
import ru.linkshare.routes.useAuthentication
import ru.linkshare.routes.useStatusPages

fun main(args: Array<String>) = EngineMain.main(args)

// Используется в качестве точки входа в конфигурации
fun Application.module() = module(DI {
    bindSingleton<CodesRepository> { InMemoryCodesRepository() }
    bindSingleton<LinksRepository> { InMemoryLinksRepository() }
    bindSingleton { CodesGenerator() }
    bindSingleton { UserService(instance(), instance()) }
    bindSingleton { LinksService(instance()) }
    bindSingleton { LinksController(instance()) }
    bindSingleton { UserController(instance()) }
})

// TODO настроить SSL
fun Application.module(kodein: DI) {
    useSerialization()
    useAuthentication()
    useStatusPages()
    useRouting(kodein)
}

private fun Application.useRouting(kodein: DI){
    val userController by kodein.instance<UserController>()
    val linksController by kodein.instance<LinksController>()

    routing {
        configureStatics()
        configureLinksRouting(linksController)
        configureUserRouting(userController)
    }
}

private fun Application.useSerialization() {
    install(ContentNegotiation) {
        json()
    }
}
