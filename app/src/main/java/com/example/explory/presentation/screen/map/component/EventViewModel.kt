package com.example.explory.presentation.screen.map.component

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.domain.usecase.GetFriendProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventViewModel(
    private val getFriendProfileUseCase: GetFriendProfileUseCase
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
}
