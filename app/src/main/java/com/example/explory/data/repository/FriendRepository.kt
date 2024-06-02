package com.example.explory.data.repository

import com.example.explory.data.model.friend.FriendsResponse
import com.example.explory.data.model.profile.ProfileDto
import com.example.explory.data.model.requests.FriendRequest
import com.example.explory.data.service.ApiService

class FriendRepository(private val apiService: ApiService) {
    suspend fun getFriendRequests(): FriendRequest {
        return apiService.getFriendRequests()
    }

    suspend fun acceptFriend(userId: String) {
        return apiService.acceptFriend(userId)
    }

    suspend fun declineFriend(userId: String) {
        return apiService.declineFriend(userId)
    }

    suspend fun getFriends(): FriendsResponse {
        return apiService.getFriends()
    }

    suspend fun addFriend(userId: String){
        return apiService.addFriend(userId)
    }

    suspend fun getUserList(username: String) : List<ProfileDto>{
        return apiService.getUserList(username)
    }
}
