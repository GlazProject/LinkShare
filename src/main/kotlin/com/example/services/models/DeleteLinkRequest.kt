package com.example.services.models

data class DeleteLinkRequest(val titles: List<String>) {
    companion object{
        fun instance(vararg titles: String): DeleteLinkRequest{
            val result = ArrayList<String>()
            for (t in titles)
                result.add(t)
            return DeleteLinkRequest(result)
        }
    }
}