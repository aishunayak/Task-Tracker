package com.smarttasktracker.model;


public class User {

    private int id;
    private String email;
    private String name;
    private String username;
    private String password;
    private String profession;
    private String companyName;
    private String designation;
    private String city;


    public User() {
    }

    public User(String email, String name, String username, String password,
                String profession, String companyName, String designation, String city) {
        this.email = email;
        this.name = name;
        this.username = username;
        this.password = password;
        this.profession = profession;
        this.companyName = companyName;
        this.designation = designation;
        this.city = city;
    }

    // Getters and Setters for all fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // toString method - useful for debugging
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", profession='" + profession + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
