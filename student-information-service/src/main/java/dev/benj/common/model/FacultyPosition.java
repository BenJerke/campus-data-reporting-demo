package dev.benj.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class FacultyPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    String title;

    @NotNull
    BigDecimal salary;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JsonIgnore
    private Department department;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = FacultyMember_.POSITION)
    @JsonIgnore
    Set<FacultyMember> facultyMembers;
}
