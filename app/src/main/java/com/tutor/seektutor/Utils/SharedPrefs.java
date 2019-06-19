package com.tutor.seektutor.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.tutor.seektutor.Models.Student;
import com.tutor.seektutor.Models.Tutor;
import com.google.gson.Gson;


/**
 * Created by AliAh on 20/02/2018.
 */

public class SharedPrefs {


    private SharedPrefs() {

    }

    public static String getUsername() {
        return preferenceGetter("username");
    }


    public static void setUsername(String username) {
        preferenceSetter("username", username);
    }


    public static void setName(String value) {

        preferenceSetter("name", value);
    }

    public static String getName() {
        return preferenceGetter("name");
    }

    public static void setPhone(String value) {

        preferenceSetter("phone", value);
    }


    public static String getPhone() {
        return preferenceGetter("phone");
    }


    public static String getUserType() {
        return preferenceGetter("userType");
    }

    public static void setUserType(String value) {

        preferenceSetter("userType", value);
    }


    public static void setIsLoggedIn(String value) {

        preferenceSetter("isLoggedIn", value);
    }

    public static String getIsLoggedIn() {
        return preferenceGetter("isLoggedIn");
    }


    public static void setFcmKey(String fcmKey) {
        preferenceSetter("fcmKey", fcmKey);
    }

    public static String getFcmKey() {
        return preferenceGetter("fcmKey");
    }

    public static void setStudent(Student model) {

        Gson gson = new Gson();
        String json = gson.toJson(model);
        preferenceSetter("studentModel", json);
    }

    public static Student getStudent() {
        Gson gson = new Gson();
        Student customer = gson.fromJson(preferenceGetter("studentModel"), Student.class);
        return customer;
    }

    public static void setTutor(Tutor model) {

        Gson gson = new Gson();
        String json = gson.toJson(model);
        preferenceSetter("tutorModel", json);
    }

    public static Tutor getTutor() {
        Gson gson = new Gson();
        Tutor customer = gson.fromJson(preferenceGetter("tutorModel"), Tutor.class);
        return customer;
    }


    public static void preferenceSetter(String key, String value) {
        SharedPreferences pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String preferenceGetter(String key) {
        SharedPreferences pref;
        String value = "";
        pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        value = pref.getString(key, "");
        return value;
    }

    public static void logout() {
        SharedPreferences pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}
