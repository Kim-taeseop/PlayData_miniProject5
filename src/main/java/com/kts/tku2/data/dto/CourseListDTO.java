package com.kts.tku2.data.dto;

import lombok.Getter;

@Getter
public class CourseListDTO {

    private Long id;
    private String name;
    private Integer grades;

    public CourseListDTO(Long id, String name, Integer grades) {
        this.id = id;
        this.name = name;
        this.grades= grades;
    }
}
