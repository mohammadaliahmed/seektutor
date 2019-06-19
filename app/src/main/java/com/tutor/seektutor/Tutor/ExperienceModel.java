package com.tutor.seektutor.Tutor;

public class ExperienceModel {
    String id,subject,experience,description;

    public ExperienceModel(String id, String subject, String experience, String description) {
        this.id = id;
        this.subject = subject;
        this.experience = experience;
        this.description = description;
    }

    public ExperienceModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
