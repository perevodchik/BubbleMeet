package com.perevodchik.bubblemeet.ui.register

import android.annotation.SuppressLint
import android.content.DialogInterface
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
import com.perevodchik.bubblemeet.util.Api
import com.perevodchik.bubblemeet.util.UserInstance
import io.reactivex.disposables.CompositeDisposable

class FragmentRegister: DialogFragment() {
    private var nextBtn: Button? = null
    private var closeBtn: ImageView? = null
    private var registerFragmentAdapter: RegisterFragmentPagerAdapter? = null
    private var registerTabLayout: TabLayout? = null
    private var registerViewPager: ViewPager? = null
    private lateinit var presenter: RegisterPresenter

    @SuppressLint("InflateParams", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter = RegisterPresenter(activity!!)
        val v = inflater.inflate(R.layout.fragment_register, null)
        nextBtn = v.findViewById(R.id.buttonNext)
        closeBtn = v.findViewById(R.id.buttonClose)
        registerTabLayout = v.findViewById(R.id.tab_layout)
        registerViewPager = v.findViewById(R.id.profile_viewpager)

        registerFragmentAdapter = RegisterFragmentPagerAdapter(childFragmentManager)
        registerViewPager!!.setOnTouchListener { _, _ -> false }
        registerViewPager!!.adapter = registerFragmentAdapter

        registerTabLayout!!.setupWithViewPager(registerViewPager)
        closeBtn!!.setOnClickListener {
            dismiss()
            activity!!.finish()
        }

        nextBtn!!.setOnClickListener {
            (registerFragmentAdapter!!.getItem(registerViewPager!!.currentItem) as IRegisterFragment).setData()
            val index = registerViewPager!!.currentItem
            if(index == 3) {
                presenter.register(false)
            }
            if(index == 9) {
                presenter.register(true)
            }
            if((registerFragmentAdapter!!.getItem(registerViewPager!!.currentItem) as IRegisterFragment).validate()) {
                showNext()
            }
        }
        return v
    }

    fun showNext() {
        registerViewPager!!.currentItem = ++registerViewPager!!.currentItem
    }

    override fun onResume() {
        val attributes = dialog?.window?.attributes
        attributes?.height = 1400
        dialog?.window?.attributes = attributes
        super.onResume()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        activity?.finish()
    }
}