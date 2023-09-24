package com.kts.tku2.data.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@Table(name="Enrollment")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;

    private String courseName;

    private String day;

    private Integer starttime;

    private Integer endtime;

    private Integer grades;

    // Course 필드 추가
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    // Getter 및 Setter 메서드 추가
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
