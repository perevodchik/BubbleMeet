package com.perevodchik.bubblemeet.ui.login

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.perevodchik.bubblemeet.util.Presenter
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.util.UserInstance
import com.perevodchik.bubblemeet.ui.mainmenu.MainActivity
import com.perevodchik.bubblemeet.util.Api
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

class LoginPresenter(_ctx: LoginActivity): View.OnClickListener,
    Presenter {

    private val context: LoginActivity = _ctx
    private val api: Api = Api(context)
    private var email: EditText
    private var pass: EditText
    private var compositeDisposable: CompositeDisposable
    private var editor: SharedPreferences

    init {
        email = context.findViewById(R.id.email)
        pass = context.findViewById(R.id.password)
        compositeDisposable = CompositeDisposable()
        editor = context.getSharedPreferences("BubbleMeet", MODE_PRIVATE)
    }

    override fun onClick(v: View) {
        if(v.id == R.id.login) {
            login()
        }
    }

    private fun login() {
        compositeDisposable.add(api.login(email = email.text.toString(), password = pass.text.toString()).subscribeOn(
            Schedulers.io()
        ).observeOn(
            AndroidSchedulers.mainThread()
        ).subscribeWith(object : DisposableSingleObserver<Response<ResponseBody>>() {
            override fun onSuccess(response: Response<ResponseBody>) {
                if(response.code() == 200 && response.body() != null) {
                    val e = editor.edit()
                    val string = response.body()?.string()
                    val json = Gson().fromJson(string, JsonObject::class.java)
                    UserInstance.session = response.headers().get("Set-Cookie")

                    e.putString("session", UserInstance.session ?: response.headers().get("Set-Cookie"))
                    e.putString("password", pass.text.toString())
                    e.putString(NotificationCompat.CATEGORY_EMAIL, email.text.toString())
                    e.putInt("id", Integer.valueOf(json.get("id").toString()))
                    e.apply()

                    context.startActivity(Intent(context, MainActivity::class.java))
                    context.finish()
                    compositeDisposable.clear()
                } else {
                    context.startActivity(Intent(context, LoginActivity::class.java))
                    Toast.makeText(context, context.resources.getString(R.string.user_not_exist), Toast.LENGTH_LONG).show()
                }
            }

            override fun onError(e: Throwable) {
                if(e is HttpException)
                    Toast.makeText(context, e.response()?.errorBody()?.string() ?: "", Toast.LENGTH_LONG).show()
            }

        })
        )
    }

    override fun showMessage(t: Toast) {
    }
}