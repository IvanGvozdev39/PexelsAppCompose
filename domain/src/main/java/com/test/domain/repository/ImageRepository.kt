package com.test.domain.repository

import com.test.domain.models.images.Collection
import com.test.domain.models.images.CollectionMediaResponse
import com.test.domain.models.images.ImageResponse
import com.test.domain.result.Result
import retrofit2.Response

interface ImageRepository {

    suspend fun getImages(query : String): Result<ImageResponse>
    suspend fun getCuratedImages(): Result<ImageResponse>
    suspend fun getPopularImages(): Result<ImageResponse>
    suspend fun getFeaturedCollectionNames(): Result<List<Collection>>
    suspend fun getImagesFromCollection(collectionId: String): Result<CollectionMediaResponse>
}