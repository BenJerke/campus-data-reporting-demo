package dev.benj.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    Name name;

    @NotNull
    LocalDate enrollmentDate;

    LocalDate graduationDate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JsonIgnore
    Set<FinancialAidPackage> financialAidPackages = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JsonIgnore
    Set<Semester> semestersEnrolled;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JsonIgnore
    Set<Course> courses;


    public void awardFinancialAid(FinancialAidPackage pkg){
        if(this.financialAidPackages == null){
            this.financialAidPackages = new HashSet<>();
        }
        this.financialAidPackages.add(pkg);
    }

    public void enrollForSemester(Semester semester){
        if(this.semestersEnrolled == null){
            this.semestersEnrolled = new HashSet<>();
        }
        this.semestersEnrolled.add(semester);
    }

    public void registerForCourse(Course course){
        if(this.courses == null){
            this.courses = new HashSet<>();
        }
        this.courses.add(course);
    }
}
