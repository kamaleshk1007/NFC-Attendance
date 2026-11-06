package com.TIMS.NFC_Attendance.controller;

import com.TIMS.NFC_Attendance.service.ReportService;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ReportController {

    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    @GetMapping("/api/report")
    public List<ReportService.ReportRow> report(@RequestParam(required = false) String date) {
        try {
            LocalDate day = date == null ? LocalDate.now() : LocalDate.parse(date);
            return service.dailyReport(day);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "date must be yyyy-MM-dd");
        }
    }
}
