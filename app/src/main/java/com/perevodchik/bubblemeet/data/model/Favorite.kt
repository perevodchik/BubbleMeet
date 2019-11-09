package com.perevodchik.bubblemeet.data.model

import com.google.gson.annotations.SerializedName

data class Favorite (

	@SerializedName("id") val id : Int,
	@SerializedName("Name") val name : String,
	@SerializedName("Last_name") val last_name : String,
	@SerializedName("City") val city : String,
	@SerializedName("AvatarSmall") val avatarSmall : String,
	@SerializedName("AvatarFull") val avatarFull : String,
	@SerializedName("Email") val email : String,
	@SerializedName("Login") val login : String,
	@SerializedName("Gender") val gender : String,
	@SerializedName("Age") val age : Int,
	@SerializedName("Location") val location : String,
	@SerializedName("id_favorite") val id_favorite : Int
)