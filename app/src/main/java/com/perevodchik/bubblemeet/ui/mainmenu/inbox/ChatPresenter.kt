package com.perevodchik.bubblemeet.ui.mainmenu.inbox

import android.text.Editable
import android.text.SpannableStringBuilder
import android.widget.EditText
import android.widget.Toast
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.data.model.Message
import com.perevodchik.bubblemeet.util.Api
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_chat.view.*
import okhttp3.ResponseBody
import retrofit2.Response

class ChatPresenter(_ctx: ChatFragment, _id: Int) {
    private val context = _ctx
    private val disposable: CompositeDisposable = CompositeDisposable()
    private val api: Api = Api()
    private val id: Int = _id

    init {
        fetchMessages()
    }

    private fun fetchMessages() {
        disposable.addAll(
            api.getMessages(id)
                .subscribeOn(
                    Schedulers.io())
                .observeOn(
                    AndroidSchedulers.mainThread())
                .subscribeWith(
                    object: DisposableSingleObserver<Response<List<Message>>>() {
                        override fun onSuccess(r: Response<List<Message>>) {
                            context.setMessages(r.body()!!)
                        }
                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                            Toast.makeText(context.context, e.localizedMessage, Toast.LENGTH_LONG).show()
                        }
                    }))
    }

    fun sendMessage(id: Int, message: String) {
        disposable.addAll(
            api.sendMessage(id, message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<ResponseBody>() {
                    override fun onSuccess(r: ResponseBody) {
                        println(r.string())
                        context.activity?.findViewById<EditText>(R.id.input_message_text)?.text = SpannableStringBuilder("")
                    }

                    override fun onError(e: Throwable) {
                        Toast.makeText(context.context, e.localizedMessage, Toast.LENGTH_LONG).show()
                    }

                })
        )
    }
}