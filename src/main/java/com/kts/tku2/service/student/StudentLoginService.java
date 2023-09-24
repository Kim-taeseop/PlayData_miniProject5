package com.kts.tku2.service.student;

import com.kts.tku2.data.entity.Student;
import com.kts.tku2.data.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StudentLoginService{

    @Autowired
    private StudentRepository studentRepository;

    public Student findByUnid(Integer unid) {
        return studentRepository.findByUnid(unid);
    }
}