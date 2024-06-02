package com.example.explory.data.repository

import com.example.explory.data.model.friend.FriendsResponse
import com.example.explory.data.service.ApiService

class FriendRepository(private val apiService: ApiService) {
    suspend fun getFriends(): FriendsResponse {
        return apiService.getFriends()
    }
}