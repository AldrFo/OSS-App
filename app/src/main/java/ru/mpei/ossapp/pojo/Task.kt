package ru.mpei.ossapp.pojo

data class Task (
    var taskName: String,
    var taskDescription: String,
    var location: String,
    var price: String,
    var startDate: String,
    var endDate: String,
    var refuseInfo: String,
    var status: String? = null,
    var changeBefore: String? = null,
    var id: String? = null,
    var penalty: String? = null
)