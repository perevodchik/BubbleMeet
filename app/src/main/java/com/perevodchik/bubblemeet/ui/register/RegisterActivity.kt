package com.perevodchik.bubblemeet.ui.register

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.ui.register.fragments.*
import java.io.File

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerFragment: FragmentRegister

    companion object {
        val fragments: List<Fragment> = listOf(
            Fragment0(), Fragment1(), Fragment2(), Fragment3(), Fragment4(),
            Fragment5(), Fragment6(), Fragment7(), Fragment8(), Fragment9()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerFragment = FragmentRegister()
        registerFragment.show(supportFragmentManager, "reg")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("setPhoto", "$requestCode -> $resultCode -> ${data.toString()}")
        val file = File(getRealPathFromUri(applicationContext, data?.data))
        (fragments[0] as Fragment0).setPhoto(requestCode, file)
    }

    private fun getRealPathFromUri(context: Context, contentUri: Uri?): String {
        val str = "_data"
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(contentUri!!, arrayOf(str), null, null, null)
            val column = cursor!!.getColumnIndexOrThrow(str)
            cursor.moveToFirst()
            return cursor.getString(column)
        } finally {
            cursor?.close()
        }
    }
}
