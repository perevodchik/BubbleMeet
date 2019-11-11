package com.perevodchik.bubblemeet.ui.register.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.ui.mainmenu.MainActivity
import com.perevodchik.bubblemeet.ui.register.IRegisterFragment
import com.perevodchik.bubblemeet.util.UserInstance
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File


class Fragment0: Fragment(), IRegisterFragment {
    private var mainPhoto: File? = null
    private lateinit var textOnMainPhoto: TextView
    private lateinit var mainAvatar: CircleImageView
    private lateinit var photo0: CircleImageView
    private lateinit var photo1: CircleImageView
    private lateinit var photo2: CircleImageView
    private lateinit var addName: EditText
    private val photos: MutableList<File> = mutableListOf()

    override fun validate(): Boolean {
        return mainPhoto != null && addName.text.isNotEmpty()
    }

    override fun showDialog() {
    }

    override fun setData() {
        UserInstance.userAvatar = mainPhoto!!
        UserInstance.userPhotos.addAll(photos)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainAvatar = view.findViewById(R.id.add_photo0)
        photo0 = view.findViewById(R.id.add_photo1)
        photo1 = view.findViewById(R.id.add_photo2)
        photo2 = view.findViewById(R.id.add_photo3)
        textOnMainPhoto = view.findViewById(R.id.text_on_avatar)
        addName = view.findViewById(R.id.add_name)
        mainAvatar.setOnClickListener { setPicker(mainAvatar, 0) }
        photo0.setOnClickListener { setPicker(photo0, 1) }
        photo1.setOnClickListener { setPicker(photo1, 2) }
        photo2.setOnClickListener { setPicker(photo2, 3) }
    }

    fun setPhoto(number: Int, file: File) {
        when(number) {
            0 -> {
                textOnMainPhoto.visibility = View.GONE
                mainPhoto = file
                Glide.with(context!!).load(file).into(mainAvatar)
            }
            1 -> {
                photos.add(0, file)
                Glide.with(context!!).load(file).into(photo0)
            }
            2 -> {
                photos.add(1, file)
                Glide.with(context!!).load(file).into(photo1)
            }
            3 -> {
                photos.add(2, file)
                Glide.with(context!!).load(file).into(photo2)
            }
        }
    }

    private fun checkPermission() {
        val permission =
            ActivityCompat.checkSelfPermission(activity as Activity, MainActivity.PERMISSIONS_STORAGE[1])

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity as Activity,
                MainActivity.PERMISSIONS_STORAGE,
                1
            )
        }
    }

    private fun setPicker(circle: CircleImageView, id: Int) {
        circle.setOnClickListener {
            checkPermission()
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activity?.startActivityForResult(intent, id)
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_0, null)
    }
}