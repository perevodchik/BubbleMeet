package com.perevodchik.bubblemeet.ui.register

interface IRegisterFragment {
    fun setData()
    fun validate(): Boolean
    fun showDialog()
}