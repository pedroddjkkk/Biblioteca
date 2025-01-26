package com.teste.elotech.service;

import com.teste.elotech.dto.CreateLoanDTO;
import com.teste.elotech.dto.UpdateLoanDTO;
import com.teste.elotech.enums.LoanStatusEnum;
import com.teste.elotech.exception.ResourceNotFoundException;
import com.teste.elotech.mapper.LoanMapper;
import com.teste.elotech.model.Book;
import com.teste.elotech.model.Loan;
import com.teste.elotech.model.User;
import com.teste.elotech.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private BookService bookService;

    @Mock
    private UserService userService;

    @InjectMocks
    private LoanService loanService;

    private CreateLoanDTO createLoanDTO;
    private UpdateLoanDTO updateLoanDTO;
    private Loan loan;
    private Book book;
    private User user;

    @BeforeEach
    void setUp() {
        createLoanDTO = CreateLoanDTO.builder()
                .loanDate(LocalDate.now())
                .devolutionDate(LocalDate.now().plusDays(7))
                .status(LoanStatusEnum.PENDING)
                .userId(1L)
                .bookId(1L)
                .build();

        updateLoanDTO = UpdateLoanDTO.builder()
                .id(1L)
                .status(LoanStatusEnum.RETURNED)
                .devolutionDate(LocalDate.now().plusDays(7))
                .build();

        loan = Loan.builder()
                .id(1L)
                .loanDate(LocalDate.now())
                .devolutionDate(LocalDate.now().plusDays(7))
                .status(LoanStatusEnum.PENDING)
                .build();

        book = Book.builder()
                .id(1L)
                .loans(new ArrayList<>(List.of()))
                .build();

        user = User.builder()
                .id(1L)
                .build();
    }

    @Test
    void testCreateLoanSuccess() {
        when(loanMapper.toEntity(any(CreateLoanDTO.class))).thenReturn(loan);
        when(bookService.findById(anyLong())).thenReturn(Optional.of(book));
        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        Loan createdLoan = loanService.create(createLoanDTO);

        assertNotNull(createdLoan);
        assertEquals(loan.getId(), createdLoan.getId());
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void testCreateLoanBookAlreadyLoaned() {
        book.getLoans().add(loan);
        when(loanMapper.toEntity(any(CreateLoanDTO.class))).thenReturn(loan);
        when(bookService.findById(anyLong())).thenReturn(Optional.of(book));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                loanService.create(createLoanDTO)
        );

        assertEquals("Book already loaned", exception.getMessage());
    }

    @Test
    void testCreateLoanBookOrUserNotFound() {
        when(loanMapper.toEntity(any(CreateLoanDTO.class))).thenReturn(loan);
        when(bookService.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                loanService.create(createLoanDTO)
        );

        assertEquals("Book or User not found", exception.getMessage());
    }

    @Test
    void testUpdateLoanSuccess() {
        when(loanRepository.findById(anyLong())).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        Loan updatedLoan = loanService.update(updateLoanDTO);

        assertNotNull(updatedLoan);
        assertEquals(updateLoanDTO.getStatus(), updatedLoan.getStatus());
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void testUpdateLoanNotFound() {
        when(loanRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                loanService.update(updateLoanDTO)
        );

        assertEquals("Loan not found", exception.getMessage());
    }
}