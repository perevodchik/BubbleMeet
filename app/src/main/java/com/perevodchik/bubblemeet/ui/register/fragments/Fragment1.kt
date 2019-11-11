package com.perevodchik.bubblemeet.ui.register.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import androidx.core.view.ViewCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.ui.register.IRegisterFragment
import com.perevodchik.bubblemeet.util.UserInstance
import com.perevodchik.bubblemeet.util.setTint
import java.lang.NumberFormatException

class Fragment1: Fragment(), IRegisterFragment {
    private lateinit var setGender: EditText
    private lateinit var setYears: EditText

    override fun validate(): Boolean {
        val isValid =  setGender.text.isNotEmpty() && setYears.text.isNotEmpty() && setYears.text.toString().isDigitsOnly() && Integer.parseInt(setYears.text.toString()) in (1) until 120
        if(!isValid) {
            if(setGender.text.isEmpty())
                setGender.setTint(R.color.colorRed)
            else setGender.setTint(R.color.transparent)

            if(setYears.text.isEmpty() || Integer.parseInt(setYears.text.toString()) < 0 || Integer.parseInt(setYears.text.toString()) > 120)
                    setYears.setTint(R.color.colorRed)
            else setYears.setTint(R.color.transparent)
        }
        return isValid
    }

    override fun showDialog() {
        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.dialog_list)
        val list = dialog.findViewById<ListView>(R.id.list)
        val values = resources.getTextArray(R.array.gender)
        list.adapter = ArrayAdapter<CharSequence>(context!!, R.layout.item_list, values)

        list.setOnItemClickListener { _, _, position, _ ->
            setGender.setText((list.getChildAt(position) as TextView).text)
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun setData() {
        UserInstance.profile.gender = setGender.text.toString()
        try {
            UserInstance.profile.age = Integer.parseInt(setYears.text.toString())
        } catch(ignored: NumberFormatException) { }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setGender = view.findViewById(R.id.add_gender)
        setYears = view.findViewById(R.id.add_years)
        setGender.setOnClickListener { showDialog() }
        setYears.doOnTextChanged { _, _, _, _ -> validate() }
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_1, null)
    }
}