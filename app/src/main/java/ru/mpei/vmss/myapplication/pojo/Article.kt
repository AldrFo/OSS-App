package ru.mpei.vmss.myapplication.pojo

import android.media.Image

data class Article(var title: String, var content: String, var date: String, var img: Image?, var imageUrl: String)