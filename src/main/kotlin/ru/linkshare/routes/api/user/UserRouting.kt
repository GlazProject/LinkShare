package ru.linkshare.routes.api.user

import io.ktor.server.routing.*

fun Routing.configureUserRouting(userController: UserController) {
    route("api/user") {

        route("code") {
            get { userController.getCodeForCurrentSession(this.call) }
            get("new") { userController.getNewCode(this.call) }
        }

        get("login") { userController.logIn(this.call) }
        get("logout") { userController.logOut(this.call) }
    }
}