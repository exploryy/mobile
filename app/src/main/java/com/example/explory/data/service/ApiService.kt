package com.example.explory.data.service

import com.example.explory.data.model.CoinDto
import com.example.explory.data.model.friend.FriendsResponse
import com.example.explory.data.model.location.AreaDto
import com.example.explory.data.model.location.LocationStatisticDto
import com.example.explory.data.model.location.PolygonDto
import com.example.explory.data.model.profile.ProfileDto
import com.example.explory.data.model.quest.DistanceQuestDto
import com.example.explory.data.model.quest.PointDto
import com.example.explory.data.model.quest.PointToPointQuestDto
import com.example.explory.data.model.quest.QuestDto
import com.example.explory.data.model.quest.QuestListDto
import com.example.explory.data.model.quest.RouteDto
import com.example.explory.data.model.requests.FriendRequest
import com.example.explory.data.model.statistic.AchievementDto
import com.example.explory.data.model.statistic.UserStatisticDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
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
    ): String

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
        @Query("transport_type") transportType: String
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

    @POST("/quest/{quest_id}/cancel")
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

    // Coin

    @GET("/coin/list")
    suspend fun getCoins(): List<CoinDto>

    @PATCH("/coin/consume")
    suspend fun consumeCoin(
        @Query("coin_id") coinId: Long
    )

    @GET("/user")
    suspend fun getUserList(
        @Query("username") username: String
    ): List<ProfileDto>

    @GET("/statistic/my")
    suspend fun getUserStatistic() : UserStatisticDto

    @GET("/statistic/friend/coordinates")
    suspend fun getFriendStatistic() : List<LocationStatisticDto>
}


