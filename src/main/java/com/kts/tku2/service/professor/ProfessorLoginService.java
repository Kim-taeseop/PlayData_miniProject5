package com.kts.tku2.service.professor;

import com.kts.tku2.data.entity.Professor;
import com.kts.tku2.data.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfessorLoginService {

    @Autowired
    private ProfessorRepository professorRepository;

    public Professor findByProunid(Integer unid){
        return professorRepository.findByUnid(unid);
    }
}
