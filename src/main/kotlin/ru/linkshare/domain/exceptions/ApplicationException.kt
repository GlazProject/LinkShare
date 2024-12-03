package ru.linkshare.domain.exceptions

import io.ktor.http.*

class ApplicationException(
    val code: HttpStatusCode,
    val errorMessage: String? = null
): Exception() {
    companion object{
        fun authenticationFailed() = ApplicationException(HttpStatusCode.Unauthorized, "User is not authenticated")
        fun forbidden() = ApplicationException(HttpStatusCode.Unauthorized, "User is not authenticated")
    }
}