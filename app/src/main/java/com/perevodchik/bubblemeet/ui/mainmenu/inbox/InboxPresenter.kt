package com.perevodchik.bubblemeet.ui.mainmenu.inbox

import android.widget.Toast
import com.perevodchik.bubblemeet.data.model.ChatItem
import com.perevodchik.bubblemeet.util.Api
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class InboxPresenter(_ctx: InboxFragment) {
    private val context = _ctx
    private val disposable: CompositeDisposable = CompositeDisposable()
    private val api: Api = Api()

    init {
        fetchChats()
    }

    fun fetchChats() {
        disposable.addAll(
            api.getChats()
                .subscribeOn(
                    Schedulers.io())
                .observeOn(
                    AndroidSchedulers.mainThread())
                .subscribeWith(
                    object: DisposableSingleObserver<Response<List<ChatItem>>>() {
                        override fun onSuccess(r: Response<List<ChatItem>>) {
                            context.setChats(r.body()!!)
                        }
                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                            Toast.makeText(context.context, e.localizedMessage, Toast.LENGTH_LONG).show()
                        }
                    }))
    }
}