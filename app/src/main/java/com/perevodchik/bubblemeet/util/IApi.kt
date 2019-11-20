package com.perevodchik.bubblemeet.util

import com.perevodchik.bubblemeet.data.model.*
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface IApi {

    /**
     * register with piece of the all fields
     */
    @POST("user")
    @Multipart
    fun register(
        @Part("name") name: RequestBody, @Part("gender") gender: RequestBody, @Part(
            "age"
        ) age: RequestBody, @Part("email") email: RequestBody, @Part("password") password: RequestBody, @Part avatarFull: MultipartBody.Part, @Part avatarSmall: MultipartBody.Part, @Part(
            "login"
        ) login: RequestBody, @Part("city") city: RequestBody, @Part("location") location: RequestBody
    ): Single<Response<ResponseBody>>

    /**
     * register with full user data fields
     */
    @POST("user")
    @Multipart
    fun registerFull(
        @Part("name") name: RequestBody, @Part("gender") gender: RequestBody, @Part(
            "age"
        ) age: RequestBody, @Part("email") email: RequestBody, @Part("password") password: RequestBody, @Part avatarFull: MultipartBody.Part, @Part avatarSmall: MultipartBody.Part, @Part(
            "height"
        ) height: RequestBody, @Part("smoking") smoking: RequestBody, @Part("marred") marred: RequestBody, @Part(
            "children"
        ) children: RequestBody, @Part("cook") cook: RequestBody, @Part("city") city: RequestBody, @Part(
            "looking"
        ) looking: RequestBody, @Part("hobbes") hobbes: RequestBody, @Part("login") login: RequestBody, @Part(
            "location"
        ) location: RequestBody
    ): Single<Response<ResponseBody>>

    /**
     * upload single photo to server
     */
    @POST("addImage")
    @Multipart
    fun addPhoto(@Header("Cookie") cookie: String, @Part photo: MultipartBody.Part): Single<ResponseBody>

    @POST("login")
    @Multipart
    fun login(@Part("email") email: RequestBody, @Part("password") password: RequestBody): Single<Response<ResponseBody>>

    @POST("chat")
    @Multipart
    fun sendMessage(@Header("Cookie") cookie: String, @Part("to") to: RequestBody, @Part("message") message: RequestBody): Single<ResponseBody>

    @POST("favorite")
    @Multipart
    fun addFavourite(@Header("Cookie") cookie: String, @Part("favorite") requestBody: RequestBody): Single<ResponseBody>

    @POST("temporary")
    @Multipart
    fun addTemporary(@Header("Cookie") cookie: String, @Part("favorite") requestBody: RequestBody): Single<ResponseBody>

    @POST("history")
    @Multipart
    fun addWatcher(@Header("Cookie") cookie: String, @Part("id") requestBody: RequestBody): Single<ResponseBody>

    @POST("viewCount")
    @Multipart
    fun setViewCount(@Header("Cookie") cookie: String, @Part("countViews") requestBody: RequestBody): Single<ResponseBody>

    @POST("userFavorite")
    @Multipart
    fun setUserFavorite(@Header("Cookie") cookie: String, @Part("id") requestBody: RequestBody): Single<ResponseBody>

    @POST("invite")
    @Multipart
    fun sendInvite(@Header("Cookie") cookie: String, @Part("email") requestBody: RequestBody): Single<ResponseBody>

    /**
     * get all users
     */
    @GET("allUser")
    fun getUsers(): Single<Response<List<UserData>>>

    @GET("chat")
    fun getMessages(@Header("Cookie") session: String, @Query("id") id: String): Single<Response<List<Message>>>

    @GET("userChar")
    fun getChats(@Header("Cookie") session: String): Single<Response<List<ChatItem>>>

    @GET("lastMessage")
    fun getUnreadMessages(@Header("Cookie") session: String): Single<Response<List<Message>>>

    @GET("temporary")
    fun getTemporary(@Header("Cookie") cookie: String): Single<Response<List<UserData>>>

    @GET("user")
    fun getProfile(@Header("Cookie") cookie: String): Single<Response<Profile>>

    @GET("userFavorite")
    fun getUserFavorite(@Header("Cookie") cookie: String): Single<Response<List<UserData>>>

    @GET("favorite")
    fun getFavourite(@Header("Cookie") cookie: String): Single<Response<List<UserData>>>

    @GET("history")
    fun getWatchers(@Header("Cookie") cookie: String): Single<Response<List<UserData>>>

    @GET("viewCount")
    fun getViewCount(@Header("Cookie") cookie: String): Single<ResponseBody>

    /**
     * set isView = 1
     */
    @PUT("viewed")
    @Multipart
    fun sendViewed(@Header("Cookie") cookie: String, @Part("id") requestBody: RequestBody): Single<ResponseBody>

    /**
     * edit the chat message by id
     */
    @PUT("chat")
    @Multipart
    fun editMessage(@Header("Cookie") session: String, @Part("id") id: RequestBody, @Part("message") message: RequestBody): Single<ResponseBody>

    /**
     * delete the chat message by id
     */
    @DELETE("chat")
    fun deleteMessage(@Header("Cookie") session: String, @Query("id") id: String): Single<ResponseBody>

    /**
     * delete the user favorite by id
     */
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "favorite", hasBody = true)
    fun deleteFavorite(@Field("id") id: String): Single<ResponseBody>
}