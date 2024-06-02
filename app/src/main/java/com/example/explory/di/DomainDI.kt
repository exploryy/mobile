package com.example.explory.di

import com.example.explory.domain.usecase.CheckUserTokenUseCase
import com.example.explory.domain.usecase.GetPolygonsUseCase
import com.example.explory.domain.usecase.GetProfileUseCase
import com.example.explory.domain.usecase.EditProfileUseCase
import com.example.explory.domain.usecase.GetFriendsUseCase
import com.example.explory.domain.usecase.GetUserTokenUseCase
import com.example.explory.domain.usecase.PostLoginUseCase
import com.example.explory.domain.usecase.PostRegistrationUseCase
import com.example.explory.domain.usecase.RefreshTokenUseCase
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
    factoryOf(com.example.explory.domain.usecase::GetQuestsUseCase)
    factoryOf(com.example.explory.domain.usecase::GetCoinsUseCase)
}