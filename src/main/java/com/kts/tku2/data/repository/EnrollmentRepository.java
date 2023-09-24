package com.kts.tku2.data.repository;

import com.kts.tku2.data.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository <Enrollment, Long> {
    List<Enrollment> findByStudentId(Long studentId);
    Enrollment findByStudentIdAndCourseName(Long studentId, String courseName);
}
