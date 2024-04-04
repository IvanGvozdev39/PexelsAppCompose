package com.test.domain.models.featured_collections

import com.test.domain.models.images.Collection

data class FeaturedCollectionsResponse(
    val collections: List<Collection>,
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val prev_page: String,
    val total_results: Int
)