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

class Fragment5: Fragment(), IRegisterFragment {
    private lateinit var addMarried: EditText
    private lateinit var addChild: EditText
    private val arrayMarried by lazy { resources.getTextArray(R.array.married) }
    private val arrayChildren by lazy { resources.getTextArray(R.array.children) }
    private val values: MutableList<String> = mutableListOf()
    private var selectMarred: Int = -1
    private var selectChild: Int = -1
    private var selectedField: Int = -1
    private var selectedItem: Int = -1

    override fun validate(): Boolean {
        val isValid = addMarried.text.isNotEmpty() && addChild.text.isNotEmpty()
        if(!isValid) {
            if(addMarried.text.isEmpty())
                addMarried.setTint(R.color.colorRed)
            else addMarried.setTint(R.color.transparent)

            if(addChild.text.isEmpty())
                addChild.setTint(R.color.colorRed)
            else addChild.setTint(R.color.transparent)
        }
        return isValid
    }

    override fun showDialog() {
        values.clear()
        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.dialog_list)
        val list = dialog.findViewById<ListView>(R.id.list)
        when(selectedField) {
            0 -> {
                values.add(arrayMarried[0].toString())
                values.add(arrayMarried[1].toString())
            }
            1 -> {
                values.add(arrayChildren[0].toString())
                values.add(arrayChildren[1].toString())
            }
        }
        val arr = Array(2) {i -> values[i]}
        list.adapter = ArrayAdapter<CharSequence>(context!!, R.layout.item_list, arr)
        list.setOnItemClickListener { _, _, position, _ ->
            when(selectedField) {
                0 -> {
                    selectMarred = position
                    addMarried.setText((list.getChildAt(position) as TextView).text)
                }
                1 -> {
                    selectChild = position
                    addChild.setText((list.getChildAt(position) as TextView).text)
                }
            }


            selectedItem = position
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun setData() {
        UserInstance.profile.marred = selectMarred
        UserInstance.profile.marred = selectChild
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addMarried = view.findViewById(R.id.add_married)
        addChild = view.findViewById(R.id.add_children)
        addMarried.setOnClickListener {
            selectedField = 0
            showDialog()
        }
        addChild.setOnClickListener {
            selectedField = 1
            showDialog()
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_5, null)
    }
}