package com.example.data

import com.example.data.models.Link
import com.example.data.models.LinkTitle

interface ILinksStorage {
    suspend fun getLinks(): List<Link>
    suspend fun addLink(link: Link)
    suspend fun deleteLink(title: LinkTitle)
}