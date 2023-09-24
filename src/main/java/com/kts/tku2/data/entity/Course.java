package com.kts.tku2.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// 과목 엔티티
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name="course")
public class Course extends BaseEntity{

    // 과목 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 과목명
    @Column(nullable = false)
    private String name;

    // 수업 요일
    @Column(nullable = false)
    private String day;

    // 학점
    @Column(nullable = false)
    private Integer grades;

    // 수업 시작 시간
    @Column(nullable = false)
    private Integer starttime;

    // 수업 종료 시간
    @Column(nullable = false)
    private Integer endtime;

    // 총 수강인원
    @Column(nullable = false)
    private Integer personnel;

    // 현재 남은 자리
    @Column
    private Integer seat;

    // 과목 주요 설명 null 값 허용
    @Column(nullable = true)
    private String comment;

    @ManyToOne
    @JoinColumn(name="pro_id")
    @ToString.Exclude
    private Professor professor;

    @OneToMany(mappedBy = "course")
    @ToString.Exclude
    private List<Enrollment> enrollmentList = new ArrayList<>();

    // 총 수강인원과 현재 신청 가능한 자리 초기화
    public Course(String name, String day, Integer grades, Integer starttime, Integer endtime,
                  Integer personnel, Integer seat, String comment, Professor professor) {
        super();
        this.name = name;
        this.day = day;
        this.grades = grades;
        this.starttime = starttime;
        this.endtime = endtime;
        this.personnel = personnel;
        this.seat = personnel;
        this.comment = comment;
        this.professor = professor;
    }

    public void updateSeat() {
        if (this.enrollmentList != null) {
            this.seat = this.personnel - this.enrollmentList.size();
        } else {
            this.seat = this.personnel;
        }
    }


    public Course(Object o, String name, String day, Integer grades, Integer starttime, Integer endtime, Integer personnel, String comment, Object o1) {
        super();
    }
}
