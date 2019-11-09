package com.perevodchik.bubblemeet.data.model

import com.google.gson.annotations.SerializedName

data class ChatItem(
    @SerializedName("id") val id : Int,
    @SerializedName("Name") val name : String,
    @SerializedName("Last_name") val last_name : String,
    @SerializedName("City") val city : String,
    @SerializedName("AvatarSmall") val avatarSmall : String,
    @SerializedName("AvatarFull") val avatarFull : String,
    @SerializedName("timeOnline") val timeOnline : String
)