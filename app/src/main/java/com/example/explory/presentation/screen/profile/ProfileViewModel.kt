package com.example.explory.presentation.screen.profile

import android.content.ContentResolver
import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.data.model.profile.Profile
import com.example.explory.data.model.profile.ProfileMultipart
import com.example.explory.data.model.profile.ProfileRequest
import com.example.explory.data.model.profile.toProfile
import com.example.explory.domain.usecase.EditProfileUseCase
import com.example.explory.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val editProfileUseCase: EditProfileUseCase,
    private val context: Context
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

    fun changeOpenEditDialogState(){
        _profileState.update { it.copy(isEditDialogOpen = !it.isEditDialogOpen ) }
    }

    fun editProfile(profile: ProfileRequest) {
        val newUsername = if (!profile.username.equals(_profileState.value.profile?.name)) profile.username else null
        val newEmail = if (!profile.email.equals(_profileState.value.profile?.email)) profile.email else null

        val newAvatarFile = profile.avatar?.let { uri ->
            val fileName = "${profile.username?.replace(" ", "_")}_avatar.png"
            context.contentResolver.openInputStream(uri.toUri())?.use { inputStream ->
                val file = File(context.cacheDir, fileName)
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                file
            }
        }

        val profileRequest = ProfileMultipart(
            username = newUsername,
            email = newEmail,
            password = profile.password,
            avatar = newAvatarFile
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
}