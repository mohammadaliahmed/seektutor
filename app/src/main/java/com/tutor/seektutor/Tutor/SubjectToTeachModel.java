package com.tutor.seektutor.Tutor;

public class SubjectToTeachModel {
    String id,subject,description,tutorName,tutorPic,tutorId,location;

    public SubjectToTeachModel(String id, String subject, String description, String tutorName, String tutorPic, String tutorId, String location) {
        this.id = id;
        this.subject = subject;
        this.description = description;
        this.tutorName = tutorName;
        this.tutorPic = tutorPic;
        this.tutorId = tutorId;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public SubjectToTeachModel() {
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


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public String getTutorPic() {
        return tutorPic;
    }

    public void setTutorPic(String tutorPic) {
        this.tutorPic = tutorPic;
    }

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }
}
