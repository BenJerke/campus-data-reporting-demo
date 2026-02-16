package dev.benj.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class FacultyMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String title;

    @NaturalId
    private Name name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = Course_.INSTRUCTOR)
    @JsonIgnore
    private Set<Course> courses;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JsonIgnore
    private FacultyPosition position;

    public void addCourse(Course course){
        if(this.courses == null){
            this.courses = new HashSet<>();
        }
        this.courses.add(course);
    }
}
