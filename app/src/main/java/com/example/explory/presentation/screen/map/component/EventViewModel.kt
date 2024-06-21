package com.example.explory.presentation.screen.map.component

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.domain.usecase.GetFriendProfileUseCase
import com.example.explory.domain.usecase.SendReviewUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventViewModel(
    private val getFriendProfileUseCase: GetFriendProfileUseCase,
    private val sendReviewUseCase: SendReviewUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(EventState())
    val state = _state.asStateFlow()
    fun getUserInfo(friendId: String) {
        viewModelScope.launch {
            try {
                val user = getFriendProfileUseCase.invoke(friendId)
                _state.update { it.copy(user = user) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun sendReview(
        eventId: Long,
        rating: Int,
        review: String,
        images: List<Uri>
    ) {
        val request = SendReviewRequest(eventId, rating, review, images)
        viewModelScope.launch {
            try {
                sendReviewUseCase.execute(request)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class SendReviewRequest(
    val eventId: Long,
    val rating: Int,
    val reviewText: String?,
    val images: List<Uri>
)
