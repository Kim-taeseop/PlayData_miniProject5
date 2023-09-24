package com.kts.tku2.data.repository;

import com.kts.tku2.data.entity.Department;
import com.kts.tku2.data.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessorRepository extends JpaRepository <Professor, Long> {
    Professor findByUnid(Integer unid);
    List<Professor> findByPdepartment(Department department);
}
