package com.kts.tku2.controller.student;

import com.kts.tku2.data.entity.Course;
import com.kts.tku2.data.entity.Student;
import com.kts.tku2.service.student.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class StudentController {

    private final TimetableService timetableService;

    @Autowired
    public StudentController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    // 수강 테이블 생성
    @GetMapping("/student/timetable")
    public String viewTimetable(Model model, HttpSession session) {
        Student loggedInStudent = (Student) session.getAttribute("user");
        if (loggedInStudent != null) {
            // 학생 ID를 이용하여 시간표 데이터를 가져옵니다.
            List<Course> timetable = timetableService.generateTimetable(loggedInStudent.getId());
            model.addAttribute("timetable", timetable);
        }
        return "student/timetable"; // timetable.html 템플릿을 사용하여 시간표를 표시합니다.
    }
}
