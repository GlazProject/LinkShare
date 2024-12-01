package ru.linkshare.domain.service

import ru.linkshare.domain.exceptions.ApplicationException
import ru.linkshare.domain.models.LinkInfo
import ru.linkshare.domain.models.LinkTitle
import ru.linkshare.domain.models.UserSession
import ru.linkshare.domain.repository.abstractions.LinksRepository

class LinksService(
    private val repository: LinksRepository
) {
    // TODO пробрасывать время жизни для ссылки
    suspend fun saveLink(session: UserSession?, request: LinkInfo){
        checkSession(session)
        repository.addLink(session!!.userId, request)
    }

    suspend fun getLinks(session: UserSession?): List<LinkInfo>{
        checkSession(session)
        return repository.getLinks(session!!.userId)
    }

    suspend fun deleteLinks(session: UserSession?, titles: List<LinkTitle>){
        checkSession(session)
        for (title in titles) {
            assert(title.title.isNotEmpty())
            repository.deleteLink(session!!.userId, title)
        }
    }

    suspend fun clear(session: UserSession?){
        checkSession(session)
        for (link: LinkInfo in repository.getLinks(session!!.userId))
            repository.deleteLink(session.userId, link.title)
    }

    private fun checkSession(session: UserSession?){
        if (session == null)
            throw ApplicationException.unauthenticated()
    }
}