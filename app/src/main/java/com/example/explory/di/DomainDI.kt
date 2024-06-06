package com.example.explory.di

import com.example.explory.domain.usecase.AcceptFriendUseCase
import com.example.explory.domain.usecase.AddFavoriteFriendUseCase
import com.example.explory.domain.usecase.AddFriendUseCase
import com.example.explory.domain.usecase.CheckUserTokenUseCase
import com.example.explory.domain.usecase.DeclineFriendUseCase
import com.example.explory.domain.usecase.EditProfileUseCase
import com.example.explory.domain.usecase.GetCoinsUseCase
import com.example.explory.domain.usecase.GetFriendRequestsUseCase
import com.example.explory.domain.usecase.GetFriendsUseCase
import com.example.explory.domain.usecase.GetPolygonsUseCase
import com.example.explory.domain.usecase.GetProfileUseCase
import com.example.explory.domain.usecase.GetQuestsUseCase
import com.example.explory.domain.usecase.GetUserListUseCase
import com.example.explory.domain.usecase.GetUserStatisticUseCase
import com.example.explory.domain.usecase.GetUserTokenUseCase
import com.example.explory.domain.usecase.LogoutUseCase
import com.example.explory.domain.usecase.PostLoginUseCase
import com.example.explory.domain.usecase.PostRegistrationUseCase
import com.example.explory.domain.usecase.RefreshTokenUseCase
import com.example.explory.domain.usecase.RemoveFavoriteFriendUseCase
import com.example.explory.domain.websocket.LocationTracker
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun domainModule() = module {
    factoryOf(::GetPolygonsUseCase)
    factoryOf(::PostLoginUseCase)
    factoryOf(::PostRegistrationUseCase)
    factoryOf(::CheckUserTokenUseCase)
    factoryOf(::GetUserTokenUseCase)
    factoryOf(::LocationTracker)
    factoryOf(::RefreshTokenUseCase)
    factoryOf(::GetProfileUseCase)
    factoryOf(::EditProfileUseCase)
    factoryOf(::GetFriendsUseCase)
    factoryOf(::AcceptFriendUseCase)
    factoryOf(::DeclineFriendUseCase)
    factoryOf(::GetFriendRequestsUseCase)
    factoryOf(::AddFriendUseCase)
    factoryOf(::GetUserListUseCase)
    factoryOf(::GetQuestsUseCase)
    factoryOf(::GetCoinsUseCase)
    factoryOf(::LogoutUseCase)
    factoryOf(::AddFavoriteFriendUseCase)
    factoryOf(::RemoveFavoriteFriendUseCase)
    factoryOf(::GetUserStatisticUseCase)
}