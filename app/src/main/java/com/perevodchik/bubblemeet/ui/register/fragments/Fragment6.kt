package com.perevodchik.bubblemeet.ui.register.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.ui.register.IRegisterFragment
import com.perevodchik.bubblemeet.util.UserInstance
import com.perevodchik.bubblemeet.util.setTint

class Fragment6: Fragment(), IRegisterFragment {
    private lateinit var addCooking: EditText
    private lateinit var addCity: EditText
    private val values0 by lazy { resources.getTextArray(R.array.loveToCook) }
    private val values: MutableList<String> = mutableListOf()
    private var selectedCookingItem: Int = -1

    override fun validate(): Boolean {
        val isValid = addCooking.text.isNotEmpty() && addCity.text.isNotEmpty()
        if(!isValid) {
            if(addCooking.text.isEmpty())
                addCooking.setTint(R.color.colorRed)
            else addCooking.setTint(R.color.transparent)

            if(addCity.text.isEmpty())
                addCity.setTint(R.color.colorRed)
            else addCity.setTint(R.color.transparent)
        }
        return isValid
    }

    override fun showDialog() {
        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.dialog_list)
        val list = dialog.findViewById<ListView>(R.id.list)
        values.add(values0[0].toString())
        values.add(values0[1].toString())
        val arr = Array(2) {i -> values[i]}
        list.adapter = ArrayAdapter<CharSequence>(context!!, R.layout.item_list, arr)
        list.setOnItemClickListener { _, _, position, _ ->
            addCooking.setText((list.getChildAt(position) as TextView).text)
            selectedCookingItem = position
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun setData() {
        UserInstance.profile.city = addCity.text.toString()
//        UserInstance.profile?. = selectedCookingItem
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addCity = view.findViewById(R.id.add_city)
        addCooking = view.findViewById(R.id.add_cooking)
        addCooking.setOnClickListener { showDialog() }
        addCity.doOnTextChanged { _, _, _, _ -> validate() }
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_6, null)
    }
}