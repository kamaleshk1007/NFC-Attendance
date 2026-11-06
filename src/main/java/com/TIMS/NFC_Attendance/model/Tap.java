package com.TIMS.NFC_Attendance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "taps", indexes = @Index(name = "idx_taps_uid_tapped_at", columnList = "uid, tapped_at"))
public class Tap {

    public enum Status { ACCEPTED, DUPLICATE, UNKNOWN }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 32)
    private String uid;

    @Column(name = "student_id", length = 64)
    private String studentId;

    @Column(name = "tapped_at", nullable = false)
    private Instant tappedAt;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Status status;

    protected Tap() {}

    public Tap(String uid, String studentId, Instant tappedAt, Instant receivedAt, Status status) {
        this.uid = uid;
        this.studentId = studentId;
        this.tappedAt = tappedAt;
        this.receivedAt = receivedAt;
        this.status = status;
    }

    public Long getId() { return id; }
    public String getUid() { return uid; }
    public String getStudentId() { return studentId; }
    public Instant getTappedAt() { return tappedAt; }
    public Instant getReceivedAt() { return receivedAt; }
    public Status getStatus() { return status; }
}
