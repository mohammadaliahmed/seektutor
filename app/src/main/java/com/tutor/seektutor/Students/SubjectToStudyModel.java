package com.tutor.seektutor.Students;

public class SubjectToStudyModel {
    String id,subject,description,studentName,studentPic,studentId,location;

    public SubjectToStudyModel(String id, String subject, String description, String studentName, String studentPic, String studentId, String location) {
        this.id = id;
        this.subject = subject;
        this.description = description;
        this.studentName = studentName;
        this.studentPic = studentPic;
        this.studentId = studentId;
        this.location = location;
    }

    public SubjectToStudyModel() {
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

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentPic() {
        return studentPic;
    }

    public void setStudentPic(String studentPic) {
        this.studentPic = studentPic;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
