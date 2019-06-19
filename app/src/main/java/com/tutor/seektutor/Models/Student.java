package com.tutor.seektutor.Models;

import java.util.ArrayList;
import java.util.List;

public class Student {
    String username,name,password,phone,address,city,age,gender,fcmKey,picUrl;
    long time;

    List<String> confirmFriends=new ArrayList<>();
    List<String> requestSent=new ArrayList<>();
    List<String> requestReceived=new ArrayList<>();
    List<String> ratedTutors=new ArrayList<>();

    public Student(String username, String name, String password, String phone, String address,String city, String age, String gender, String fcmKey, String picUrl, long time) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.age = age;
        this.gender = gender;
        this.fcmKey = fcmKey;
        this.picUrl = picUrl;
        this.time = time;
    }


    public Student() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getRatedTutors() {
        return ratedTutors;
    }

    public void setRatedTutors(List<String> ratedTutors) {
        this.ratedTutors = ratedTutors;
    }

    public List<String> getConfirmFriends() {
        return confirmFriends;
    }

    public void setConfirmFriends(List<String> confirmFriends) {
        this.confirmFriends = confirmFriends;
    }

    public List<String> getRequestSent() {
        return requestSent;
    }

    public void setRequestSent(List<String> requestSent) {
        this.requestSent = requestSent;
    }

    public List<String> getRequestReceived() {
        return requestReceived;
    }

    public void setRequestReceived(List<String> requestReceived) {
        this.requestReceived = requestReceived;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFcmKey() {
        return fcmKey;
    }

    public void setFcmKey(String fcmKey) {
        this.fcmKey = fcmKey;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
