package com.example.explory.presentation.screen.profile

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.data.model.profile.ProfileMultipart
import com.example.explory.data.model.profile.ProfileRequest
import com.example.explory.data.model.profile.toProfile
import com.example.explory.domain.usecase.EditProfileUseCase
import com.example.explory.domain.usecase.GetFriendRequestsUseCase
import com.example.explory.domain.usecase.GetProfileUseCase
import com.example.explory.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val editProfileUseCase: EditProfileUseCase,
    private val getFriendRequestsUseCase: GetFriendRequestsUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    init {
        fetchProfile()
        getNotificationCount()
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

    fun changeCurrentPage(newPage: Int) {
        _profileState.update { it.copy(profileScreenState = newPage) }
    }

    fun changeOpenEditDialogState() {
        _profileState.update { it.copy(isEditDialogOpen = !it.isEditDialogOpen) }
    }

    fun getNotificationCount() {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(isLoading = true)
            try {
                val response = getFriendRequestsUseCase.execute()
                _profileState.value =
                    _profileState.value.copy(notificationCount = response.other.size)
            } catch (e: Exception) {
                _profileState.value = _profileState.value.copy(error = e.message)
            } finally {
                _profileState.value = _profileState.value.copy(isLoading = false)
            }
        }
    }

    fun editProfile(profile: ProfileRequest) {
        val newUsername =
            if (!profile.username.equals(_profileState.value.profile?.name)) profile.username else null
        val newEmail =
            if (!profile.email.equals(_profileState.value.profile?.email)) profile.email else null

        val newUri =
            if (!profile.avatar?.equals(_profileState.value.profile?.avatarUri?.toUri())!!) {
                profile.avatar
            } else null

        val profileRequest = ProfileMultipart(
            username = newUsername, email = newEmail, password = profile.password, avatar = newUri
        )

        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(isLoading = true)
            try {
                Log.d("ProfileViewModel", "Starting profile update")
                editProfileUseCase.execute(profileRequest)
                Log.d("ProfileViewModel", "Profile update executed")

                fetchProfile()
                Log.d("ProfileViewModel", "Profile fetched")

                changeOpenEditDialogState()
                Log.d("ProfileViewModel", "Dialog state changed")
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error updating profile", e)
                _profileState.value = _profileState.value.copy(error = e.message)
            } finally {
                _profileState.value = _profileState.value.copy(isLoading = false)
                Log.d("ProfileViewModel", "Loading state set to false")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                logoutUseCase.execute()
                _profileState.update { it.copy(loggedOut = true) }
            } catch (e: Exception) {
                Log.d("ProfileViewModel", "Error logging out", e)
                _profileState.value = _profileState.value.copy(error = e.message)
            }
        }
    }
}