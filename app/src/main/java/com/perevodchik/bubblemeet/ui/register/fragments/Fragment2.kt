package com.perevodchik.bubblemeet.ui.register.fragments

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.ui.register.IRegisterFragment
import com.perevodchik.bubblemeet.util.UserInstance
import com.perevodchik.bubblemeet.util.setTint

class Fragment2: Fragment(), IRegisterFragment {
    private lateinit var setEmail: EditText
    private lateinit var setPassword: EditText
    private lateinit var confirmPassword: EditText

    override fun validate(): Boolean {
        val isValid = setEmail.text.isNotEmpty() && setPassword.text.isNotEmpty() && confirmPassword.text.isNotEmpty() && setPassword.text.toString() == confirmPassword.text.toString()
        if(!isValid) {
            if(setEmail.text.isEmpty())
                setEmail.setTint(R.color.colorRed)
            else setEmail.setTint(R.color.transparent)

            if(setPassword.text.isEmpty())
                setPassword.setTint(R.color.colorRed)
            else setPassword.setTint(R.color.transparent)

            if(confirmPassword.text.isEmpty())
                confirmPassword.setTint(R.color.colorRed)
            else confirmPassword.setTint(R.color.transparent)

            if(setPassword.text.toString() != confirmPassword.text.toString()) {
                setPassword.setTint(R.color.colorRed)
                confirmPassword.setTint(R.color.colorRed)
            } else {
                setPassword.setTint(R.color.transparent)
                confirmPassword.setTint(R.color.transparent)
            }
        }
        return isValid
    }

    override fun showDialog() {}

    override fun setData() {
        UserInstance.profile.email = setEmail.text.toString()
        UserInstance.password = setPassword.text.toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setEmail = view.findViewById(R.id.add_email)
        setPassword = view.findViewById(R.id.add_password)
        confirmPassword = view.findViewById(R.id.confirm_password)

        setEmail.doOnTextChanged { _, _, _, _ -> validate() }
        setPassword.doOnTextChanged { _, _, _, _ -> validate() }
        confirmPassword.doOnTextChanged { _, _, _, _ -> validate() }
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_2, null)
    }
}