package com.TIMS.NFC_Attendance.service;

import com.TIMS.NFC_Attendance.model.Tag;
import com.TIMS.NFC_Attendance.model.Tap;
import com.TIMS.NFC_Attendance.repository.TagRepository;
import com.TIMS.NFC_Attendance.repository.TapRepository;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {

    private final TagRepository tags;
    private final TapRepository taps;
    private final Duration duplicateWindow;

    public AttendanceService(TagRepository tags, TapRepository taps,
            @Value("${attendance.duplicate-window-seconds:60}") long windowSeconds) {
        this.tags = tags;
        this.taps = taps;
        this.duplicateWindow = Duration.ofSeconds(windowSeconds);
    }

    public static String normalizeUid(String raw) {
        return raw.replaceAll("[^0-9A-Fa-f]", "").toUpperCase();
    }

    public Tag registerTag(String uid, String studentId) {
        return tags.save(new Tag(normalizeUid(uid), studentId));
    }

    public Tap recordTap(String rawUid, Instant tappedAt) {
        String uid = normalizeUid(rawUid);
        Instant now = Instant.now();
        Instant when = tappedAt != null ? tappedAt : now;

        Optional<Tag> tag = tags.findById(uid);
        if (tag.isEmpty()) {
            return taps.save(new Tap(uid, null, when, now, Tap.Status.UNKNOWN));
        }

        String studentId = tag.get().getStudentId();
        boolean duplicate = taps.existsByUidAndStatusAndTappedAtAfter(
                uid, Tap.Status.ACCEPTED, when.minus(duplicateWindow));
        Tap.Status status = duplicate ? Tap.Status.DUPLICATE : Tap.Status.ACCEPTED;
        return taps.save(new Tap(uid, studentId, when, now, status));
    }

    public List<Tap> recent() {
        return taps.findTop20ByOrderByIdDesc();
    }
}
