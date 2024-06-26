package com.example.explory.presentation.screen.event

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.data.model.review.SendReviewRequest
import com.example.explory.domain.model.BuffDto
import com.example.explory.domain.usecase.AcceptFriendUseCase
import com.example.explory.domain.usecase.ApplyBuffUseCase
import com.example.explory.domain.usecase.DeclineFriendUseCase
import com.example.explory.domain.usecase.GetBuffListUseCase
import com.example.explory.domain.usecase.GetUserProfileUseCase
import com.example.explory.domain.usecase.SendReviewUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val sendReviewUseCase: SendReviewUseCase,
    private val getBuffListUseCase: GetBuffListUseCase,
    private val applyBuffUseCase: ApplyBuffUseCase,
    private val acceptFriendUseCase: AcceptFriendUseCase,
    private val declineFriendUseCase: DeclineFriendUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(EventState())
    val state = _state.asStateFlow()
    fun getUserInfo(friendId: String) {
        viewModelScope.launch {
            try {
                val user = getUserProfileUseCase.execute(friendId)
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

    fun getBuffList(level: Int) {
        viewModelScope.launch {
            try {
                val buffs = getBuffListUseCase.execute(level)
                _state.update { state ->
                    state.copy(buffs = buffs.map {
                        BuffDto(it.buffId, it.status.title, it.status.animationResource)
                    })
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun applyBuff(buffId: Long) {
        viewModelScope.launch {
            try {
                applyBuffUseCase.execute(buffId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun acceptFriendRequest(friendId: String) {
        viewModelScope.launch {
            try {
                acceptFriendUseCase.execute(friendId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun declineFriendRequest(friendId: String) {
        viewModelScope.launch {
            try {
                declineFriendUseCase.execute(friendId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

