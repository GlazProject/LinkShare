package com.example.services

import com.example.data.ILinksStorage
import com.example.data.models.Link
import com.example.data.models.LinkTitle
import com.example.data.models.LinkUrl
import com.example.services.models.DeleteLinkRequest
import com.example.services.models.LinkDto

class LinkShareService(private val linksStorage: ILinksStorage): ILinkShareService {
    override suspend fun getLinks(): Collection<LinkDto> =
        linksStorage.getLinks().map { link -> LinkDto(link.url.url, link.title.title) }

    override suspend fun saveLink(request: LinkDto) {
        linksStorage.addLink(
            Link(
                url = LinkUrl(request.url),
                title = LinkTitle(request.title)
            )
        )
    }

    override suspend fun deleteLink(request: DeleteLinkRequest) {
        for (title in request.titles)
            linksStorage.deleteLink(LinkTitle(title))
    }

    override suspend fun clearLinks() {
        for (dto in getLinks())
            linksStorage.deleteLink(LinkTitle(dto.title))
    }
}