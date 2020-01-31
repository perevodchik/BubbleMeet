package com.perevodchik.bubblemeet.ui.user

import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.perevodchik.bubblemeet.data.model.UserData
import com.perevodchik.bubblemeet.util.Api
import com.perevodchik.bubblemeet.util.UserInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

class UserFullPresenter(_ctx: UserFullFragment) {
    private val context: UserFullFragment = _ctx
    private val api: Api = Api()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun addFavorite(userData: UserData) {
        compositeDisposable
            .add(api.addFavorite(userData.id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<ResponseBody>() {
                    override fun onSuccess(r: ResponseBody) {
                        val json = Gson().fromJson(r.string(), JsonObject::class.java)
                        userData.id_favorite = json.get("favorite").asJsonObject.get("id").asInt
                        UserInstance.userLikes.add(userData)
                    }
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        Toast.makeText(context.context, e.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }))
    }

    fun deleteFavorite(userData: UserData) {
        compositeDisposable
            .add(api.deleteFavorite(userData.id_favorite).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<ResponseBody>() {
                    override fun onSuccess(r: ResponseBody) {
                        for(u in UserInstance.userLikes) {
                            if(u.id == userData.id) {
                                UserInstance.userLikes.remove(u)
                                break
                            }
                        }
                        println(r.string())
                    }
                    override fun onError(e: Throwable) {
                        Toast.makeText(context.context, e.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }))
    }

    fun isUserInFavorite(userData: UserData): Boolean {
        var isFavoriteUser = false
        for(u in UserInstance.userLikes) {
            if(u.id_favorite == userData.id_favorite || u.id == userData.id) {
                isFavoriteUser = true
                break
            }
        }
        return isFavoriteUser
    }

    fun addHistory(historyId: Int) {
        compositeDisposable
            .add(api.addHistory(historyId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<ResponseBody>() {
                    override fun onSuccess(r: ResponseBody) {
                        println(r.string())
                    }
                    override fun onError(e: Throwable) {
                        Toast.makeText(context.context, e.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }))
    }

    fun sendInvite(email: String) {
        compositeDisposable
            .add(api.sendInvite(email).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<ResponseBody>() {
                    override fun onSuccess(r: ResponseBody) {
                        println(r.string())
                        compositeDisposable
                            .add(api.setViewCount(5).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(object: DisposableSingleObserver<ResponseBody>() {
                                    override fun onSuccess(r: ResponseBody) {
                                        println(r.string())
                                    }
                                    override fun onError(e: Throwable) {
                                        Toast.makeText(context.context, e.localizedMessage, Toast.LENGTH_LONG).show()
                                    }
                                }))
                    }
                    override fun onError(e: Throwable) {
                        Toast.makeText(context.context, e.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }))
    }

}