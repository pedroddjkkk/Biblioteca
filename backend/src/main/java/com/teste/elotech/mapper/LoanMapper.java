package com.teste.elotech.mapper;

import com.teste.elotech.dto.CreateLoanDTO;
import com.teste.elotech.model.Loan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanMapper {
    Loan toEntity(CreateLoanDTO createLoanDTO);
}