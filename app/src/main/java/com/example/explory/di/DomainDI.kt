package com.example.explory.di

import com.example.explory.domain.usecase.AcceptFriendUseCase
import com.example.explory.domain.usecase.AddFavoriteFriendUseCase
import com.example.explory.domain.usecase.AddFriendUseCase
import com.example.explory.domain.usecase.ApplyBuffUseCase
import com.example.explory.domain.usecase.BuyItemUseCase
import com.example.explory.domain.usecase.CancelQuestUseCase
import com.example.explory.domain.usecase.CheckUserTokenUseCase
import com.example.explory.domain.usecase.CollectCoinUseCase
import com.example.explory.domain.usecase.CreateNoteUseCase
import com.example.explory.domain.usecase.DeclineFriendUseCase
import com.example.explory.domain.usecase.EditProfileUseCase
import com.example.explory.domain.usecase.EquipItemUseCase
import com.example.explory.domain.usecase.GetAllNotesUseCase
import com.example.explory.domain.usecase.GetBalanceUseCase
import com.example.explory.domain.usecase.GetBuffListUseCase
import com.example.explory.domain.usecase.GetCoinsUseCase
import com.example.explory.domain.usecase.GetCurrentBattlePassUseCase
import com.example.explory.domain.usecase.GetDistanceQuestUseCase
import com.example.explory.domain.usecase.GetFriendPolygonUseCase
import com.example.explory.domain.usecase.GetFriendProfileUseCase
import com.example.explory.domain.usecase.GetFriendRequestsUseCase
import com.example.explory.domain.usecase.GetFriendStatisticUseCase
import com.example.explory.domain.usecase.GetFriendsUseCase
import com.example.explory.domain.usecase.GetInventoryUseCase
import com.example.explory.domain.usecase.GetNoteUseCase
import com.example.explory.domain.usecase.GetP2PQuestUseCase
import com.example.explory.domain.usecase.GetPolygonsUseCase
import com.example.explory.domain.usecase.GetPrivacyUseCase
import com.example.explory.domain.usecase.GetProfileUseCase
import com.example.explory.domain.usecase.GetQuestsUseCase
import com.example.explory.domain.usecase.GetShopUseCase
import com.example.explory.domain.usecase.GetUserListUseCase
import com.example.explory.domain.usecase.GetUserProfileUseCase
import com.example.explory.domain.usecase.GetUserStatisticUseCase
import com.example.explory.domain.usecase.GetUserTokenUseCase
import com.example.explory.domain.usecase.LogoutUseCase
import com.example.explory.domain.usecase.PostLoginUseCase
import com.example.explory.domain.usecase.PostRegistrationUseCase
import com.example.explory.domain.usecase.RefreshTokenUseCase
import com.example.explory.domain.usecase.RemoveFavoriteFriendUseCase
import com.example.explory.domain.usecase.RemoveFriendUseCase
import com.example.explory.domain.usecase.SellItemUseCase
import com.example.explory.domain.usecase.SendReviewUseCase
import com.example.explory.domain.usecase.SetPrivacyUseCase
import com.example.explory.domain.usecase.StartQuestUseCase
import com.example.explory.domain.usecase.UnEquipItemUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun domainModule() = module {
    factoryOf(::GetPolygonsUseCase)
    factoryOf(::PostLoginUseCase)
    factoryOf(::PostRegistrationUseCase)
    factoryOf(::CheckUserTokenUseCase)
    factoryOf(::GetUserTokenUseCase)
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
    factoryOf(::RemoveFriendUseCase)
    factoryOf(::GetFriendStatisticUseCase)
    factoryOf(::GetFriendProfileUseCase)
    factoryOf(::GetBalanceUseCase)
    factoryOf(::GetShopUseCase)
    factoryOf(::BuyItemUseCase)
    factoryOf(::EquipItemUseCase)
    factoryOf(::UnEquipItemUseCase)
    factoryOf(::GetInventoryUseCase)
    factoryOf(::BuyItemUseCase)
    factoryOf(::SellItemUseCase)
    factoryOf(::GetCurrentBattlePassUseCase)
    factoryOf(::CreateNoteUseCase)
    factoryOf(::GetNoteUseCase)
    factoryOf(::GetAllNotesUseCase)
    factoryOf(::SendReviewUseCase)
    factoryOf(::GetBuffListUseCase)
//    factoryOf(GetMyBuffsUseCase)
    factoryOf(::ApplyBuffUseCase)
    factoryOf(::GetUserProfileUseCase)
    factoryOf(::StartQuestUseCase)
    factoryOf(::CancelQuestUseCase)
    factoryOf(::GetP2PQuestUseCase)
    factoryOf(::GetDistanceQuestUseCase)
    factoryOf(::CollectCoinUseCase)
    factoryOf(::GetFriendPolygonUseCase)
    factoryOf(::GetPrivacyUseCase)
    factoryOf(::SetPrivacyUseCase)
}