package com.kts.tku2.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// 교수 엔티티
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name="professor")
public class Professor extends BaseEntity{
    
    // 교수 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 아이디, 학번
    @Column(nullable = false)
    private Integer unid;

    // 비밀번호
    @Column(nullable = false)
    private String pw;

    // 이름
    @Column(nullable = false)
    private String name;
    
    @OneToMany(mappedBy = "professor")
    @ToString.Exclude
    private List<Course> courseList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="dep_id")
    @ToString.Exclude
    private Department pdepartment;    // 학생과 분리

    public Department getPdepartment() {
        return pdepartment;
    }
}
