package com.perevodchik.bubblemeet.ui.register

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.ui.mainmenu.MainActivity
import com.perevodchik.bubblemeet.util.Api
import com.perevodchik.bubblemeet.util.UserInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

class RegisterPresenter(_ctx: Activity) {
    private val context = _ctx
    private val api: Api = Api()
    private val composite: CompositeDisposable = CompositeDisposable()
    private var editor: SharedPreferences

    init {
        editor = context.getSharedPreferences("BubbleMeet", Context.MODE_PRIVATE)
    }

    fun register(isFullRegister: Boolean = false) {
        composite.add(
            api.register(isFullRegister).subscribeOn(
                Schedulers.io()
            ).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribeWith(object : DisposableSingleObserver<Response<ResponseBody>>() {
                override fun onSuccess(response: Response<ResponseBody>) {
                    for(p in UserInstance.userPhotos) {
                        composite.add(
                            api.addPhoto(p).subscribeOn(
                                Schedulers.io()
                            ).observeOn(AndroidSchedulers.mainThread()
                            ).subscribeWith(object: DisposableSingleObserver<ResponseBody>() {
                                override fun onSuccess(t: ResponseBody) {
                                    Log.d("addPhoto", t.string())
                                }
                                override fun onError(e: Throwable) {
                                    Log.d("error -> ", e.localizedMessage ?: "error")
                                }
                            }
                            )
                        )
                    }
                    login()
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }

            })
        )
    }

    fun login() {
        composite.add(
            api.login(
                email = UserInstance.profile.email,
                password = UserInstance.password
            ).subscribeOn(
                Schedulers.io()
            ).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribeWith(object : DisposableSingleObserver<Response<ResponseBody>>() {
                override fun onSuccess(response: Response<ResponseBody>) {
                    if (response.code() == 200 && response.body() != null) {
                        val e = editor.edit()
                        val string = response.body()?.string()
                        val json = Gson().fromJson(string, JsonObject::class.java)
                        UserInstance.session = response.headers().get("Set-Cookie")
                        e.putString(
                            "session",
                            UserInstance.session ?: response.headers().get("Set-Cookie")
                        )
                        e.putString("password", UserInstance.password)
                        e.putString(NotificationCompat.CATEGORY_EMAIL, UserInstance.profile.email)
                        e.putInt("id", Integer.valueOf(json.get("id").toString()))
                        e.apply()

                        context.startActivity(Intent(context, MainActivity::class.java))
                        context.finish()
                        composite.clear()
                    } else {
                        Toast.makeText(
                            context,
                            context.resources.getString(R.string.user_not_exist),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onError(e: Throwable) {
                    if (e is HttpException)
                        Toast.makeText(
                            context,
                            e.response()?.errorBody()?.string() ?: "",
                            Toast.LENGTH_LONG
                        ).show()
                }

            })
        )
    }
}