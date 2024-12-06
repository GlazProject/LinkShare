package ru.linkshare.routes.api.user

import io.ktor.server.routing.*

fun Routing.configureUserRouting(userController: UserController) {
    route("api/user") {
        get("id") { userController.getUserId(this.call) }

        route("code") {
            get { userController.getCodeForCurrentSession(this.call) }
            get("new") { userController.getNewCode(this.call) }
        }

        get("login") { userController.login(this.call) }
        get("logout") { userController.logout(this.call) }
    }
}