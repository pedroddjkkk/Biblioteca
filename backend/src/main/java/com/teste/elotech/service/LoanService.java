package com.teste.elotech.service;

import com.teste.elotech.dto.CreateLoanDTO;
import com.teste.elotech.dto.UpdateLoanDTO;
import com.teste.elotech.enums.LoanStatusEnum;
import com.teste.elotech.exception.ResourceNotFoundException;
import com.teste.elotech.mapper.LoanMapper;
import com.teste.elotech.model.Book;
import com.teste.elotech.model.Loan;
import com.teste.elotech.repository.LoanRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final BookService bookService;
    private final UserService userService;

    public Loan create(CreateLoanDTO createLoanDTO) {
        Loan loan = loanMapper.toEntity(createLoanDTO);
        try {
            Book book = bookService.findById(createLoanDTO.getBookId()).orElseThrow();
            if (!book.getLoans().isEmpty()) {
                if (book.getLoans().stream().anyMatch(l -> l.getStatus().equals(LoanStatusEnum.PENDING))) {
                    throw new ResourceNotFoundException("Book already loaned");
                }
            }
            loan.setBook(book);
            loan.setUser(userService.findById(createLoanDTO.getUserId()).orElseThrow());
            return loanRepository.save(loan);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Book or User not found");
        }
    }

    public Loan update(UpdateLoanDTO updateLoanDTO) {
        try {
            Loan loan = loanRepository.findById(updateLoanDTO.getId()).orElseThrow();
            loan.setStatus(updateLoanDTO.getStatus());
            loan.setDevolutionDate(updateLoanDTO.getDevolutionDate());
            return loanRepository.save(loan);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Loan not found");
        }
    }

    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    public List<Loan> findAllByUserId(Long userId) {
        return loanRepository.findAllByUser_Id(userId);
    }
}
