package com.perevodchik.bubblemeet.ui.mainmenu.inbox

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.data.model.Message
import com.perevodchik.bubblemeet.ui.mainmenu.MainActivity

class MessageAdapter(_activity: MainActivity?): BaseAdapter() {
    private val messages: MutableList<Message> = mutableListOf()
    private var activity: MainActivity? = _activity
    private var inflater: LayoutInflater? = activity?.layoutInflater

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val message = convertView ?: inflater!!.inflate(R.layout.item_message, parent, false)
        val text = message.findViewById<TextView>(R.id.message_text)

        /*val params: RelativeLayout.LayoutParams =  RelativeLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        if(messages[position].to == UserInstance.profile?.id) {
            params.addRule(RelativeLayout.END_OF, R.id.message_layout)
        } else
            params.addRule(RelativeLayout.START_OF, R.id.message_layout)
        text.layoutParams = params*/

        //message.findViewById<RelativeLayout>(R.id.message_layout).layoutParams = params

        text.text = "${messages[position].from}:${messages[position].message}"

        return message
    }

    override fun getItem(position: Int): Any {
        return messages[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return messages.size
    }

    fun setMessages(_list: List<Message>) {
        messages.addAll(_list)
        notifyDataSetChanged()
    }

    fun addMessage(message: Message) {
        messages.add(message)
        notifyDataSetChanged()
    }
}