package com.perevodchik.bubblemeet.ui.mainmenu.bubble

import android.util.Log
import com.perevodchik.bubblemeet.data.model.UserData
import com.perevodchik.bubblemeet.util.Api
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class BubblePresenter {
    private lateinit var ctx: BubbleFragment
    private val api: Api = Api()
    private val disposable = CompositeDisposable()

    fun setContext(_ctx: BubbleFragment) {
        ctx = _ctx
    }

    fun getUsers(): List<UserData> {
        var list: List<UserData> = listOf()
        disposable.addAll(api.getUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<Response<List<UserData>>>() {
                override fun onSuccess(r: Response<List<UserData>>) {
                    list = r.body() ?: listOf()
                    Log.d("getUsers", r.message())
                    Log.d("getUsers", list.toString())
                    //ctx.setUsers(list)
                }

                override fun onError(e: Throwable) {
                    Log.d("getUsers", e.message ?: e.localizedMessage ?: "error")
                }

            }))

        return list
    }
}