package com.example.explory.presentation.screen.friendprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.data.model.profile.FriendProfileDto
import com.example.explory.domain.usecase.GetFriendProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FriendProfileViewModel(
    private val getFriendProfileUseCase: GetFriendProfileUseCase
) : ViewModel() {
    private val _friendProfile = MutableStateFlow<FriendProfileDto?>(null)
    val friendProfile: StateFlow<FriendProfileDto?> = _friendProfile.asStateFlow()

    fun loadFriendProfile(clientId: String) {
        viewModelScope.launch {
            try {
                val profile = getFriendProfileUseCase(clientId)
                _friendProfile.value = profile
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun checkUserPrivacy(): Boolean {
        return !(_friendProfile.value?.level == null
                || _friendProfile.value?.distance == null
                || _friendProfile.value?.experience == null
                || _friendProfile.value?.totalExperienceInLevel == null)
    }
}