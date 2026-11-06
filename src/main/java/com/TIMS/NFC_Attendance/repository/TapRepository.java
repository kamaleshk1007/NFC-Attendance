package com.TIMS.NFC_Attendance.repository;

import com.TIMS.NFC_Attendance.model.Tap;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TapRepository extends JpaRepository<Tap, Long> {

    boolean existsByUidAndStatusAndTappedAtAfter(String uid, Tap.Status status, Instant after);

    List<Tap> findTop20ByOrderByIdDesc();

    List<Tap> findByStatusAndTappedAtGreaterThanEqualAndTappedAtLessThan(Tap.Status status, Instant from, Instant to);
}
