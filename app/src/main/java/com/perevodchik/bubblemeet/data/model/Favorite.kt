package com.perevodchik.bubblemeet.data.model

import com.google.gson.annotations.SerializedName

data class Favorite (
	@SerializedName("id") val id : Int,
	@SerializedName("id_user") val name : Int,
	@SerializedName("favorite") val last_name : Int,
	@SerializedName("updatedAt") val updatedAt: String,
	@SerializedName("createdAt") val createdAt: String
)