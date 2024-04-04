package com.test.data.repository

import com.test.domain.api.RetrofitInstance
import com.test.domain.api.RetrofitInstance.Companion.api
import com.test.domain.models.images.CollectionMediaResponse
import com.test.domain.models.images.ImageResponse
import com.test.domain.repository.ImageRepository
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor() : ImageRepository {

    override suspend fun getImages(query: String): com.test.domain.result.Result<ImageResponse> {
        return try {
            val response = RetrofitInstance.api.getImages(query)
            if (response.isSuccessful) {
                com.test.domain.result.Result.Success(response.body()!!)
            } else {
                com.test.domain.result.Result.Error(Exception("Failed to fetch images"))
            }
        } catch (e: Exception) {
            com.test.domain.result.Result.Error(e)
        }
    }

    override suspend fun getCuratedImages(): com.test.domain.result.Result<ImageResponse> {
        return try {
            val response = api.getCuratedImages()
            if (response.isSuccessful) {
                com.test.domain.result.Result.Success(response.body()!!)
            } else {
                com.test.domain.result.Result.Error(Exception("Failed to fetch curated images"))
            }
        } catch (e: Exception) {
            com.test.domain.result.Result.Error(e)
        }
    }

    override suspend fun getPopularImages(): com.test.domain.result.Result<ImageResponse> {
        return try {
            val response = api.getPopularImages()
            if (response.isSuccessful) {
                com.test.domain.result.Result.Success(response.body()!!)
            } else {
                com.test.domain.result.Result.Error(Exception("Failed to fetch popular images"))
            }
        } catch (e: Exception) {
            com.test.domain.result.Result.Error(e)
        }
    }

    override suspend fun getFeaturedCollectionNames(): com.test.domain.result.Result<List<com.test.domain.models.images.Collection>> {
        return try {
            val response = api.getFeaturedCollections()
            if (response.isSuccessful) {
                com.test.domain.result.Result.Success(response.body()?.collections ?: emptyList())
            } else {
                com.test.domain.result.Result.Error(Exception("Failed to fetch featured collections"))
            }
        } catch (e: Exception) {
            com.test.domain.result.Result.Error(e)
        }
    }

    override suspend fun getImagesFromCollection(collectionId: String): com.test.domain.result.Result<CollectionMediaResponse> {
        return try {
            val response = api.getImagesFromCollection(collectionId)
            if (response.isSuccessful) {
                com.test.domain.result.Result.Success(response.body()!!)
            } else {
                com.test.domain.result.Result.Error(Exception("Failed to fetch images from collection"))
            }
        } catch (e: Exception) {
            com.test.domain.result.Result.Error(e)
        }
    }
}