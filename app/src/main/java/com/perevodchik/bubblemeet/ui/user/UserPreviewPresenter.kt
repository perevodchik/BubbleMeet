package com.perevodchik.bubblemeet.ui.user

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.data.model.Favorite
import com.perevodchik.bubblemeet.data.model.UserData
import com.perevodchik.bubblemeet.ui.mainmenu.presenter.LikesPresenter
import com.perevodchik.bubblemeet.util.Api
import com.perevodchik.bubblemeet.util.UserInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

class UserPreviewPresenter(_ctx: UserPreviewFragment) {
    private val context: UserPreviewFragment = _ctx
    private val api: Api = Api()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun isUserInFavorite(userData: UserData): Boolean {
        var isFavoriteUser = false
        Log.d("user ->", "start check")
        for(u in UserInstance.userLikes) {
            if(u.id_favorite == userData.id_favorite || u.id == userData.id) {
                isFavoriteUser = true
                Log.d("user -> $isFavoriteUser", u.toString())
                break
            }
        }
        return isFavoriteUser
    }

    fun openFullProfileIfCan(userData: UserData, fm: FragmentManager) {
        compositeDisposable
            .add(api.getViewCount().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<ResponseBody>() {
                    override fun onSuccess(r: ResponseBody) {
                        val json = Gson().fromJson(r.string(), JsonObject::class.java)
                        val count = json.get("countViews").asInt

                        if(count == count) {
                            val transaction = fm.beginTransaction()
                            val fragment = UserFullFragment(userData)
                            transaction.replace(R.id.container, fragment)
                            transaction.addToBackStack(null)
                            transaction.commit()

                            /*compositeDisposable
                                .add(api.setViewCount(count - 1).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeWith(object: DisposableSingleObserver<ResponseBody>() {
                                        override fun onSuccess(r: ResponseBody) {
                                            println(r.string())
                                        }
                                        override fun onError(e: Throwable) {
                                            Toast.makeText(context.context, e.localizedMessage, Toast.LENGTH_LONG).show()
                                        }
                                    }))*/
                        } else {
                            Toast.makeText(context.context, "View count == 0, cannot open full profile", Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onError(e: Throwable) {
                        Toast.makeText(context.context, e.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }))
    }

    fun addFavorite(userData: UserData) {
        compositeDisposable
            .add(api.addFavorite(userData.id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<ResponseBody>() {
                    override fun onSuccess(r: ResponseBody) {
                        val json = Gson().fromJson(r.string(), JsonObject::class.java)
                        for(u in UserInstance.userLikes) {
                            if(u.id == userData.id)
                                return
                        }
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

    fun addTemporary(temporaryId: Int) {
        compositeDisposable
            .add(api.addTemporary(temporaryId).subscribeOn(Schedulers.io())
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

    fun getTemporary() {
        compositeDisposable
            .add(api.getTemporary().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<Response<List<UserData>>>() {
                    override fun onSuccess(r: Response<List<UserData>>) {
                        context.setUsers(r.body()!!)
                    }
                    override fun onError(e: Throwable) {
                        Toast.makeText(context.context, e.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }))
    }
}