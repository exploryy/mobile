package com.example.explory.data.service

import com.example.explory.data.model.battlepass.BattlePassDto
import com.example.explory.data.model.friend.FriendsResponse
import com.example.explory.data.model.inventory.CosmeticItemInInventoryDto
import com.example.explory.data.model.leaderboard.TotalStatisticDto
import com.example.explory.data.model.location.AreaDto
import com.example.explory.data.model.location.LocationStatisticDto
import com.example.explory.data.model.location.PolygonDto
import com.example.explory.data.model.note.CommonNoteDto
import com.example.explory.data.model.note.NoteDto
import com.example.explory.data.model.profile.FriendProfileDto
import com.example.explory.data.model.profile.ProfileDto
import com.example.explory.data.model.quest.DistanceQuestDto
import com.example.explory.data.model.quest.PointDto
import com.example.explory.data.model.quest.PointToPointQuestDto
import com.example.explory.data.model.quest.QuestDto
import com.example.explory.data.model.quest.QuestListDto
import com.example.explory.data.model.quest.RouteDto
import com.example.explory.data.model.quest.TransportType
import com.example.explory.data.model.requests.FriendRequest
import com.example.explory.data.model.shop.CosmeticItemInShopDto
import com.example.explory.data.model.statistic.AchievementDto
import com.example.explory.data.model.statistic.BalanceDto
import com.example.explory.data.model.statistic.CoinDto
import com.example.explory.data.model.statistic.Privacy
import com.example.explory.data.model.statistic.UserStatisticDto
import com.example.explory.presentation.screen.map.component.BuffResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("/user/register")
    suspend fun createUser(
        @Query("username") username: String,
        @Query("email") email: String,
        @Query("password") password: String
    )

    @GET("/user/profile")
    suspend fun getProfile(): ProfileDto

    @POST("/user/profile")
    suspend fun updateProfile(
        @Body body: RequestBody
    )

    @POST("/route")
    suspend fun createRoute(
        @Query("points") points: List<PointDto>,
        @Query("start_point_longitude") startPointLongitude: String,
        @Query("start_point_latitude") startPointLatitude: String
    )

    @GET("/route/{route_id}")
    suspend fun getRoute(
        @Path("route_id") routeId: String
    ): RouteDto

    // Quests
    @POST("/quest/{quest_id}/start")
    suspend fun startQuest(
        @Path("quest_id") questId: String,
        @Query("transport_type") transportType: TransportType
    )

    @POST("/quest/{quest_id}/review")
    suspend fun reviewQuest(
        @Path("quest_id") questId: String,
        @Query("rating") rating: Int,
        @Query("comment") comment: String,
        @Part images: List<MultipartBody.Part>
    )

    @GET("/quest/point_to_point/{quest_id}")
    suspend fun getPointToPointQuest(
        @Path("quest_id") questId: String
    ): PointToPointQuestDto

    @GET("/quest/my/completed")
    suspend fun getCompletedQuests(): List<QuestDto>

    @GET("/quest/my/active")
    suspend fun getActiveQuests(): List<QuestDto?>

    @POST("/quest/{quest_id}/review")
    suspend fun sendReview(
        @Path("quest_id") questId: String,
        @Body body: RequestBody
    )

    @GET("/quest/list")
    suspend fun getQuests(): QuestListDto

    @GET("/quest/distance/{quest_id}")
    suspend fun getDistanceQuest(
        @Path("quest_id") questId: String
    ): DistanceQuestDto


    @DELETE("/quest/{quest_id}/review")
    suspend fun deleteReview(
        @Path("quest_id") questId: String, @Query("review_id") reviewId: String
    )

    @POST("/quest/{quest_id}/review/{review_id}/image")
    suspend fun addReviewImage(
        @Path("quest_id") questId: String,
        @Path("review_id") reviewId: String,
        @Part image: MultipartBody.Part
    )

    @DELETE("/quest/{quest_id}/review/{review_id}/image")
    suspend fun deleteReviewImage(
        @Path("quest_id") questId: String,
        @Path("review_id") reviewId: String,
        @Query("image_id") imageId: String
    )

    @POST("/quest/{quest_id}/finish")
    suspend fun finishQuest(
        @Path("quest_id") questId: String
    )

    @DELETE("/quest/{quest_id}/cancel")
    suspend fun cancelQuest(
        @Path("quest_id") questId: String
    )

    // friend

    @POST("/friend/favorite")
    suspend fun addFavorite(
        @Query("friend_id") userId: String
    )

    @POST("/friend/add")
    suspend fun addFriend(
        @Query("friend_id") userId: String
    )

    @POST("/friend/accept")
    suspend fun acceptFriend(
        @Query("friend_id") userId: String
    )

    @GET("/friend/requests")
    suspend fun getFriendRequests(): FriendRequest

    @GET("/friend/list")
    suspend fun getFriends(): FriendsResponse

    @DELETE("/friend/unfavorite")
    suspend fun removeFavorite(
        @Query("friend_id") userId: String
    )

    @DELETE("/friend/remove")
    suspend fun removeFriend(
        @Query("friend_id") userId: String
    )

    @DELETE("/friend/decline")
    suspend fun declineFriend(
        @Query("friend_id") userId: String
    )

    // Achievement

    @GET("/achievement")
    suspend fun getAchievements(): List<AchievementDto>

    // Polygons

    @GET("/multipolygon")
    suspend fun getPolygons(): PolygonDto

    @GET("/multipolygon/area")
    suspend fun getPolygonArea(): AreaDto

    @GET("/multipolygon/friend")
    suspend fun getFriendPolygons(
        @Query("friend_id") friendId: String
    ): PolygonDto

    // Coin

    @GET("/coin/list")
    suspend fun getCoins(): List<CoinDto>

    @Multipart
    @PATCH("/coin/consume")
    suspend fun consumeCoin(
        @Part("coin_id") coinId: Long
    )

    @GET("/coin/balance")
    suspend fun getBalance(): BalanceDto

    @GET("/user")
    suspend fun getUserList(
        @Query("username") username: String
    ): List<ProfileDto>

    @GET("/statistic/my")
    suspend fun getUserStatistic(): UserStatisticDto

    @GET("/statistic/friend/{client_id}")
    suspend fun getFriendProfile(@Path("client_id") clientId: String): FriendProfileDto

    @GET("/statistic/friend/coordinates")
    suspend fun getFriendStatistic(): List<LocationStatisticDto>

    @GET("/shop")
    suspend fun getShop(): List<CosmeticItemInShopDto>

    @POST("/shop/{item_id}/buy")
    suspend fun buyItem(@Path("item_id") itemId: Long)

    // Inventory
    @POST("/inventory/{item_id}/equip")
    suspend fun equipItem(@Path("item_id") itemId: Long)

    @POST("/inventory/{item_id}/unequip")
    suspend fun unEquipItem(@Path("item_id") itemId: Long)

    @GET("/inventory")
    suspend fun getInventory(): List<CosmeticItemInInventoryDto>

    @DELETE("/inventory/{item_id}/sell")
    suspend fun sellItem(@Path("item_id") itemId: Long)

    // Battle pass
    @GET("/battle_pass/current")
    suspend fun getCurrentBattlePass(): BattlePassDto

    // Buffs
    @GET("/buffs/available")
    suspend fun getBuffList(): List<BuffResponse>

    @GET("/buffs/my")
    suspend fun getMyBuffs(): List<BuffResponse>

    @Multipart
    @POST("/buffs/apply")
    suspend fun applyBuff(
        @Part("buff_id") buffId: Long
    )
    // Note
    @GET("/note/all")
    suspend fun getAllNotes(): List<CommonNoteDto>

    @GET("/note/{note_id}")
    suspend fun getNote(@Path("note_id") noteId: Long): NoteDto

    @POST("/note")
    suspend fun createNote(@Body body: RequestBody)

    //leaderboard
    @GET("/statistic/top/distance")
    suspend fun getDistanceStatistic(@Query("count") count: Int) : TotalStatisticDto

    @GET("/statistic/top/level")
    suspend fun getExperienceStatistic(@Query("count") count: Int): TotalStatisticDto

    // Privacy
    @PATCH("/privacy")
    suspend fun setPrivacy(@Query("isPublic") isPublic: Boolean)

    @GET("/privacy")
    suspend fun getPrivacy() : Privacy
}


