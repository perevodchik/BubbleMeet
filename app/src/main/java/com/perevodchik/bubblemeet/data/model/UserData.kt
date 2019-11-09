package com.perevodchik.bubblemeet.data.model

import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("id") val id : Int,
    @SerializedName("Name") val name : String,
    @SerializedName("Last_name") val last_name : String,
    @SerializedName("Photo") val photo : List<String>,
    @SerializedName("City") val city : String,
    @SerializedName("AvatarSmall") val avatarSmall : String,
    @SerializedName("AvatarFull") val avatarFull : String,
    @SerializedName("Email") val email : String,
    @SerializedName("EyeColor") val eyeColor : String,
    @SerializedName("Login") val login : String,
    @SerializedName("Gender") val gender : String,
    @SerializedName("Age") val age : Int,
    @SerializedName("Location") val location : String,
    @SerializedName("Height") val height : Int,
    @SerializedName("Smoking") val smoking : Int,
    @SerializedName("Religion") val religion : String,
    @SerializedName("Marred") val marred : Int,
    @SerializedName("Children") val children : Int,
    @SerializedName("Looking") val looking : String,
    @SerializedName("Hobbes") val hobbes : String,
    @SerializedName("timeOnline") val timeOnline : String,
    @SerializedName("id_favorite") val id_favorite : Int,
    @SerializedName("showWindow") val showWindow : Int,
    @SerializedName("isViewed") val isViewed : Int
)