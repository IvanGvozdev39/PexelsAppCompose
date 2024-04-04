package com.test.domain.api

import com.test.domain.models.featured_collections.FeaturedCollectionsResponse
import com.test.domain.models.images.CollectionMediaResponse
import com.test.domain.models.images.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterface {

    @Headers("Authorization: dl4SeUa1OfAeZQqkN160kyujsExbgUz5qLd621n6hGVQD2FdISfBM11D")
    @GET("collections/featured")
    suspend fun getFeaturedCollections(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 7
    ): Response<FeaturedCollectionsResponse>


    @Headers("Authorization: dl4SeUa1OfAeZQqkN160kyujsExbgUz5qLd621n6hGVQD2FdISfBM11D")
    @GET("search")
    suspend fun getImages(
        @Query("query") query : String,
        @Query("per_page") perpage : Int = 80,
    ): Response<ImageResponse>


    @Headers("Authorization: dl4SeUa1OfAeZQqkN160kyujsExbgUz5qLd621n6hGVQD2FdISfBM11D")
    @GET("curated")
    suspend fun getCuratedImages(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 80
    ): Response<ImageResponse>


    @Headers("Authorization: dl4SeUa1OfAeZQqkN160kyujsExbgUz5qLd621n6hGVQD2FdISfBM11D")
    @GET("popular")
    suspend fun getPopularImages(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 80
    ): Response<ImageResponse>


    @Headers("Authorization: dl4SeUa1OfAeZQqkN160kyujsExbgUz5qLd621n6hGVQD2FdISfBM11D")
    @GET("collections/{collectionId}")
    suspend fun getImagesFromCollection(
        @Query("collectionId") collectionId: String,
        @Query("type") type: String = "photos",
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 80
    ): Response<CollectionMediaResponse>
}