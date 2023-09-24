package com.kts.tku2.data.repository;

import com.kts.tku2.data.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository <Department, Long> {
}
