package com.test.domain.use_cases

import com.test.domain.models.images.ImageResponse
import com.test.domain.repository.ImageRepository
import com.test.domain.result.Result

class SearchUseCase(private val imageRepository: ImageRepository) {

    suspend fun execute(query: String): Result<ImageResponse> {
        return imageRepository.getImages(query)
    }
}