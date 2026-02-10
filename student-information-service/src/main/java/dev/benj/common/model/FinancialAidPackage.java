package dev.benj.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class FinancialAidPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NaturalId
    String packageName;

    @NotNull
    BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @NaturalId
    Semester semesterOffered;
}
