package com.perevodchik.bubblemeet.ui.register

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.perevodchik.bubblemeet.ui.register.fragments.*
import java.util.ArrayList

class RegisterFragmentPagerAdapter(_fm: FragmentManager) : FragmentPagerAdapter(_fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragments: ArrayList<Fragment> = arrayListOf(
        Fragment0(), Fragment1(), Fragment2(), Fragment3(), Fragment4(),
        Fragment5(), Fragment6(), Fragment7(), Fragment8(), Fragment9()
    )

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        //return view === `object` as Fragment
        return true
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }



}