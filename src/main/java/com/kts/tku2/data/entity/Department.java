package com.kts.tku2.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// 학과 엔티티
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name="department")
public class Department extends BaseEntity{

    // 학과 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 학과명
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "pdepartment")
    @ToString.Exclude
    private List<Professor> professorList = new ArrayList<>();

    @OneToMany(mappedBy = "sdepartment")
    @ToString.Exclude
    private List<Student> studentList = new ArrayList<>();
}
