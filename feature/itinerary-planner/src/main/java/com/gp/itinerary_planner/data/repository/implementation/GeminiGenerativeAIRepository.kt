package com.gp.itinerary_planner.data.repository.implementation

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.gp.itinerary_planner.data.repository.contract.GenerativeAIRepository
import com.gp.itinerary_planner.domain.model.NetworkResult
import javax.inject.Inject

class GeminiGenerativeAIRepository @Inject constructor(private val generativeModel: GenerativeModel) :
    GenerativeAIRepository {
    override suspend fun googleGeminiGenerateContent(prompt: String): NetworkResult<GenerateContentResponse> {
        return try {
            NetworkResult.Success(data = generativeModel.generateContent(prompt))
        } catch (e: Exception) {
            NetworkResult.Error(errorMessage = e.localizedMessage)
        }
    }
}