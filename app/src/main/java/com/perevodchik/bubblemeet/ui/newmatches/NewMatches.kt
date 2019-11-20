package com.perevodchik.bubblemeet.ui.newmatches

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.util.UserInstance
import com.perevodchik.bubblemeet.data.model.UserData
import com.perevodchik.bubblemeet.ui.mainmenu.MainActivity
import com.perevodchik.bubblemeet.util.Api
import com.perevodchik.bubblemeet.util.Values
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

class NewMatches(_ctx: Context, _list: MutableList<UserData>): Fragment() {
    private val ctx = _ctx
    private val list: MutableList<UserData> = _list
    private val api: Api = Api()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var currentData: UserData
    private lateinit var avatar0: ImageView
    private lateinit var avatar1: ImageView
    private lateinit var toggle: ImageView
    private lateinit var sendBtn: ImageView
    private lateinit var messageText: EditText

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_new_matches, container, false)

        toggle = activity!!.findViewById(R.id.toggle)
        toggle.visibility = View.VISIBLE
        toggle.setImageDrawable(resources.getDrawable(R.drawable.ic_back, null))
        toggle.setOnClickListener { nextUser() }

        messageText = v.findViewById(R.id.first_message_text)

        sendBtn = v.findViewById(R.id.send_first_message)
        sendBtn.setOnClickListener {
            compositeDisposable.add(api.sendMessage(currentData.id, messageText.text.toString())
                .subscribeOn(
                    Schedulers.io())
                .observeOn(
                    AndroidSchedulers.mainThread())
                .subscribeWith(
                    object: DisposableSingleObserver<ResponseBody>() {
                    override fun onSuccess(r: ResponseBody) {
                        println(r.string())
                    }
                    override fun onError(e: Throwable) {
                        Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }))
        }

        avatar0 = v.findViewById(R.id.match_image0)
        avatar1 = v.findViewById(R.id.match_image1)

        nextUser()
        return v
    }

    override fun onStop() {
        toggle.visibility = View.INVISIBLE
        super.onStop()
    }

    private fun nextUser() {
        if(list.size < 1)
            (ctx as MainActivity).supportFragmentManager.popBackStack()
        else {
            currentData = list[list.size - 1]
            sendViewed()
            list.remove(currentData)
            setUIData()
        }
    }

    private fun sendViewed() {
        compositeDisposable
            .add(api.sendViewed(currentData.id_favorite).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ResponseBody>() {
                    override fun onSuccess(r: ResponseBody) {
                        println(r.string())
                    }

                    override fun onError(e: Throwable) {
                        Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                })
            )
    }

    private fun setUIData() {
        loadImg("${Values.imgUrl}/${UserInstance.profile?.avatarFull}", avatar0)
        loadImg("${Values.imgUrl}/${currentData.avatarFull}" , avatar1)
    }

    private fun loadImg(path: String, avatar: ImageView) {
        try {
            Picasso.with(ctx).load(path).placeholder(R.drawable.preview_user_avatar_background).into(avatar)
        } catch(e: IllegalArgumentException) { }
    }

}