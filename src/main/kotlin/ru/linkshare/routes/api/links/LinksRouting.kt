package ru.linkshare.routes.api.links

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import ru.linkshare.routes.AuthConstants

fun Routing.configureLinksRouting(linksController: LinksController) {
    addEditorRouting(linksController)
    addTvRouting(linksController)
}

private fun Routing.addEditorRouting(linksController: LinksController) {
    authenticate(AuthConstants.editorSessionName) {
        route("api/edit/links") {
            get { linksController.getLinks(this.call) }
            post { linksController.saveLink(this.call) }
            delete { linksController.deleteLinks(this.call) }
            post("clear") { linksController.clear(this.call) }
        }
    }
}

private fun Routing.addTvRouting(linksController: LinksController) {
    authenticate(AuthConstants.tvSessionName) {
        get("api/tv/links") { linksController.getLinks(this.call) }
    }
}