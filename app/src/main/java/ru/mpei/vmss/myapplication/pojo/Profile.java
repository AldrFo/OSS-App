package ru.mpei.vmss.myapplication.pojo;

public class Profile {

    private int id;
    private String name;
    private String surname;
    private String email;
    private int capital;

    public Profile(int id, String name, String surname, String email, int capital) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.capital = capital;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCapital() {
        return capital;
    }

    public void setCapital(int capital) {
        this.capital = capital;
    }
}
