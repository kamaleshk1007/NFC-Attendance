package com.TIMS.NFC_Attendance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @Column(length = 32)
    private String uid;

    @Column(name = "student_id", nullable = false, unique = true, length = 64)
    private String studentId;

    protected Tag() {}

    public Tag(String uid, String studentId) {
        this.uid = uid;
        this.studentId = studentId;
    }

    public String getUid() { return uid; }
    public String getStudentId() { return studentId; }
}
