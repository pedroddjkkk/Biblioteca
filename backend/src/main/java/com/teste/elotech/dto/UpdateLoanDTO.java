package com.teste.elotech.dto;

import com.teste.elotech.enums.LoanStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UpdateLoanDTO {
    @NotNull
    private Long id;

    @NotNull
    private LocalDate devolutionDate;

    @NotNull
    private LoanStatusEnum status;
}
