package com.perevodchik.bubblemeet.ui.mainmenu.bubble

import android.util.Log
import com.perevodchik.bubblemeet.data.model.UserData
import com.perevodchik.bubblemeet.util.Api
import com.perevodchik.bubblemeet.util.Location
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import com.perevodchik.bubblemeet.util.Math
import com.perevodchik.bubblemeet.util.UserInstance

class BubblePresenter(_ctx: BubbleFragment) {
    private var ctx: BubbleFragment = _ctx
    private val api: Api = Api()
    private val disposable = CompositeDisposable()

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

    fun filterByGender(list: MutableList<UserData>, value: String): MutableList<UserData> {
        if(value.equals("null", true))
            return list

        val l = mutableListOf<UserData>()
        val iterator = list.iterator()
        while(iterator.hasNext()) {
            val n = iterator.next()
            if(!n.gender.equals(value, true)) {
                iterator.remove()
            }
                //l.add(n)
        }
        return l
    }

    fun filterByAge(list: MutableList<UserData>, value: String): MutableList<UserData> {
        if(value.equals("null", true))
            return list

        val iterator = list.iterator()

        if(value.contains("more", true)) {
            val val0 = value.split("-")[0].toInt()
            while(iterator.hasNext()) {
                val n = iterator.next()
                if(n.age < val0)
                    iterator.remove()
            }
        } else {
            val arr = value.split("-")
            val val0 = arr[0].toInt()
            val val1 = arr[1].toInt()

            while(iterator.hasNext()) {
                val n = iterator.next()
                if(n.age !in val0..val1)
                    iterator.remove()
            }
        }

        return list
    }

    fun filterByLocation(list: MutableList<UserData>, value: String): MutableList<UserData> {
        if(value.contains("all", true) || value.contains("area", true) || value.equals("null", true))
            return list

        val c = Location.getCoordinates()
        if (c != null)
            UserInstance.profile.location = "${c.latitude},${c.longitude}"

        val iterator = list.iterator()
        val arr = value.split(" ")
        val val0 = arr[0].toInt()
        val val1 = arr[1]
        val arr1 = UserInstance.profile.location.split(',')
        val userCoord0 = arr1[0].toDouble()
        val userCoord1 = arr1[1].toDouble()

        while(iterator.hasNext()) {
            val n = iterator.next()
            val arr2 = n.location.split(',')
            val c0 = arr2[0].toDouble()
            val c1 = arr2[1].toDouble()
            
            if(Math.differenceBetween(c0, c1, userCoord0, userCoord1, if(val1.equals("km", true)) 'K' else 'N') > val0)
                iterator.remove()
        }

        return list
    }

    fun filterByEyeColor(list: MutableList<UserData>, value: String): MutableList<UserData> {
        if(value.contains("Spectrum", true) || value.contains("color", true) || value.equals("null", true))
            return list

        val iterator = list.iterator()

        while(iterator.hasNext()) {
            val n = iterator.next()
            if(!n.gender.contains(value, true))
                iterator.remove()
        }
        return list
    }

    fun filterByHeight(list: MutableList<UserData>, value: String): MutableList<UserData> {
        if(value.contains("no", true) || value.contains("matter", true) || value.equals("null", true))
            return list

        val iterator = list.iterator()
        val val0 = value.toInt()

        while(iterator.hasNext()) {
            val n = iterator.next()
            if(n.height < val0)
                iterator.remove()
        }

        return list
    }

    fun filterBySmoking(list: MutableList<UserData>, value: String): MutableList<UserData> {
        if(value.equals("null", true))
            return list

        val iterator = list.iterator()
        val val0 = if(value.equals("yes", true)) 1 else if(value.equals("no", true)) 0 else -1

        if(val0 == -1)
            return list

        while(iterator.hasNext()) {
            val n = iterator.next()
            if(n.smoking != val0)
                iterator.remove()
        }

        return list
    }

    fun filterByMarried(list: MutableList<UserData>, value: String): MutableList<UserData> {
        if(value.equals("null", true))
            return list

        val iterator = list.iterator()
        val val0 = if(value.equals("yes", true)) 1 else if(value.equals("no", true)) 0 else -1

        if(val0 == -1)
            return list

        while(iterator.hasNext()) {
            val n = iterator.next()
            if(n.marred != val0)
                iterator.remove()
        }

        return list
    }

    fun filterByChildren(list: MutableList<UserData>, value: String): MutableList<UserData> {
        if(value.equals("null", true))
            return list

        val iterator = list.iterator()
        val val0 = if(value.equals("yes", true)) 1 else if(value.equals("no", true)) 0 else -1

        if(val0 == -1)
            return list

        while(iterator.hasNext()) {
            val n = iterator.next()
            if(n.children != val0)
                iterator.remove()
        }

        return list
    }

    fun filterByLoveToCook(list: MutableList<UserData>, value: String): MutableList<UserData> {
        if(value.equals("null", true))
            return list

        val iterator = list.iterator()
        val val0 = if(value.equals("yes", true)) 1 else if(value.equals("no", true)) 0 else -1

        if(val0 == -1)
            return list

        while(iterator.hasNext()) {
            val n = iterator.next()
            if(n.cook != val0)
                iterator.remove()
        }

        return list
    }

    fun filterByLookingFor(list: MutableList<UserData>, value: String): MutableList<UserData> {
        if(value.equals("null", true))
            return list

        val iterator = list.iterator()

        while(iterator.hasNext()) {
            val n = iterator.next()
            if(!n.looking.equals(value, true))
                iterator.remove()
        }

        return list
    }


}