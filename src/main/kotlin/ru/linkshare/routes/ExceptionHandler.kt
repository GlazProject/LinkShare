package ru.linkshare.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import ru.linkshare.domain.exceptions.ApplicationException
import kotlin.Exception

fun Application.useStatusPages() {
    install(StatusPages) {
        exception<ApplicationException> { call, cause ->
            call.respondText(text = cause.errorMessage ?: "Unknown error", status = cause.code)
        }

        exception<BadRequestException>(::badRequest)
        exception<IllegalArgumentException>(::badRequest)

        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
}

private suspend fun<T> badRequest(call: ApplicationCall, cause: T) where T: Exception {
    call.respondText(text = cause.message ?: "Incorrect arguments", status = HttpStatusCode.BadRequest)
}