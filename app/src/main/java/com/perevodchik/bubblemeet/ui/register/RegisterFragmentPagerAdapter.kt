package com.perevodchik.bubblemeet.ui.register

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.perevodchik.bubblemeet.ui.register.fragments.*

class RegisterFragmentPagerAdapter(_fm: FragmentManager) :
    FragmentPagerAdapter(_fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragments: List<Fragment> = listOf(
        Fragment0(), Fragment1(), Fragment2(), Fragment3(), Fragment4(),
        Fragment5(), Fragment6(), Fragment7(), Fragment8(), Fragment9()
    )

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }


}