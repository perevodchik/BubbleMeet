package com.perevodchik.bubblemeet.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.data.model.UserData
import com.perevodchik.bubblemeet.ui.mainmenu.MainActivity
import com.perevodchik.bubblemeet.ui.user.UserPreviewFragment
import com.perevodchik.bubblemeet.util.Values
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class GridAdapter(_list: List<UserData>, _activity: MainActivity?, _fm: FragmentManager?): BaseAdapter() {
    private var list: List<UserData> = _list
    private var activity: MainActivity? = _activity
    private var inflater: LayoutInflater? = activity?.layoutInflater
    private var fm: FragmentManager? = _fm


    @SuppressLint("ClickableViewAccessibility")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val grid: View = convertView ?: inflater!!.inflate(R.layout.item_profile_preview, parent, false)
        val name: TextView = grid.findViewById(R.id.preview_name)
        val city: TextView = grid.findViewById(R.id.preview_city)
        val avatar: CircleImageView = grid.findViewById(R.id.preview_img)

        name.text = list.elementAt(position).name
        city.text = list.elementAt(position).city
        val img = "${Values.imgUrl}/${list.elementAt(position).avatarSmall}"

        Picasso.with(activity).load(img).placeholder(R.drawable.background_loading).into(avatar)

        avatar.setOnClickListener {
            val ft: FragmentTransaction = fm!!.beginTransaction()
            val userPreviewFragment = UserPreviewFragment(list[position], activity!!.supportFragmentManager)
            ft.replace(R.id.container, userPreviewFragment)
            ft.addToBackStack(null)
            ft.commit()
        }

        return grid
    }

    fun setUsers(_list: List<UserData>) {
        list = _list
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Any {
        return list.elementAt(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

}