package ru.linkshare.web

import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get
import ru.linkshare.web.controllers.LinksController
import ru.linkshare.web.controllers.UserController

fun Routing.redirects() {
    get("") { this.call.respondRedirect("tv") }
    get("edit") {this.call.respondRedirect("edit/links")}
    get("user") {this.call.respondRedirect("user/login")}
}

fun Routing.statics() {
    staticResources("user/login", "/static/user/login")
    staticResources("tv/code", "/static/tv/code")
    authenticate("show-session") {
        staticResources("tv", "/static/tv")
    }
    authenticate("editor-session") {
        staticResources("edit/links", "/static/edit/links")
    }
}

fun Routing.users(userController: UserController) {
    route("api/user") {
        route("code") {
            get { userController.getCodeForCurrentSession(this.call) }
            get("new") { userController.getNewCode(this.call) }
        }
        post("login") { userController.logIn(this.call) }
        get("logout") { userController.logOut(this.call) }
    }
}

// TODO прибраться и сделать нормальные модели запросов
fun Routing.links(linksController: LinksController) {
    route("api") {
        authenticate("show-session") {
            route("tv") {
                get("links") { linksController.getLinks(this.call) }
            }
        }

        authenticate("editor-session") {
            route("edit/links") {
                get { linksController.getLinks(this.call) }
                post("save") { linksController.saveLink(this.call) }
                post("delete") { linksController.deleteLinks(this.call) }
            }
        }
    }
}