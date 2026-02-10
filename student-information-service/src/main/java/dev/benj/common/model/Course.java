package dev.benj.common.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NaturalId
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    private FacultyMember instructor;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Semester> semesters;

    public void offerCourseInSemester(Semester semester){
        if(this.semesters == null){
            this.semesters = new HashSet<>();
        }
        this.semesters.add(semester);
    }
}
