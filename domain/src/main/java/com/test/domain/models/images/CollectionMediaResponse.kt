package com.test.domain.models.images

data class CollectionMediaResponse(
    val id: String,
    val media: List<Media>,
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val prev_page: String,
    val total_results: Int
)