package com.perevodchik.bubblemeet.ui.register.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.ui.register.FragmentRegister
import com.perevodchik.bubblemeet.ui.register.IRegisterFragment

class Fragment3: Fragment(), IRegisterFragment {
    private lateinit var buttonAddMore: Button

    override fun validate(): Boolean {
        return true
    }

    override fun showDialog() {}

    override fun setData() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        buttonAddMore = view.findViewById(R.id.buttonAddMore)
        buttonAddMore.setOnClickListener { (parentFragment as FragmentRegister).showNext() }
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_3, null)
    }
}