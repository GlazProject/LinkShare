package com.example.data

import com.example.data.models.Link
import com.example.data.models.LinkTitle
import com.example.data.models.LinkUrl

class InMemoryLinksStorage: ILinksStorage {
    private val links = mutableMapOf<LinkTitle, LinkUrl>()

    override suspend fun getLinks(): List<Link> {
        return links.map { (title, url) -> Link(url, title) }
    }

    override suspend fun addLink(link: Link) {
        links[link.title] = link.url
    }

    override suspend fun deleteLink(title: LinkTitle) {
        links.remove(title)
    }
}