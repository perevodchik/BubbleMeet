package com.perevodchik.bubblemeet.ui.register.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.ui.register.IRegisterFragment

class Fragment7: Fragment(), IRegisterFragment {
    override fun setData() {

    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_7, null)
    }
}