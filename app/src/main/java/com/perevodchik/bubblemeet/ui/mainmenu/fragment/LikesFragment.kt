package com.perevodchik.bubblemeet.ui.mainmenu.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.data.adapter.GridAdapter
import com.perevodchik.bubblemeet.data.model.UserData
import com.perevodchik.bubblemeet.ui.mainmenu.MainActivity
import com.perevodchik.bubblemeet.ui.mainmenu.presenter.LikesPresenter

class LikesFragment: Fragment() {
    private var adapter: GridAdapter? = null
    private var gridView: GridView? = null
    private val presenter: LikesPresenter = LikesPresenter(this)

    companion object {
        fun newInstance() = LikesFragment()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_grid_view, container, false)
    }

    override fun onStart() {
        super.onStart()
        presenter.fetchLikes()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gridView = view.findViewById(R.id.fragment_grid)
        adapter = GridAdapter(_activity = activity as MainActivity?, _fm = fragmentManager, _list = emptyList())
        gridView!!.adapter = adapter
    }

    fun setLikes(list: List<UserData>) {
        adapter?.setUsers(list)
    }
}