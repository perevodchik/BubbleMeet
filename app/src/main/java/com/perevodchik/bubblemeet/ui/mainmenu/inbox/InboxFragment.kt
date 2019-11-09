package com.perevodchik.bubblemeet.ui.mainmenu.inbox

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.data.model.ChatItem
import com.perevodchik.bubblemeet.ui.mainmenu.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class InboxFragment: Fragment() {
    private lateinit var chatList: ListView
    private lateinit var adapter: ChatAdapter
    private lateinit var searchInbox: SearchView
    private var presenter: InboxPresenter = InboxPresenter(this)
    private var chatListInstance: List<ChatItem> = listOf()

    companion object {
        fun newInstance() = InboxFragment()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_inbox, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatList = view.findViewById(R.id.chats_list)
        adapter = ChatAdapter(_activity = activity as MainActivity?, _list = emptyList())
        chatList.adapter = adapter

        searchInbox = view.findViewById(R.id.inbox_search)

        val disposable = PublishSubject.create<String>()

        searchInbox.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                disposable.onComplete()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                disposable.onNext(newText ?: "")
                return true
            }
        })

        disposable.debounce(1, TimeUnit.SECONDS)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ item ->
                println(item)
                if(item.isEmpty())
                    adapter.setChats(chatListInstance)
                adapter.setChats(
                chatListInstance.filter { it.name.contains(item, true) || it.city.contains(item, true)}
                )
            }, { e -> println(e.message)})
    }

    override fun onStart() {
        super.onStart()
        presenter.fetchChats()
    }

    fun setChats(_list: List<ChatItem>) {
        chatListInstance = _list
        adapter.setChats(_list)
    }
}