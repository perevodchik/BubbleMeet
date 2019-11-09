package com.perevodchik.bubblemeet.ui.preload

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.util.UserInstance
import com.perevodchik.bubblemeet.ui.login.LoginActivity
import com.perevodchik.bubblemeet.ui.mainmenu.MainActivity
import com.perevodchik.bubblemeet.util.Api
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response

class PreloadPresenter(_ctx: Context) {
    private val context: Context = _ctx
    private val api: Api
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        api = Api(context)
        checkUserData()
    }

    private fun checkUserData() {
        val s = context.getSharedPreferences("BubbleMeet", Context.MODE_PRIVATE)
        if(s.contains(NotificationCompat.CATEGORY_EMAIL) && s.contains("password")) {

            compositeDisposable.add(api.login(email = s.getString(NotificationCompat.CATEGORY_EMAIL, "")!!, password = s.getString("password", "")!!).subscribeOn(
                Schedulers.io()
            ).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribeWith(object : DisposableSingleObserver<Response<ResponseBody>>() {
                override fun onSuccess(response: Response<ResponseBody>) {
                    if(response.code() == 200 && response.body() != null) {
                        UserInstance.session = response.headers().get("Set-Cookie")
                        val editor = s.edit()

                        editor?.putString("session", UserInstance.session ?: response.headers().get("Set-Cookie"))
                        editor?.apply()

                        context.startActivity(Intent(context, MainActivity::class.java))
                        compositeDisposable.clear()
                    } else {
                        Toast.makeText(context, context.resources.getString(R.string.user_not_exist), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onError(e: Throwable) {
                        Toast.makeText(context, e.localizedMessage ?: "", Toast.LENGTH_LONG).show()
                }
            }))
        }
        else
            context.startActivity(Intent(context, LoginActivity::class.java))
    }

}