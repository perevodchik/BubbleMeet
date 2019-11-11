package com.perevodchik.bubblemeet.data.model

import com.google.gson.annotations.SerializedName

data class Profile (
	@SerializedName("id") var id : Int = -1,
	@SerializedName("Name") var name : String = "",
	@SerializedName("Last_name") var last_name : String = "",
	@SerializedName("Photo") var photo : List<String> = listOf(),
	@SerializedName("City") var city : String = "",
	@SerializedName("AvatarSmall") var avatarSmall : String = "",
	@SerializedName("AvatarFull") var avatarFull : String = "",
	@SerializedName("Email") var email : String = "",
	@SerializedName("EyeColor") var eyeColor : String = "",
	@SerializedName("Login") var login : String = "",
	@SerializedName("Gender") var gender : String = "",
	@SerializedName("Age") var age : Int = -1,
	@SerializedName("Location") var location : String = "",
	@SerializedName("Height") var height : Int = -1,
	@SerializedName("Smoking") var smoking : Int = -1,
	@SerializedName("Religion") var religion : String = "",
	@SerializedName("Marred") var marred : Int = -1,
	@SerializedName("Children") var children : Int = -1,
	@SerializedName("Looking") var looking : String = "",
	@SerializedName("Hobbes") var hobbes : String = "",
	@SerializedName("timeOnline") var timeOnline : String = "",
	@SerializedName("countViews") var countViews : Int = -1,
	@SerializedName("rights") var rights : Boolean = false
)
