package ru.mpei.domain_profile.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProductItem (
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val name: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("price")
    val price: Int = 0,
    @SerializedName("image_src")
    val imageUrl: String = "",
    @SerializedName("counter")
    val counter: Int = 0,
    @SerializedName("quantity")
    val quantity: Int = 0
): Serializable
