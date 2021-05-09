package ru.mpei.domain_profile.dto

import com.google.gson.annotations.SerializedName

data class ProductItem(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val name: String = "",
    @SerializedName("price")
    val price: String = "0",
    @SerializedName("image_src")
    val imageUrl: String = "",
    @SerializedName("counter")
    val counter: Int = 0,
    @SerializedName("quantity")
    val quantity: Int = 0
)
