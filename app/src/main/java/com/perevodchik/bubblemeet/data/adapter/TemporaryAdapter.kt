package com.perevodchik.bubblemeet.data.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.data.model.UserData
import com.perevodchik.bubblemeet.ui.mainmenu.MainActivity
import com.perevodchik.bubblemeet.ui.user.UserPreviewFragment
import com.perevodchik.bubblemeet.util.Values
import com.squareup.picasso.Picasso


class TemporaryAdapter(_ctx: MainActivity?, _fm: FragmentManager?, _list: MutableList<UserData>):
    RecyclerView.Adapter<TemporaryAdapter.ViewHolder>() {
    private var list: MutableList<UserData> = _list
    private val context: MainActivity? = _ctx as MainActivity
    private val fm: FragmentManager? = _fm
    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_temporaty_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userData: UserData = list[position]

        holder.avatarView.setOnClickListener {
            val transaction = fm?.beginTransaction()
            val frag = UserPreviewFragment(userData, context!!.supportFragmentManager)
            transaction?.replace(R.id.container, frag)
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        try {
            Picasso.with(context).load("${Values.imgUrl}/${userData.avatarFull}")
                .placeholder(R.drawable.preview_user_avatar_background).into(holder.avatarView)
        }
        catch(e: Exception) {
        }
    }

    open inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatarView: ImageView = view.findViewById(R.id.temporary_avatar)
    }

    fun setUsers(_list: List<UserData>) {
        val ids: MutableSet<Int> = mutableSetOf()

        for(u in _list) {
            if (!ids.contains(u.id)) {
                ids.add(u.id)
                Log.d("duplicate temporary", "setUsers -> ${u.id}")
                list.add(u)
            }
        }

        notifyDataSetChanged()
    }

    fun addUserToStart(user: UserData) {
        for(u in list)
            if(u.id == user.id) {
                Log.d("duplicate temporary", "addUserToStart -> ${user.id}")
                return
            }

        list.add(0, user)
        notifyDataSetChanged()
    }

}