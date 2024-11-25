package ru.linkshare.web

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import ru.linkshare.web.controllers.LinksController
import ru.linkshare.web.controllers.UserController

fun Routing.users(userController: UserController) {
    route("tv") {
        get("code") { userController.getCode(this.call) }
    }

    route("user") {
        post("login") { userController.logIn(this.call) }
        post("logout") { userController.logOut(this.call) }
    }
}

fun Routing.links(linksController: LinksController) {
    route("") {
        authenticate("show-session") {
            get { linksController.getLinks(this.call) }
        }
    }
    route("edit") {
        authenticate("editor-session") {
            route("links") {
                get { linksController.getLinks(this.call) }
                post("save") { linksController.saveLink(this.call) }
                post("delete") { linksController.deleteLinks(this.call) }
            }
        }
    }
}