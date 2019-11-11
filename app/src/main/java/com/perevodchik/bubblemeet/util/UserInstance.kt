package com.perevodchik.bubblemeet.util

import com.perevodchik.bubblemeet.data.model.Profile
import java.io.File

object UserInstance {
    var profile: Profile = Profile()
    var session: String? = null
    var password: String = ""
    lateinit var userAvatar: File
    var userPhotos: MutableList<File> = mutableListOf()
}