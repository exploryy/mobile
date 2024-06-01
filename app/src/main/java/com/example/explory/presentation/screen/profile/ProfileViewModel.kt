package com.example.explory.presentation.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.data.model.toProfile
import com.example.explory.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {
    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    init {
        fetchProfile()
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileState(isLoading = true)
            try {
                val profileDto = getProfileUseCase.execute()
                val profile = profileDto.toProfile()
                _profileState.value = ProfileState(profile = profile)
            } catch (e: Exception) {
                _profileState.value = ProfileState(error = e.message)
            }
        }
    }
}