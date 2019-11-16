package com.perevodchik.bubblemeet.ui.mainmenu.presenter

import android.util.Log
import android.widget.Toast
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.data.model.UserData
import com.perevodchik.bubblemeet.ui.mainmenu.fragment.MatchesFragment
import com.perevodchik.bubblemeet.ui.newmatches.NewMatches
import com.perevodchik.bubblemeet.util.Api
import com.perevodchik.bubblemeet.util.ListUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class MatchesPresenter(_ctx: MatchesFragment) {
    private val context = _ctx
    private val api: Api = Api(context.context)
    private val disposable: CompositeDisposable = CompositeDisposable()
    private val matches = mutableListOf<UserData>()

    fun fetchMatches() {
        var likeUser: List<UserData>
        var favorite: List<UserData>
        val list: MutableList<UserData> = mutableListOf()

        // страшно =)
        disposable.addAll(
            api.getFavorite()
                .subscribeOn(
                    Schedulers.io())
                .observeOn(
                    AndroidSchedulers.mainThread())
                .subscribeWith(
                    object: DisposableSingleObserver<Response<List<UserData>>>() {
                        override fun onSuccess(r: Response<List<UserData>>) {
                            favorite = ListUtil.removeDuplicates(r.body() ?: mutableListOf())

                            disposable.addAll(
                                api.getUserFavorite()
                                    .subscribeOn(
                                        Schedulers.io())
                                    .observeOn(
                                        AndroidSchedulers.mainThread())
                                    .subscribeWith(
                                        object: DisposableSingleObserver<Response<List<UserData>>>() {
                                            override fun onSuccess(r: Response<List<UserData>>) {
                                                likeUser = ListUtil.removeDuplicates(r.body() ?: mutableListOf())
                                                list.addAll(filterList(likeUser, favorite))

                                                context.setMatchers(list)

                                                for(u in list)
                                                    if(u.isViewed == 0 && u.showWindow == 1)
                                                        matches.add(u)

                                                if(matches.isEmpty()) {
                                                    Log.d("matches", "empty")
                                                    return
                                                }

                                                context.fragmentManager?.beginTransaction()
                                                    ?.replace(R.id.container, NewMatches(context.context!!, matches))
                                                    ?.addToBackStack(null)
                                                    ?.commit()
                                            }
                                            override fun onError(e: Throwable) {
                                                Toast.makeText(context.context, e.localizedMessage, Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    )
                            )
                        }
                        override fun onError(e: Throwable) {
                        }
                    }
                )
        )
    }

    private fun filterList(userLike: List<UserData>, favorite: List<UserData>): List<UserData> {
        val list: MutableList<UserData> = mutableListOf()

        for(u1 in favorite) {
            loop@ for(u2 in userLike) {
                if(u1.id == u2.id) {
                    list.add(u1)
                    break@loop
                }
            }
        }
        return list

    }
}