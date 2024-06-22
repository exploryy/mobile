package com.example.explory.domain.usecase

import com.example.explory.data.repository.QuestRepository
import com.example.explory.data.model.review.SendReviewRequest

class SendReviewUseCase(
    private val questRepository: QuestRepository
) {
    suspend fun execute(reviewRequest: SendReviewRequest) {
        questRepository.sendReview(reviewRequest)
    }
}
