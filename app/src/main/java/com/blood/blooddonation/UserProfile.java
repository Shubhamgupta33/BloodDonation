package com.blood.blooddonation;

public class UserProfile {

    private String name, age, phone, email, city, state, address, password, group,preimg,gender,lastBD;

    public UserProfile() {
    }

    public UserProfile(String name, String age, String phone, String email, String city, String state, String address, String password, String group, String preimg, String gender, String lastBD) {
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.city = city;
        this.state = state;
        this.address = address;
        this.password = password;
        this.group = group;
        this.preimg = preimg;
        this.gender = gender;
        this.lastBD = lastBD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPreimg() {
        return preimg;
    }

    public void setPreimg(String preimg) {
        this.preimg = preimg;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastBD() {
        return lastBD;
    }

    public void setLastBD(String lastBD) {
        this.lastBD = lastBD;
    }
}
