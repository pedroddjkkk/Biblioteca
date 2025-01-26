package com.teste.elotech.controller;

import com.teste.elotech.dto.CreateLoanDTO;
import com.teste.elotech.dto.UpdateLoanDTO;
import com.teste.elotech.model.Loan;
import com.teste.elotech.service.LoanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loan")
@Tag(name = "Loan")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<Loan> create(@Valid @RequestBody CreateLoanDTO createLoanDTO) {
        return new ResponseEntity<>(loanService.create(createLoanDTO), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Loan> update(@Valid @RequestBody UpdateLoanDTO updateLoanDTO) {
        return ResponseEntity.ok(loanService.update(updateLoanDTO));
    }
}
