package com.TIMS.NFC_Attendance.controller;

import com.TIMS.NFC_Attendance.model.Tag;
import com.TIMS.NFC_Attendance.model.Tap;
import com.TIMS.NFC_Attendance.service.AttendanceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AttendanceController {

    public record TagRequest(@NotBlank String uid, @NotBlank String studentId) {}
    public record TagResponse(String uid, String studentId) {}
    public record TapRequest(@NotBlank String uid, Instant tappedAt) {}
    public record TapResponse(String status, String uid, String studentId, Instant tappedAt, Instant receivedAt) {}

    private final AttendanceService service;

    public AttendanceController(AttendanceService service) {
        this.service = service;
    }

    @PostMapping("/tags")
    @ResponseStatus(HttpStatus.CREATED)
    public TagResponse registerTag(@Valid @RequestBody TagRequest req) {
        Tag t = service.registerTag(req.uid(), req.studentId());
        return new TagResponse(t.getUid(), t.getStudentId());
    }

    @PostMapping("/taps")
    public TapResponse tap(@Valid @RequestBody TapRequest req) {
        return toResponse(service.recordTap(req.uid(), req.tappedAt()));
    }

    @GetMapping("/taps")
    public List<TapResponse> recent() {
        return service.recent().stream().map(AttendanceController::toResponse).toList();
    }

    private static TapResponse toResponse(Tap t) {
        return new TapResponse(t.getStatus().name(), t.getUid(), t.getStudentId(), t.getTappedAt(), t.getReceivedAt());
    }
}
