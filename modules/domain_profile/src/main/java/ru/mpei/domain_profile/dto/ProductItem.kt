package ru.mpei.domain_profile.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProductItem (
    //ID продукта
    @SerializedName("id")
    val id: Int = 0,
    //Имя продукта
    @SerializedName("title")
    val name: String = "",
    //Описание продукта
    @SerializedName("description")
    val description: String = "",
    //Цена продукта
    @SerializedName("price")
    val price: Int = 0,
    //Ссылка на изображение продукта
    @SerializedName("image_src")
    val imageUrl: String = "",
    //Число покупок
    @SerializedName("counter")
    val counter: Int = 0,
    //Количество оставшегося товара
    @SerializedName("quantity")
    val quantity: Int = 0
): Serializable
