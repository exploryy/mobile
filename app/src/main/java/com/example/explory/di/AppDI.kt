package com.example.explory.di

import com.example.explory.presentation.screen.auth.login.LoginViewModel
import com.example.explory.presentation.screen.auth.register.RegistrationViewModel
import com.example.explory.presentation.screen.common.ThemeViewModel
import com.example.explory.presentation.screen.friendprofile.FriendProfileViewModel
import com.example.explory.presentation.screen.friends.FriendViewModel
import com.example.explory.presentation.screen.inventory.InventoryViewModel
import com.example.explory.presentation.screen.map.MapViewModel
import com.example.explory.presentation.screen.profile.ProfileViewModel
import com.example.explory.presentation.screen.quest.QuestViewModel
import com.example.explory.presentation.screen.requests.FriendRequestsViewModel
import com.example.explory.presentation.screen.shop.ShopViewModel
import com.example.explory.presentation.screen.splash.SplashViewModel
import com.example.explory.presentation.screen.userstatistic.UserStatisticViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

fun appModule() = module {
    viewModelOf(::MapViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::SplashViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::FriendViewModel)
    viewModelOf(::FriendRequestsViewModel)
    viewModelOf(::UserStatisticViewModel)
    viewModelOf(::QuestViewModel)
    single { ThemeViewModel(get()) }
    viewModelOf(::FriendProfileViewModel)
    viewModelOf(::ShopViewModel)
    viewModelOf(::InventoryViewModel)
}