package com.kts.tku2.service.student;

import com.kts.tku2.data.entity.Course;
import com.kts.tku2.data.entity.Enrollment;
import com.kts.tku2.service.student.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TimetableService {

    private final EnrollmentService enrollmentService;

    @Autowired
    public TimetableService(EnrollmentService enrollmentService){
        this.enrollmentService = enrollmentService;
    }

    // 수강 시간표 작성
    public List<Course> generateTimetable(Long studentId){
        List<Enrollment> studentEnrollments = enrollmentService.getStudentEnrollments(studentId);
        List<Course> timetable = new ArrayList<>();

        for(Enrollment enrollment : studentEnrollments){
            Course course = enrollment.getCourse();
            if(course != null){
                timetable.add(course);
            }
        }
        return timetable;
    }
}
