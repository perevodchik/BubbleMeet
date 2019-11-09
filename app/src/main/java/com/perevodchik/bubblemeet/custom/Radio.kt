package com.perevodchik.bubblemeet.custom

import android.annotation.SuppressLint
import android.content.Context
import android.widget.RadioButton
import androidx.appcompat.widget.AppCompatRadioButton
import com.google.android.material.tabs.TabLayout

@SuppressLint("ViewConstructor")
class Radio(context: Context, _key: String, _value: String): AppCompatRadioButton(context) {
    var key: String = _key
    var value: String = _value

    init {
        text = value
    }

    override fun toString(): String {
        return "Radio(key='$key', value='$value')"
    }
}