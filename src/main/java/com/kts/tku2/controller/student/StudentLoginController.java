package com.kts.tku2.controller.student;

import com.kts.tku2.data.entity.Student;

import com.kts.tku2.service.student.StudentLoginService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpSession;

@Controller
public class StudentLoginController {

    @Autowired
    private StudentLoginService studentLoginService;

    @GetMapping("/student/login")
    public String stuLoginForm() {
        return "student/login"; // 로그인 페이지 템플릿 이름
    }

    @PostMapping("/student/login")
    public String stuLogin(@RequestParam("unid") Integer unid, @RequestParam("pw") String pw, HttpSession session) {
        Student student = studentLoginService.findByUnid(unid);

        if (student != null && student.getPw().equals(pw)) {
            // 로그인 성공
            session.setAttribute("user", student); // 세션에 사용자 정보 저장
            return "redirect:/student/home"; // 로그인 후 홈 페이지로 이동
        } else {
            // 로그인 실패
            return "redirect:/student/login?error"; // 로그인 페이지로 다시 이동하고 에러 메시지 표시
        }
    }

    @GetMapping("/student/home")
    public String stuHome(HttpSession session) {
        Student student = (Student) session.getAttribute("user");
        if (student != null) {
            student.getSdepartment(); // sdepartment 엔티티를 초기화
        }
        return "student/home";
    }

    @GetMapping("/stuLogout")
    public String stuLogout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/student/login?stuLogout"; // 로그아웃 후 로그인 페이지로 이동
    }
}
