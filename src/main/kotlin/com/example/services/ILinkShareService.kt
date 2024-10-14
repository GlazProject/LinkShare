package com.example.services

import com.example.services.models.DeleteLinkRequest
import com.example.services.models.LinkDto

interface ILinkShareService {
    suspend fun getLinks(): Collection<LinkDto>
    suspend fun saveLink(request: LinkDto)
    suspend fun deleteLink(request: DeleteLinkRequest)
    suspend fun clearLinks()
}