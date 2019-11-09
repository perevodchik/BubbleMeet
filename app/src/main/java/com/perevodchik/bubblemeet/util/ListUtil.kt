package com.perevodchik.bubblemeet.util

import com.perevodchik.bubblemeet.data.model.UserData


object ListUtil {

    fun removeDuplicates(list: List<UserData>): List<UserData> {
        val instance = list.toMutableList()
        val l: MutableList<Int> = mutableListOf()

        val i = list.iterator()
        while (i.hasNext()) {
            val u = i.next()
            if (!l.contains(u.id))
                l.add(u.id)
            else
                instance.remove(u)
        }

        return instance.toList()
    }
}