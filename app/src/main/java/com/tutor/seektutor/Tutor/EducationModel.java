package com.tutor.seektutor.Tutor;

public class EducationModel {
    String id,educationName,year,description;

    public EducationModel(String id, String educationName, String year, String description) {
        this.id = id;
        this.educationName = educationName;
        this.year = year;
        this.description = description;
    }

    public EducationModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEducationName() {
        return educationName;
    }

    public void setEducationName(String educationName) {
        this.educationName = educationName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
