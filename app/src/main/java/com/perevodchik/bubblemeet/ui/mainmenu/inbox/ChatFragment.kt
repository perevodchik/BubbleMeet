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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.util.UserInstance
import com.perevodchik.bubblemeet.data.model.Message
import com.perevodchik.bubblemeet.data.model.UserData
import com.perevodchik.bubblemeet.ui.mainmenu.MainActivity
import com.perevodchik.bubblemeet.ui.user.UserPreviewFragment
import com.perevodchik.bubblemeet.util.Values
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.sql.Timestamp

class ChatFragment(_id: Int): Fragment() {
    private val userId: Int = _id
    private lateinit var userData: UserData
    private var presenter: ChatPresenter = ChatPresenter(this, userId)
    private val messagesList: MutableList<Message> = mutableListOf()
    private lateinit var messages: ListView
    private lateinit var adapter: MessageAdapter
    private lateinit var sendMessageBtn: ImageView
    private lateinit var messageText: TextView
    private lateinit var inboxNameText: TextView
    private lateinit var inboxAvatar: CircleImageView
    private lateinit var closeInbox: CircleImageView
    private lateinit var headerMain: ConstraintLayout
    private lateinit var headerInbox: ConstraintLayout

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for(u in UserInstance.allUsers) {
            if(u.id == userId) {
                userData = u
                break
            }
        }

        inboxNameText = view.findViewById(R.id.name_inbox)
        inboxAvatar = view.findViewById(R.id.profile_inbox_btn)
        closeInbox = view.findViewById(R.id.close_inbox_btn)

        try {
            Picasso.with(context).load("${Values.imgUrl}/${userData.avatarSmall}").into(inboxAvatar)
        } catch (ignored: Exception) {}
        inboxNameText.text = userData.name

        headerMain = activity!!.findViewById(R.id.top_menu)
        headerInbox = view.findViewById(R.id.inbox_header)
        headerMain.visibility = View.GONE
        headerInbox.visibility = View.VISIBLE

        sendMessageBtn = view.findViewById(R.id.send_message)
        messageText = view.findViewById(R.id.input_message_text)

        closeInbox.setOnClickListener {
            val fm = fragmentManager
            fm?.popBackStack()
            headerMain.visibility = View.VISIBLE
        }

        inboxAvatar.setOnClickListener {
            val fm = fragmentManager!!
            val fragment = UserPreviewFragment(userData, fm)
            fm.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
        }

        sendMessageBtn.setOnClickListener {
            val msgText = messageText.text.toString()
            presenter.sendMessage(userId, msgText)
            adapter.addMessage(Message(from = UserInstance.profile.id, to = userId, message = msgText, createdAt = Timestamp(System.currentTimeMillis()), id = null))
        }

        messages = view.findViewById(R.id.message_list)
        messages.transcriptMode = AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL
        adapter = MessageAdapter(activity as MainActivity)
        messages.adapter = adapter
    }


    override fun onStart() {
        super.onStart()
        if(messagesList.isNotEmpty())
            adapter.setMessages(messagesList)
        presenter.fetchMessages()
    }

    override fun onStop() {
        super.onStop()
        headerMain.visibility = View.VISIBLE
    }

    fun setMessages(_list: List<Message>) {
        messagesList.clear()
        messagesList.addAll(_list)
        adapter.setMessages(_list)
    }
}