package com.perevodchik.bubblemeet.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.perevodchik.bubblemeet.R

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerFragment: FragmentRegister

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerFragment = FragmentRegister()
        registerFragment.show(supportFragmentManager, "reg")

    }

}
