package ru.mpei.vmss.myapplication.pojo

class Task {
    var taskName: String
    var taskDescription: String
    var location: String
    var price: String
    var startDate: String
    var endDate: String
    var refuse_info: String
    var status: String? = null
    var changeBefore: String? = null
    var id: String? = null
    var penalty: String? = null

    constructor(taskName: String, taskDescription: String, location: String, price: String, startDate: String, endDate: String, refuse_info: String) {
        this.taskName = taskName
        this.taskDescription = taskDescription
        this.location = location
        this.price = price
        this.startDate = startDate
        this.endDate = endDate
        this.refuse_info = refuse_info
    }

    constructor(taskName: String, taskDescription: String, location: String, price: String, startDate: String, endDate: String, refuse_info: String, ID: String?) {
        this.taskName = taskName
        this.taskDescription = taskDescription
        this.location = location
        this.price = price
        this.startDate = startDate
        this.endDate = endDate
        this.refuse_info = refuse_info
        id = ID
    }

}