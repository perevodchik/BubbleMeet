package com.perevodchik.bubblemeet.ui.mainmenu.inbox

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.util.UserInstance
import com.perevodchik.bubblemeet.data.model.Message
import com.perevodchik.bubblemeet.ui.mainmenu.MainActivity
import java.sql.Timestamp

class ChatFragment(_id: Int): Fragment() {
    private val userId: Int = _id
    private lateinit var messages: ListView
    private lateinit var adapter: MessageAdapter
    private var presenter: ChatPresenter = ChatPresenter(this, userId)
    private lateinit var sendMessageBtn: ImageView
    private lateinit var messageText: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sendMessageBtn = view.findViewById(R.id.send_message)
        messageText = view.findViewById(R.id.input_message_text)

        sendMessageBtn.setOnClickListener {
            val msgText = messageText.text.toString()
            presenter.sendMessage(userId, msgText)
            adapter.addMessage(Message(from = UserInstance.profile?.id, to = userId, message = msgText, createdAt = Timestamp(System.currentTimeMillis()), id = null))
        }

        messages = view.findViewById(R.id.message_list)
        messages.transcriptMode = AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL
        adapter = MessageAdapter(activity as MainActivity)
        messages.adapter = adapter
    }

    fun setMessages(_list: List<Message>) {
        adapter.setMessages(_list)
    }
}