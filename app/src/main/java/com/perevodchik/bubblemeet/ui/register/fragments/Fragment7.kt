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

class Fragment7: Fragment(), IRegisterFragment {
    private lateinit var addLooking: EditText
    private val values by lazy { resources.getTextArray(R.array.lookingForChoice) }

    override fun validate(): Boolean {
        val isValid = addLooking.text.isNotEmpty()
        if(!isValid) {
            if(addLooking.text.isEmpty())
                addLooking.setTint(R.color.colorRed)
            else addLooking.setTint(R.color.transparent)
        }
        return isValid
    }

    override fun showDialog() {
        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.dialog_list)
        val list = dialog.findViewById<ListView>(R.id.list)
        list.adapter = ArrayAdapter<CharSequence>(context!!, R.layout.item_list, values)
        list.setOnItemClickListener { _, _, position, _ ->
            addLooking.setText((list.getChildAt(position) as TextView).text)
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun setData() {
        UserInstance.profile.looking = addLooking.text.toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addLooking = view.findViewById(R.id.add_looking)
        addLooking.setOnClickListener { showDialog() }
        addLooking.doOnTextChanged { _, _, _, _ -> validate() }
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_7, null)
    }
}