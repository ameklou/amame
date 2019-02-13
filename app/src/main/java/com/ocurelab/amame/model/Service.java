package com.ocurelab.amame.model;

public class Service {
    private String name, phone, description,city;

    public Service() {
    }

    public Service(String name, String phone, String description, String city) {
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
