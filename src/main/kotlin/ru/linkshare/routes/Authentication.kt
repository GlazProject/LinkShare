package ru.linkshare.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import ru.linkshare.domain.models.UserSession
import java.io.File

internal class AuthConstants {
    companion object{
        const val XAuthSid: String = "X-linkshare-sid"
        const val tvSessionName: String = "tv-session"
        const val editorSessionName: String = "editor-session"
    }
}

fun Application.useAuthentication() {

    install(Sessions){
        // TODO Вынести в конфигурационный файл
        val secretEncryptKey = hex("00112233445566778899aabbccddeeff")
        val secretSignKey = hex("6819b57a326945c1968f45236589")
        cookie<UserSession>(AuthConstants.XAuthSid, directorySessionStorage(File("build/.sessions"))){
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60*60*24*7
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretSignKey))
        }
    }

    install(Authentication) {
        session<UserSession>(AuthConstants.editorSessionName) {
            validate { it } // Достаточно проверить, что сессия существует
            challenge("/user/login")
        }

        session<UserSession>(AuthConstants.tvSessionName) {
            validate { it } // Достаточно проверить, что сессия существует
            challenge("/tv/code")
        }
    }
}