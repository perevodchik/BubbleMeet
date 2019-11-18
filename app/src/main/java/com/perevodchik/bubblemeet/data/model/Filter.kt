package com.perevodchik.bubblemeet.data.model

data class Filter(val name: String,
                  val value: String) {
    override fun toString(): String {
        return "name=$name,value=$value"
    }
}