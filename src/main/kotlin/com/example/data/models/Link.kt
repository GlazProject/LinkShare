package com.example.data.models

data class Link (
    val url: LinkUrl,
    val title: LinkTitle
)

@JvmInline
value class LinkUrl(val url: String)

@JvmInline
value class LinkTitle(val title: String)