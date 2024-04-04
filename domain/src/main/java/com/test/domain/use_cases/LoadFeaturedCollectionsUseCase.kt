package com.test.domain.use_cases

import com.test.domain.repository.ImageRepository

class LoadFeaturedCollectionsUseCase(private val imageRepository: ImageRepository) {

    suspend fun execute(): com.test.domain.result.Result<List<com.test.domain.models.images.Collection>> {
        return imageRepository.getFeaturedCollectionNames()
    }
}