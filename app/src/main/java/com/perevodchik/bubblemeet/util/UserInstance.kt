package com.perevodchik.bubblemeet.util

import com.perevodchik.bubblemeet.data.model.Profile
import com.perevodchik.bubblemeet.data.model.UserData
import java.io.File

object UserInstance {
    var profile: Profile = Profile()
    var session: String? = null
    var password: String = ""
    var userAvatar: File? = null
    var userPhotos: MutableList<File> = mutableListOf()
    var userHistory: MutableList<UserData> = mutableListOf()
    var userMatches: MutableList<UserData> = mutableListOf()
    var userLikes: MutableList<UserData> = mutableListOf()
    var allUsers: MutableList<UserData> = mutableListOf()
    val filters: HashMap<String, String> = HashMap()

    fun removeLikes(userData: UserData) {
        try {
            for (u in userLikes)
                if (u.id == userData.id || u.id_favorite == userData.id_favorite) {
                    userLikes.remove(u)
                }
        } catch (ignored: ConcurrentModificationException) {}
    }

    fun addUsers(list: List<UserData>) {
//        if(allUsers.isEmpty()) {
//            allUsers.addAll(list)
//        } else if(allUsers.size != list.size) {
//            allUsers.clear()
//            allUsers.addAll(list)
//        }
        allUsers.addAll(list)
    }
}