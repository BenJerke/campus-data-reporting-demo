package dev.benj.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;

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

    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    FacultyMember facultyMember;
}
