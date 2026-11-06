package com.TIMS.NFC_Attendance.service;

import com.TIMS.NFC_Attendance.model.Tag;
import com.TIMS.NFC_Attendance.model.Tap;
import com.TIMS.NFC_Attendance.repository.TagRepository;
import com.TIMS.NFC_Attendance.repository.TapRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    public record ReportRow(String studentId, String uid, boolean present, int taps, Instant firstTap, String firstTapLocal) {}

    private final TagRepository tags;
    private final TapRepository taps;

    public ReportService(TagRepository tags, TapRepository taps) {
        this.tags = tags;
        this.taps = taps;
    }

    public List<ReportRow> dailyReport(LocalDate date) {
        ZoneId zone = ZoneId.systemDefault();
        Instant from = date.atStartOfDay(zone).toInstant();
        Instant to = date.plusDays(1).atStartOfDay(zone).toInstant();
        Map<String, List<Tap>> byUid = taps
                .findByStatusAndTappedAtGreaterThanEqualAndTappedAtLessThan(Tap.Status.ACCEPTED, from, to)
                .stream().collect(Collectors.groupingBy(Tap::getUid));
        return tags.findAll().stream()
                .sorted(Comparator.comparing(Tag::getStudentId))
                .map(t -> {
                    List<Tap> list = byUid.getOrDefault(t.getUid(), List.of());
                    Instant first = list.stream().map(Tap::getTappedAt).min(Comparator.naturalOrder()).orElse(null);
                    String local = first == null ? null : first.atZone(zone).toLocalTime().withNano(0).toString();
                    return new ReportRow(t.getStudentId(), t.getUid(), !list.isEmpty(), list.size(), first, local);
                })
                .toList();
    }
}
