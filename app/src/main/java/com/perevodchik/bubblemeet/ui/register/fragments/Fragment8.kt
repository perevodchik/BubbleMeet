package com.perevodchik.bubblemeet.ui.register.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.ui.register.IRegisterFragment
import com.perevodchik.bubblemeet.util.UserInstance
import com.perevodchik.bubblemeet.util.setTint

class Fragment8: Fragment(), IRegisterFragment {
    private lateinit var addHobbies: EditText

    override fun validate(): Boolean {
        val isValid = addHobbies.text.isNotEmpty()
        if(!isValid) {
            if(addHobbies.text.isEmpty())
                addHobbies.setTint(R.color.colorRed)
            else addHobbies.setTint(R.color.transparent)
        }
        return isValid
    }

    override fun showDialog() {
    }

    override fun setData() {
        UserInstance.profile.hobbes = addHobbies.text.toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addHobbies = view.findViewById(R.id.add_hobbies)
        addHobbies.doOnTextChanged { _, _, _, _ -> validate() }
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_8, null)
    }
}