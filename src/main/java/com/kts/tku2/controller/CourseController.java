package com.kts.tku2.controller;

import com.kts.tku2.data.dto.CourseCreateDTO;
import com.kts.tku2.data.entity.*;
import com.kts.tku2.data.repository.CourseRepository;
import com.kts.tku2.data.repository.EnrollmentRepository;
import com.kts.tku2.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
// @RequestMapping("/professor")
public class CourseController {

    private final CourseService courseService;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public CourseController(CourseService courseService,
                            EnrollmentRepository enrollmentRepository,
                            CourseRepository courseRepository){
        this.courseService = courseService;
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping("/professor/coursecreate")
    public String courceCreate(){
        //  model.addAttribute("courseCreateDTO", new CourseCreateDTO());
        return "professor/coursecreate";
    }

    // 과목 등록(교수)
    @PostMapping("/professor/coursecreate")
    public String insertCourse(CourseCreateDTO courseCreateDTO, HttpSession session) {

        Course course = new Course();
        course.setName(courseCreateDTO.getName());
        course.setDay(courseCreateDTO.getDay());
        course.setGrades(courseCreateDTO.getGrades());
        course.setStarttime(courseCreateDTO.getStarttime());
        course.setEndtime(courseCreateDTO.getEndtime());
        course.setPersonnel(courseCreateDTO.getPersonnel());
        course.setSeat(courseCreateDTO.getPersonnel()); // 첫 설정은 personnel 이랑 같음
        course.setComment(courseCreateDTO.getComment());

        Professor loggedInProfessor = (Professor) session.getAttribute("admin");
        course.setProfessor(loggedInProfessor);

        courseService.createCourse(course);

        return "redirect:/professor/home";
    }
    //insert 메소드는 POST /professor/coursecreate 요청을 받아서
    // JPA를 이용해 데이터베이스에 저장한 후 /professor/read/{courseId} 주소로 이동합니다.

    // 전체 과목 리스트(교수)
    @GetMapping("/professor/courselist")
    public String courseList(Model model){
        List<Course> courseList = courseService.courseList();
        model.addAttribute("courseList", courseList);
        return "professor/courselist";
    }

    // 담당 과목 리스트(교수)
    @GetMapping("/professor/courselistbyprofessor")
    public String courseListByProfessor(Model model, HttpSession session){
        Long loggedInProfessorId = ((Professor) session.getAttribute("admin")).getId();

        List<Course> courseListByProfessor = courseService.courseListByProfessor(loggedInProfessorId);

        model.addAttribute("courseListByProfessor", courseListByProfessor);
        return "professor/courselistbyprofessor";
    }

    // 학생의 과목 리스트(학생)
    @GetMapping("/student/studentcourselist")
    public String studentCourseList(Model model, HttpSession session){
        Student loggedInStudent = (Student) session.getAttribute("user");
        if (loggedInStudent != null) {
            // 학생이 속한 학과 찾기
            Department studentDepartment = loggedInStudent.getSdepartment();

            // 학과에 속한 교수들 찾기
            List<Professor> professors = courseService.professorListByDepartment(studentDepartment);

            // 교수별 과목 리스트를 저장할 리스트
            List<List<Course>> professorCoursesList = new ArrayList<>();

            for (Professor professor : professors) {
                Long professorId = professor.getId();
                // 교수별 과목 리스트 가져오기
                List<Course> professorCourses = courseService.studentCourseList(professorId);
                professorCoursesList.add(professorCourses);
            }

            model.addAttribute("professors", professors);
            model.addAttribute("professorCoursesList", professorCoursesList);

            // 수강 신청을 위한 id 가져옴
            Long studentId = loggedInStudent.getId();
            model.addAttribute("studentId", studentId);

            return "student/studentcourselist";
        } else {
            // 로그인하지 않은 경우 로그인 페이지로 리다이렉트 또는 다른 처리 수행
            return "redirect:/student/login";
        }
    }

    // 중복 검사 메서드
    private boolean isCourseOverlapping(List<Enrollment> studentEnrollments, Course currentCourse) {
        int currentCourseStartTime = currentCourse.getStarttime();
        int currentCourseEndTime = currentCourse.getEndtime();
        String currentCourseDay = currentCourse.getDay();

        for (Enrollment enrollment : studentEnrollments) {
            Course enrolledCourse = enrollment.getCourse();
            if (enrolledCourse != null) {
                int enrolledCourseStartTime = enrolledCourse.getStarttime();
                int enrolledCourseEndTime = enrolledCourse.getEndtime();
                String enrolledCourseDay = enrolledCourse.getDay();

                if (currentCourseDay.equals(enrolledCourseDay)) {
                    if (currentCourseStartTime == enrolledCourseStartTime && currentCourseEndTime == enrolledCourseEndTime) {
                        return true; // 완전히 중복되는 경우
                    } else if (currentCourseStartTime >= enrolledCourseStartTime && currentCourseStartTime < enrolledCourseEndTime) {
                        return true; // 시작 시간 중복
                    } else if (currentCourseEndTime > enrolledCourseStartTime && currentCourseEndTime <= enrolledCourseEndTime) {
                        return true; // 종료 시간 중복
                    }
                }
            }
        }
        return false; // 중복 없음
    }

    // 수강 신청
    @PostMapping("/student/addcourse/{id}")
    public String addToCourse(@PathVariable Long id, HttpSession session, Model model) {
        Student loggedInStudent = (Student) session.getAttribute("user");

        // 현재 시간 구하기
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 허용되는 수강 신청 시간 범위 설정
        LocalDateTime startDateTime = LocalDateTime.of(2023, 9, 21, 11, 0); // 시작 시간
        LocalDateTime endDateTime = LocalDateTime.of(2023, 9, 26, 20, 0); // 종료 시간

        // 현재 시간이 허용 범위 내에 있는지 확인
        if (currentDateTime.isBefore(startDateTime) || currentDateTime.isAfter(endDateTime)) {
            return "redirect:/student/studentcourselist?timeError";
        }

        if (loggedInStudent != null) {
            Course course = courseService.findCourseById(id);

            if (course != null) {
                // 이미 수강한 강좌인지 확인
                Enrollment existingEnrollment = enrollmentRepository.findByStudentIdAndCourseName(loggedInStudent.getId(), course.getName());

                // 이미 수강한 강좌 목록 가져오기
                List<Enrollment> studentEnrollments = enrollmentRepository.findByStudentId(loggedInStudent.getId());

                // 중복 수강 검사
                if (isCourseOverlapping(studentEnrollments, course)) {
                    return "redirect:/student/studentcourselist?duplicationError";
                } else {
                    Course enrolledCourse = courseService.enrollStudentInCourse(id);
                    // 중복이 없으면 수강 신청 처리
                    Enrollment enrollment = new Enrollment();
                    enrollment.setStudentId(loggedInStudent.getId());
                    enrollment.setCourseName(course.getName());
                    enrollment.setDay(course.getDay());
                    enrollment.setGrades(course.getGrades());
                    enrollment.setStarttime(course.getStarttime());
                    enrollment.setEndtime(course.getEndtime());
                    enrollment.setCourse(course); // 연관된 Course 설정
                    // 신청 가능 자리 업데이트
                    if (course.getSeat() > 0) {
                        course.setSeat(course.getSeat() - 1);
                    } else {
                        return "redirect:/student/studentcourselist?seatError";
                    }
                    // 수강신청 정보 저장
                    enrollmentRepository.save(enrollment);
                    // 강좌 정보 저장
                    courseRepository.save(course);
                }
            }
        }
        return "redirect:/student/studentcourselist";
    }

    // 과목 카트 보기
    @GetMapping("/student/coursecart")
    public String viewCart(Model model, HttpSession session){
        Student loggedInStudent = (Student) session.getAttribute("user");
        if(loggedInStudent != null){
            List<Enrollment> enrollments =  enrollmentRepository.findByStudentId(loggedInStudent.getId());
            model.addAttribute("enrollments", enrollments);
        }

        return "student/coursecart";
    }

    // 담당 과목 상세보기 (교수)
    @GetMapping("/professor/coursedetail/{id}")
    public String courseDetail(@PathVariable Long id, Model model) {
        // courseId를 사용하여 과목의 상세 정보 가져오기
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        return "professor/coursedetail"; // 과목 상세 정보를 보여줄 뷰 페이지
    }

    // 과목 상세보기 (학생)
    @GetMapping("/student/studentcoursedetail/{id}")
    public String studentCourseDetail(@PathVariable Long id, Model model){
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        return "student/studentcoursedetail";
    }

    // 과목 수정폼 이동 (교수)
    @GetMapping("/professor/courseupdate/{id}")
    public String updateForm(@PathVariable Long id, Model model){
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        return "professor/courseupdate";
    }

    // 과목 수정 로직 처리 (교수)
    @PostMapping("/professor/courseupdate")
    public String updateCourse(@ModelAttribute Course course){
        courseService.updateCourse(course);
        return "redirect:/professor/coursedetail/" + course.getId();
    }

    // 과목 삭제 (교수)
    @GetMapping("/professor/coursedelete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes){
        courseService.deleteCourse(id);
        redirectAttributes.addFlashAttribute("successMessage", "과목이 삭제되었습니다.");
        return "redirect:/professor/courselistbyprofessor";
    }

    // 수강신청한 과목 삭제 (학생)
    @PostMapping("/student/remove/{id}")
    public String removeEnrollment(@PathVariable Long id, HttpSession session){
        Student loggedInStudent = (Student) session.getAttribute("user");

        if (loggedInStudent != null) {
            // 해당 수강 신청을 데이터베이스에서 삭제
            Enrollment enrollmentToDelete = enrollmentRepository.findById(id).orElse(null);

            if (enrollmentToDelete != null) {
                Course course = enrollmentToDelete.getCourse();

                // 해당 과목의 자리를 1 증가시킴
                if (course != null) {
                    course.setSeat(course.getSeat() + 1);
                    courseRepository.save(course);
                }

                // 삭제 수행
                enrollmentRepository.deleteById(id);
            }
        }
        return "redirect:/student/coursecart";
    }
}
