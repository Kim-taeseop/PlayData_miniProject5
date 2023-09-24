package com.kts.tku2.service.student;

import com.kts.tku2.data.entity.Enrollment;
import com.kts.tku2.data.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository){
        this.enrollmentRepository = enrollmentRepository;
    }

    // 학생 Id 를 통해 해당 학생이 수강한 과목 들고옴
    public List<Enrollment> getStudentEnrollments(Long studentId){
        return enrollmentRepository.findByStudentId(studentId);
    }
}
