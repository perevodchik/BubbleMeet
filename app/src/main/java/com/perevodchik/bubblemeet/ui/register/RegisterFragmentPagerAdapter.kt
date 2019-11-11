package com.perevodchik.bubblemeet.ui.register

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.perevodchik.bubblemeet.ui.register.fragments.*

class RegisterFragmentPagerAdapter(_fm: FragmentManager) :
    FragmentPagerAdapter(_fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragments = RegisterActivity.fragments

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }


}