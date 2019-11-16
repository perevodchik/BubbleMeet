package com.perevodchik.bubblemeet.ui.mainmenu.inbox

import android.annotation.SuppressLint
import android.graphics.Point
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.data.model.Message
import com.perevodchik.bubblemeet.ui.mainmenu.MainActivity
import com.perevodchik.bubblemeet.util.Math
import com.perevodchik.bubblemeet.util.UserInstance
import kotlin.math.roundToInt

class MessageAdapter(_activity: MainActivity?): BaseAdapter() {
    private val messages: MutableList<Message> = mutableListOf()
    private var activity: MainActivity? = _activity
    private var inflater: LayoutInflater? = activity?.layoutInflater
    private val display by lazy { activity!!.windowManager.defaultDisplay }
    private var size: Point = Point()
    private var width = 0

    init {
        display.getSize(Point())
        width = size.x / 2
    }

    @SuppressLint("SetTextI18n", "NewApi")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutId = if(messages[position].to == UserInstance.profile.id)
            R.layout.item_message_left
        else R.layout.item_message_right

        val message = convertView ?: inflater!!.inflate(layoutId, parent, false)
        val text = message.findViewById<TextView>(R.id.message_text)

        //text.maxWidth = maxWidth
//        text.text = "${messages[position].from}:${messages[position].message}"
        text.text = messages[position].message
//        text.maxWidth = width
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
        messages.clear()
        messages.addAll(_list)
        notifyDataSetChanged()
    }

    fun addMessage(message: Message) {
        messages.add(message)
        notifyDataSetChanged()
    }
}