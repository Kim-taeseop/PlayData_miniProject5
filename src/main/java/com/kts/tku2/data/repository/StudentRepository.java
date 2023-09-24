package com.kts.tku2.data.repository;

import com.kts.tku2.data.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository <Student, Long> {
    // 사용자 아이디(학번)를 기반으로 학생 정보 조회
    Student findByUnid(Integer unid);
}
