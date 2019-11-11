package com.perevodchik.bubblemeet.ui.register.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
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
import java.lang.NumberFormatException

class Fragment4: Fragment(), IRegisterFragment {
    private lateinit var addHeight: EditText
    private lateinit var addSmoke: EditText
    private val values0 by lazy { resources.getTextArray(R.array.smoking) }
    private val values: MutableList<String> = mutableListOf()
    private var selectedSmokeItem: Int = -1

    override fun validate(): Boolean {
        val isValid = addHeight.text.isNotEmpty() && addSmoke.text.isNotEmpty()
        if(!isValid) {
            if(addHeight.text.isEmpty())
                addHeight.setTint(R.color.colorRed)
            else addHeight.setTint(R.color.transparent)

            if(addSmoke.text.isEmpty())
                addSmoke.setTint(R.color.colorRed)
            else addSmoke.setTint(R.color.transparent)
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
            addSmoke.setText((list.getChildAt(position) as TextView).text)
            selectedSmokeItem = position
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun setData() {
        UserInstance.profile.smoking = selectedSmokeItem
        try {
            UserInstance.profile.height = Integer.parseInt(addHeight.text.toString())
        } catch(ignored: NumberFormatException) {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addHeight = view.findViewById(R.id.add_height)
        addSmoke = view.findViewById(R.id.add_smoking)
        addSmoke.setOnClickListener { showDialog() }
        addHeight.doOnTextChanged { _, _, _, _ -> validate() }
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_4, null)
    }
}