package com.perevodchik.bubblemeet.ui.user

import android.widget.Toast
import com.perevodchik.bubblemeet.util.Api
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

class UserFullPresenter(_ctx: UserFullFragment) {
    private val context: UserFullFragment = _ctx
    private val api: Api = Api()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

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