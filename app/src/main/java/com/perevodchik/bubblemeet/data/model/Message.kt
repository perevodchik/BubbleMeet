package com.perevodchik.bubblemeet.data.model

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class Message(
    @SerializedName("id") val id: Int?,
    @SerializedName("from") val from: Int?,
    @SerializedName("to") val to: Int,
    @SerializedName("message") val message: String,
    @SerializedName("createdAt") val createdAt: Timestamp?
)