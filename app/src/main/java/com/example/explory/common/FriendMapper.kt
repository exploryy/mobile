package com.example.explory.common

import com.example.explory.data.model.friend.Friend
import com.example.explory.data.model.friend.FriendsResponse
import com.example.explory.data.model.profile.ProfileDto

object FriendMapper {
    private fun toFriend(profileDto: ProfileDto, isBestFriend: Boolean = false): Friend {
        return Friend(
            name = profileDto.username,
            email = profileDto.email,
            avatar = profileDto.avatarUrl,
            isBestFriend = isBestFriend
        )
    }

    fun toFriends(friendsResponse: FriendsResponse): List<Friend> {
        val allFriends = mutableListOf<Friend>()

        friendsResponse.friends.forEach {
            allFriends.add(toFriend(it))
        }

        friendsResponse.favoriteFriends.forEach { favorite ->
            val index = allFriends.indexOfFirst { it.email == favorite.email }
            if (index != -1) {
                allFriends[index] = allFriends[index].copy(isBestFriend = true)
            } else {
                allFriends.add(toFriend(favorite, isBestFriend = true))
            }
        }

        return allFriends
    }
}