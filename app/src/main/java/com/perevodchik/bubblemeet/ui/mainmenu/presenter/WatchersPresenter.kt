package com.perevodchik.bubblemeet.ui.mainmenu.presenter

import com.perevodchik.bubblemeet.data.model.UserData
import com.perevodchik.bubblemeet.ui.mainmenu.fragment.WatchersFragment
import com.perevodchik.bubblemeet.util.Api
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class WatchersPresenter(_ctx: WatchersFragment) {
    private val context = _ctx
    private val api: Api = Api(context.context)
    private val disposable: CompositeDisposable = CompositeDisposable()

    init {
        fetchHistory()
    }

    internal fun fetchHistory() {
        disposable.add(
            api.getWatchers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<Response<List<UserData>>>() {
                    override fun onSuccess(t: Response<List<UserData>>) {
                        context.setWatchers(t.body()!!)
                        disposable.clear()
                    }

                    override fun onError(e: Throwable) {
                    }
                }))
    }
}