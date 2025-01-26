package com.teste.elotech.dto;

import com.teste.elotech.enums.LoanStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class CreateLoanDTO {
    @NotNull
    private LocalDate loanDate;

    @NotNull
    private LocalDate devolutionDate;

    @NotNull
    private LoanStatusEnum status;

    @ManyToOne
    private Long userId;

    @ManyToOne
    private Long bookId;
}
