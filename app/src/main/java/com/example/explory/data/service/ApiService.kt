package com.example.explory.data.service

import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.OffsetDateTime

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
        @Query("username") username: String?,
        @Query("email") email: String?,
        @Query("password") password: String?,
        @Part avatar: MultipartBody.Part?
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

    @POST("/quest/{quest_id}/start")
    suspend fun startQuest(
        @Path("quest_id") questId: String
    )

    @POST("/quest/{quest_id}/review")
    suspend fun reviewQuest(
        @Path("quest_id") questId: String,
        @Query("rating") rating: Int,
        @Query("comment") comment: String,
        @Part images: List<MultipartBody.Part>
    )

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

    @DELETE("/friend/accept")
    suspend fun acceptFriend(
        @Query("friend_id") userId: String
    )

    @GET("/friend/requests")
    suspend fun getFriendRequests(): RequestsResponse

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


}

class PolygonDto(
    val type: String, val features: List<FeatureResponse>
)

class FeatureResponse(
    val type: String, val properties: Map<String, String>, val geometry: GeometryResponse
)

class GeometryResponse(
    val type: String, val coordinates: List<List<List<List<Double>>>>
)


class AreaDto(
    val area: Double
)

class AchievementDto(
    val achievementId: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val isCompleted: Boolean,
    val completionDate: OffsetDateTime
)

class FriendsResponse(
    val friends: List<ProfileDto>, val favoriteFriends: List<ProfileDto>
)

class RequestsResponse(
    val my: List<ProfileDto>, val other: List<ProfileDto>
)

class RouteDto(
    val points: List<PointDto>, val distance: Long, val routeId: Long
)

class PointDto(
    val longitude: String, val latitude: String, val nextLongitude: String, val nextLatitude: String
)

data class ProfileDto(
    val userId: String, val username: String, val email: String, val avatarUrl: String
)


