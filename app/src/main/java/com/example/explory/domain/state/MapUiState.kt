package com.example.explory.domain.state

data class MapUiState(
    val showMap: Boolean = false,
    val showRequestPermissionButton: Boolean = false,
    val permissionRequestCount: Int = 1,
    val showFriendsScreen: Boolean = false
)
