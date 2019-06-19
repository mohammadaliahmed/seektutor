package com.tutor.seektutor.Models;

public class RatingModel {
    String id,ratingFrom,picUrl;
    float rating;
    long time;
    String tutorId,studentId;

    public RatingModel(String id, String ratingFrom, String picUrl, float rating, long time, String tutorId, String studentId) {
        this.id = id;
        this.ratingFrom = ratingFrom;
        this.picUrl = picUrl;
        this.rating = rating;
        this.time = time;
        this.tutorId = tutorId;
        this.studentId = studentId;
    }

    public RatingModel() {
    }

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRatingFrom() {
        return ratingFrom;
    }

    public void setRatingFrom(String ratingFrom) {
        this.ratingFrom = ratingFrom;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
