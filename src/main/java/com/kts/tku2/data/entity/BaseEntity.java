package com.kts.tku2.data.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@MappedSuperclass   //  : JPA의 엔티티 클래스가 상속받을 경우 자식 클래스에게 매핑 정보를 전달.
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    // @CreatedDate : 데이터 생성 날짜를 자동으로 주입하는 어노테이션
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // @LastModifiedDate : 데이터 수정 날짜를 자동으로 주입하는 어노테이션
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
