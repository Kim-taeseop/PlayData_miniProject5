package com.kts.tku2.data.repository;

import com.kts.tku2.data.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CourseRepository extends JpaRepository <Course, Long> {
    List<Course> findByProfessorId(Long professorId);
}
