package ru.mpei.vmss.myapplication.pojo;

public class Task {

    private String taskName;
    private String taskDescription;
    private String location;
    private String price;
    private String startDate;
    private String endDate;
    private String refuse_info;
    private String status;
    private String changeBefore;
    private String ID;
    private String penalty;

    public Task(String taskName, String taskDescription, String location, String price, String startDate, String endDate, String refuse_info) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.location = location;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.refuse_info = refuse_info;
    }

    public Task(String taskName, String taskDescription, String location, String price, String startDate, String endDate, String refuse_info, String ID) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.location = location;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.refuse_info = refuse_info;
        this.ID = ID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getRefuse_info() {
        return refuse_info;
    }

    public void setRefuse_info(String refuse_info) {
        this.refuse_info = refuse_info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChangeBefore() {
        return changeBefore;
    }

    public void setChangeBefore(String changeBefore) {
        this.changeBefore = changeBefore;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPenalty() {
        return penalty;
    }

    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }

}
