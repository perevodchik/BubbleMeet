package com.perevodchik.bubblemeet.ui.mainmenu.inbox

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.data.model.ChatItem
import com.perevodchik.bubblemeet.ui.mainmenu.MainActivity
import com.perevodchik.bubblemeet.util.Values
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception

class ChatAdapter(_list: List<ChatItem>, _activity: MainActivity?): BaseAdapter(){
    private var list: List<ChatItem> = _list
    private var activity: MainActivity? = _activity
    private var inflater: LayoutInflater? = activity?.layoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val chat = convertView ?: inflater!!.inflate(R.layout.item_chat, parent, false)

        val indicator: ImageView = chat.findViewById(R.id.inbox_indicator)
        val name: TextView = chat.findViewById(R.id.chat_item_name)
        val city: TextView = chat.findViewById(R.id.chat_item_country)
        val open: ImageView = chat.findViewById(R.id.chat_item_open)
        val avatar: CircleImageView = chat.findViewById(R.id.chat_item_avatar)

//        when (position) {
//            0 -> indicator.setImageDrawable(convertView?.resources?.getDrawable(R.drawable.inbox_element_top, null))
//            count -> indicator.setImageDrawable(convertView?.resources?.getDrawable(R.drawable.inbox_element_bottom, null))
//            else -> indicator.setImageDrawable(convertView?.resources?.getDrawable(R.drawable.inbox_element_middle, null))
//        }

        open.setOnClickListener {
            val t = activity?.supportFragmentManager?.beginTransaction()
            val fragment = ChatFragment(list[position].id)
            t?.replace(R.id.container, fragment)
            t?.addToBackStack(null)
            t?.commit()
        }

        name.text = list[position].name
        city.text = list[position].city

        try {
            Picasso.with(activity).load("${Values.imgUrl}/${list[position].avatarFull}").placeholder(R.drawable.background_loading).into(avatar)
        } catch(e: Exception) { println("some wrong...") }

        return chat
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    fun getChats(): List<ChatItem> {
        return list
    }

    fun setChats(_list: List<ChatItem>) {
        for(c in _list)
            println(c)
        list = _list
        notifyDataSetChanged()
    }

}