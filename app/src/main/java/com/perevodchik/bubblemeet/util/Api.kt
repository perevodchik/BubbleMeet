package com.perevodchik.bubblemeet.util

import android.content.Context
import android.util.Log
import com.perevodchik.bubblemeet.data.model.ChatItem
import com.perevodchik.bubblemeet.data.model.Message
import com.perevodchik.bubblemeet.data.model.Profile
import com.perevodchik.bubblemeet.data.model.UserData
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class Api() {
    private val BASE_URL = "http://185.25.116.211:11000/"
    private val r: Retrofit = getClient(BASE_URL)
    private val textPlain: String = "text/plain"
    private var context: Context? = null

    constructor(_ctx: Context?) : this() {
        this.context = _ctx
    }

    fun register(isFull: Boolean): Single<Response<ResponseBody>> {
        val api: IApi = r.create(IApi::class.java)
        val profile: Profile = UserInstance.profile
        val avatar =
                RequestBody.create(MediaType.parse("multipart/form-data"), UserInstance.userAvatar!!
            )

        if(isFull) {
            return api.registerFull(name = RequestBody.create(MediaType.parse(textPlain), profile.name),
                gender = RequestBody.create(MediaType.parse(textPlain), profile.gender),
                age = RequestBody.create(MediaType.parse(textPlain), profile.age.toString()),
                email = RequestBody.create(MediaType.parse(textPlain), profile.email),
                password = RequestBody.create(MediaType.parse(textPlain), UserInstance.password),
                avatarFull = MultipartBody.Part.createFormData("avatarFull", profile.login, avatar),
                avatarSmall = MultipartBody.Part.createFormData("avatarSmall", profile.login, avatar),
                height = RequestBody.create(MediaType.parse(textPlain), UserInstance.password),
                city = RequestBody.create(MediaType.parse(textPlain), UserInstance.password),
                cook = RequestBody.create(MediaType.parse(textPlain), UserInstance.password),
                login = RequestBody.create(MediaType.parse(textPlain), profile.login),
                smoking = RequestBody.create(MediaType.parse(textPlain), profile.smoking.toString()),
                marred = RequestBody.create(MediaType.parse(textPlain), profile.marred.toString()),
                looking = RequestBody.create(MediaType.parse(textPlain), profile.looking),
                hobbes = RequestBody.create(MediaType.parse(textPlain), profile.hobbes),
                children = RequestBody.create(MediaType.parse(textPlain), profile.children.toString()),
                location = RequestBody.create(MediaType.parse(textPlain), profile.location)
                )
        }
        else {
            return api.register(name = RequestBody.create(MediaType.parse(textPlain), profile.name),
                gender = RequestBody.create(MediaType.parse(textPlain), profile.gender),
                age = RequestBody.create(MediaType.parse(textPlain), profile.age.toString()),
                email = RequestBody.create(MediaType.parse(textPlain), profile.email),
                password = RequestBody.create(MediaType.parse(textPlain), UserInstance.password),
                avatarFull = MultipartBody.Part.createFormData("avatarFull", profile.login, avatar),
                avatarSmall = MultipartBody.Part.createFormData("avatarSmall", profile.login, avatar),
                login = RequestBody.create(MediaType.parse(textPlain), profile.login),
                city = RequestBody.create(MediaType.parse(textPlain), profile.city),
                location = RequestBody.create(MediaType.parse(textPlain), profile.location))
        }
    }

    fun login(email: String, password: String): Single<Response<ResponseBody>> {
        val api: IApi = r.create(IApi::class.java)
        return api.login(RequestBody.create(MediaType.parse(textPlain), email), RequestBody.create(
            MediaType.parse(textPlain), password))
    }

    fun getUser(): Single<Response<Profile>> {
        val api: IApi = r.create(IApi::class.java)
        val session = UserInstance.session ?: ""
        return api.getProfile(session)
    }

    fun addFavorite(favoriteId: Int): Single<ResponseBody> {
        val api: IApi = r.create(IApi::class.java)
        val session = UserInstance.session ?: ""
        return api.addFavourite(session, RequestBody.create(MediaType.parse(textPlain), favoriteId.toString()))
    }

    fun setUserFavorite(id: Int): Single<ResponseBody> {
        val api: IApi = r.create(IApi::class.java)
        val session = UserInstance.session ?: ""
        return api.setUserFavorite(session, RequestBody.create(MediaType.parse(textPlain), id.toString()))
    }

    fun getUserFavorite(): Single<Response<List<UserData>>> {
        val api: IApi = r.create(IApi::class.java)
        val session = UserInstance.session ?: ""
        return api.getUserFavorite(session)
    }

    fun getFavorite(): Single<Response<List<UserData>>> {
        val api: IApi = r.create(IApi::class.java)
        val session = UserInstance.session ?: ""
        return api.getFavourite(session)
    }

    fun addHistory(historyId: Int): Single<ResponseBody> {
        val api = r.create(IApi::class.java)
        val session = UserInstance.session ?: ""
        return api.addWatcher(session, RequestBody.create(MediaType.parse(textPlain), historyId.toString()))
    }

    fun getWatchers(): Single<Response<List<UserData>>> {
        val api: IApi = r.create(IApi::class.java)
        val session = UserInstance.session ?: ""
        return api.getWatchers(session)
    }

    fun addTemporary(temporaryId: Int): Single<ResponseBody> {
        val api: IApi = r.create(IApi::class.java)
        val session = UserInstance.session ?: ""
        return api.addTemporary(session, RequestBody.create(MediaType.parse(textPlain), temporaryId.toString()))
    }

    fun getTemporary(): Single<Response<List<UserData>>> {
        val api: IApi = r.create(IApi::class.java)
        val session = UserInstance.session ?: ""
        return api.getTemporary(session)
    }

    fun setViewCount(count: Int): Single<ResponseBody> {
        val api = r.create(IApi::class.java)
        val session = UserInstance.session ?: ""
        return api.setViewCount(session, RequestBody.create(MediaType.parse(textPlain), count.toString()))
    }

    fun getViewCount(): Single<ResponseBody> {
        val api = r.create(IApi::class.java)
        val session = UserInstance.session ?: ""
        return api.getViewCount(session)
    }

    fun sendInvite(email: String): Single<ResponseBody> {
        val api = r.create(IApi::class.java)
        val session = UserInstance.session ?: ""
        return api.sendInvite(session, RequestBody.create(MediaType.parse(textPlain), email))
    }

    fun sendMessage(id: Int, message: String): Single<ResponseBody> {
        val api = r.create(IApi::class.java)
        val session = UserInstance.session ?: ""
        return api.sendMessage(session,
            RequestBody.create(MediaType.parse(textPlain), id.toString()),
            RequestBody.create(MediaType.parse(textPlain), message))
    }

    fun sendViewed(id: Int): Single<ResponseBody> {
        val api: IApi = r.create(IApi::class.java)
        val session = UserInstance.session ?: ""
        return api.sendViewed(session, RequestBody.create(MediaType.parse(textPlain), id.toString()))
    }

    fun getChats(): Single<Response<List<ChatItem>>> {
        val api: IApi = r.create(IApi::class.java)
        val session = UserInstance.session ?: ""
        return api.getChats(session)
    }

    fun getMessages(to: Int): Single<Response<List<Message>>> {
        val api: IApi = r.create(IApi::class.java)
        val session = UserInstance.session ?: ""
        return api.getMessages(session, to.toString())
    }

    fun getUsers(): Single<Response<List<UserData>>>{
        val api: IApi = r.create(IApi::class.java)
        return api.getUsers()
    }

    fun addPhoto(photo: File): Single<ResponseBody> {
        val api: IApi = r.create(IApi::class.java)
        val session = UserInstance.session ?: ""
        val avatar = MultipartBody.Part.createFormData("file", UserInstance.profile.login, RequestBody.create(MediaType.parse("multipart/form-data"), photo))
        return api.addPhoto(session, avatar)
    }

    fun deleteFavorite(id: Int): Single<ResponseBody> {
        val api: IApi = r.create(IApi::class.java)
        return api.deleteFavorite(id.toString())
    }

    private fun getClient(baseUrl: String): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    }

}