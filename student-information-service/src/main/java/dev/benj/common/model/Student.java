package dev.benj.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Student implements Serializable {

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
    Set<Semester> semestersAttended;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JsonIgnore
    Set<Course> courses;


    public void awardFinancialAid(FinancialAidPackage pkg){
        if(this.financialAidPackages == null){
            this.financialAidPackages = new HashSet<>();
        }
        this.financialAidPackages.add(pkg);
    }

    public void registerForSemester(Semester semester){
        if(this.semestersAttended == null){
            this.semestersAttended = new HashSet<>();
        }
        this.semestersAttended.add(semester);
    }

    public void registerForCourse(Course course){
        if(this.courses == null){
            this.courses = new HashSet<>();
        }
        this.courses.add(course);
    }
}
