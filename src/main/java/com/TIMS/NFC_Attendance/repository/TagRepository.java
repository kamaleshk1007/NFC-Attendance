package com.TIMS.NFC_Attendance.repository;

import com.TIMS.NFC_Attendance.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, String> {
}
