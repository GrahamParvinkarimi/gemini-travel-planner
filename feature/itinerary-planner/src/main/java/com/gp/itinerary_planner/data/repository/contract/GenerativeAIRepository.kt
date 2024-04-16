package com.gp.itinerary_planner.data.repository.contract

import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.gp.itinerary_planner.domain.model.NetworkResult

interface GenerativeAIRepository {
    suspend fun googleGeminiGenerateContent(prompt: String): NetworkResult<GenerateContentResponse>
}