package dev.benj.common.model.dto;

import dev.benj.common.model.FinancialAidPackage;
import dev.benj.common.model.Name;

import java.time.LocalDate;
import java.util.Set;

public record StudentWithFinancialAidAwards(
        Long id,
        Name name,
        LocalDate enrollmentDate,
        LocalDate graduationDate,
        Set<FinancialAidPackage> financialAidAwards
){}

