package com.perevodchik.bubblemeet.ui.mainmenu.presenter

import android.widget.Toast
import com.perevodchik.bubblemeet.data.model.FavoriteUser
import com.perevodchik.bubblemeet.data.model.UserData
import com.perevodchik.bubblemeet.ui.mainmenu.fragment.LikesFragment
import com.perevodchik.bubblemeet.util.Api
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class LikesPresenter(_ctx: LikesFragment) {
    private val context = _ctx
    private val api: Api = Api(context.context)
    private val disposable: CompositeDisposable = CompositeDisposable()

    init {
        fetchLikes()
    }

    internal fun fetchLikes() {
        disposable.addAll(
            api.getUserFavorite()
                .subscribeOn(
                    Schedulers.io())
                .observeOn(
                    AndroidSchedulers.mainThread())
                .subscribeWith(
                    object: DisposableSingleObserver<Response<List<UserData>>>() {
                        override fun onSuccess(r: Response<List<UserData>>) {
                            context.setLikes(r.body() ?: listOf())
                        }
                        override fun onError(e: Throwable) {
                            Toast.makeText(context.context, e.localizedMessage, Toast.LENGTH_LONG).show()
                        }
        }))

    }

}