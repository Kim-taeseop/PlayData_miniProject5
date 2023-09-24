package com.kts.tku2.data.entity;

import lombok.*;

import javax.persistence.*;

//학생 엔티티
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name="student")
public class Student extends BaseEntity{

    // 학생번호
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

    // 학년
    @Column(nullable = false)
    private Integer year;

    @ManyToOne(fetch = FetchType.EAGER)  // 즉시 로딩
    @JoinColumn(name="dep_id")
    @ToString.Exclude
    private Department sdepartment;    // 교수와 이름 분리

    // 메서드의 인자로 받은 비밀번호 값과 현 개체의 비밀번호 값을 비교하여 boolean 값 반환.
    public boolean checkPassword(String pw){
        return this.pw.equals(pw);
    }

    public Department getSdepartment() {
        return sdepartment;
    }
}
