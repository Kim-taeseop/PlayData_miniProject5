package com.kts.tku2.data.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class CourseCreateDTO {

    @NonNull
    private String name;

    @NonNull
    private String day;

    @NonNull
    private Integer grades;

    @NonNull
    private Integer starttime;

    @NonNull
    private Integer endtime;

    @NonNull
    private Integer personnel;

    private String comment;
}
