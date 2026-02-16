package dev.benj.common.model.dto;

import dev.benj.common.model.Name;
import dev.benj.common.model.Semester;

import java.util.Set;

public record StudentWithSemesters(
        Long id,
        Name name,
        Set<Semester> semesters
){}