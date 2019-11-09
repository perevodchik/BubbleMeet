package com.perevodchik.bubblemeet.util

import com.perevodchik.bubblemeet.data.model.ChatItem
import com.perevodchik.bubblemeet.data.model.Profile
import com.perevodchik.bubblemeet.data.model.UserData
import com.perevodchik.bubblemeet.data.model.Message
import io.reactivex.Single
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface IApi {

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
    fun getFavouriteByMe(@Header("Cookie") cookie: String): Single<Response<List<UserData>>>

    @GET("history")
    fun getWatchers(@Header("Cookie") cookie: String): Single<Response<List<UserData>>>

    @GET("viewCount")
    fun getViewCount(@Header("Cookie") cookie: String): Single<ResponseBody>
    
    @PUT("viewed")
    @Multipart
    fun sendViewed(@Header("Cookie") cookie: String, @Part("id") requestBody: RequestBody): Single<ResponseBody>

    @PUT("chat")
    @Multipart
    fun editMessage(@Header("Cookie") session: String, @Part("id") id: RequestBody, @Part("message") message: RequestBody): Single<ResponseBody>

    @DELETE("chat")
    @Multipart
    fun deleteMessage(@Header("Cookie") session: String, @Part("id") id: RequestBody): Single<ResponseBody>

    @GET("allUser")
    fun getUsers(): Single<Response<List<UserData>>>
}