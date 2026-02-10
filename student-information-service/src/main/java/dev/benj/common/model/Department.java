package dev.benj.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @NaturalId
    String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = FacultyPosition_.DEPARTMENT, cascade = CascadeType.ALL)
    @JsonIgnore
    Set<FacultyPosition> positions;
    public void addPosition(FacultyPosition facultyPosition){
        if(this.positions == null){
            this.positions = new HashSet<>();
        }
        this.positions.add(facultyPosition);
    }
}
