package com.test.domain.use_cases

import com.test.domain.models.images.ImageResponse
import com.test.domain.repository.ImageRepository
import com.test.domain.result.Result

class LoadCuratedImagesUseCase(private val imageRepository: ImageRepository) {

    suspend fun execute(): Result<ImageResponse> {
        return imageRepository.getCuratedImages()
    }
}