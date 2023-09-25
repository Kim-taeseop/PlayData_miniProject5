package com.kts.tku2.service;


import com.kts.tku2.data.entity.Course;
import com.kts.tku2.data.entity.Department;
import com.kts.tku2.data.entity.Professor;
import com.kts.tku2.data.repository.CourseRepository;

import com.kts.tku2.data.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final ProfessorRepository professorRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, ProfessorRepository professorRepository){
        this.courseRepository = courseRepository;
        this.professorRepository= professorRepository;
    }

    public void createCourse(Course course){
        courseRepository.save(course);
    }

    //전 과목 리스트(교수)
    public List<Course> courseList(){
        return courseRepository.findAll();
    }

    // 담당 과목 리스트(교수)
    public List<Course> courseListByProfessor(Long professorId){
        return courseRepository.findByProfessorId(professorId);
    }

    // dep_id 를 이용해 학과에 포함된 교수 찾기 (학생)
    public List<Professor> professorListByDepartment(Department department){
        return professorRepository.findByPdepartment(department);
    }

    // 학생 과목 리스트(학생)
    public List<Course> studentCourseList(Long professorId){
        return courseRepository.findByProfessorId(professorId);
    }

    // 과목 ID로 과목찾기 수강신청(학생)
    public Course findCourseById(Long id){
        return courseRepository.findById(id).orElse(null);
    }

    // 신청 가능 인원
    public Course enrollStudentInCourse(Long courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);

        if (course != null && course.getSeat() > 0) {
            course.updateSeat();
            courseRepository.save(course);
            return course;
        }
        return null;
    }

    //과목 상세보기 (교수)(학생)
    public Course getCourseById(Long id) {
        // CourseRepository에서 findById 메서드를 사용하여 과목 조회
        return courseRepository.findById(id).orElseThrow();
    }

    // 과목 정보 수정 (교수)
    public void updateCourse(Course course){
        Course existingCourse = courseRepository.findById(course.getId()).orElse(null);
        if(existingCourse != null){
            existingCourse.setName(course.getName());
            existingCourse.setGrades(course.getGrades());
            existingCourse.setStarttime(course.getStarttime());
            existingCourse.setEndtime(course.getEndtime());
            existingCourse.setPersonnel(course.getPersonnel());
            existingCourse.setComment(course.getComment());
            courseRepository.save(existingCourse);
        }
    }

    // 해당 과목 삭제 (교수)
    public void deleteCourse(Long id){
        courseRepository.deleteById(id);
    }
}
