package com.gp.itinerary_planner.domain.usecase

import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.gp.itinerary_planner.data.repository.contract.GenerativeAIRepository
import com.gp.itinerary_planner.domain.model.NetworkResult
import javax.inject.Inject

class GenerateItineraryUseCase @Inject constructor(
    private val generativeAIRepository: GenerativeAIRepository
) {
    suspend operator fun invoke(prompt: String): NetworkResult<GenerateContentResponse> {
        return generativeAIRepository.googleGeminiGenerateContent(prompt)
    }
}