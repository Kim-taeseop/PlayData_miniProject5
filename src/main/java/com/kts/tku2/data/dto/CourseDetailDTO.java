package com.kts.tku2.data.dto;


import com.kts.tku2.data.entity.Course;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Locale;

@NoArgsConstructor
@Getter
public class CourseDetailDTO {
    private String name;
    private String day;
    private Integer grades;
    private Integer starttime;
    private Integer endtime;
    private Integer personnel;
    private String comment;

    public CourseDetailDTO fromCourse(Course course){
        this.name = course.getName();
        this.day= course.getDay();
        this.grades = course.getGrades();
        this.starttime = course.getStarttime();
        this.endtime = course.getEndtime();
        this.personnel = course.getPersonnel();
        this.comment = course.getComment();
        return this;
    }

    public static CourseDetailDTO CourseFactory(Course course){
        CourseDetailDTO courseDetailDTO = new CourseDetailDTO();
        courseDetailDTO.fromCourse(course);
        return courseDetailDTO;
    }
}
