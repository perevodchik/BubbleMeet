package com.perevodchik.bubblemeet.ui.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.util.UserInstance

class FragmentRegister: DialogFragment() {
    private var nextBtn: Button? = null
    private var closeBtn: ImageView? = null
    private var registerFragmentAdapter: RegisterFragmentPagerAdapter? = null
    private var registerTabLayout: TabLayout? = null
    private var registerViewPager: ViewPager? = null

    @SuppressLint("InflateParams", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_register, null)
        this.nextBtn = v.findViewById(R.id.buttonNext)
        this.closeBtn = v.findViewById(R.id.buttonClose)
        this.registerTabLayout = v.findViewById(R.id.tab_layout)
        this.registerViewPager = v.findViewById(R.id.profile_viewpager)

        registerFragmentAdapter = RegisterFragmentPagerAdapter(childFragmentManager)
        registerViewPager!!.setOnTouchListener { _, _ -> true }
        registerViewPager!!.adapter = registerFragmentAdapter
        registerViewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }
        })

        registerTabLayout!!.setupWithViewPager(registerViewPager)
        closeBtn!!.setOnClickListener {
            dismiss()
            activity!!.finish()
        }

        nextBtn!!.setOnClickListener {
            if((registerFragmentAdapter!!.getItem(registerViewPager!!.currentItem) as IRegisterFragment).validate()) {
                (registerFragmentAdapter!!.getItem(registerViewPager!!.currentItem) as IRegisterFragment).setData()
                showNext()
            }
        }
        return v
    }

    fun showNext() {
        registerViewPager!!.currentItem = ++registerViewPager!!.currentItem
    }

}