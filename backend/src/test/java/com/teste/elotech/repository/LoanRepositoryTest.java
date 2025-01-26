package com.teste.elotech.repository;

import com.teste.elotech.enums.LoanStatusEnum;
import com.teste.elotech.model.Book;
import com.teste.elotech.model.Loan;
import com.teste.elotech.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class LoanRepositoryTest {

    private Book book;

    private User user;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        book = Book.builder()
                .title("Test Book")
                .author("Test Author")
                .isbn("123-4567890123")
                .publishedAt(LocalDate.now())
                .category("Test Category")
                .build();
        book = bookRepository.save(book);

        user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .telephone("12312312")
                .registeredAt(LocalDate.now())
                .build();
        user = userRepository.save(user);
    }

    @Test
    void testSaveLoan() {
        Loan loan = Loan.builder()
                .user(user)
                .book(book)
                .status(LoanStatusEnum.PENDING)
                .loanDate(LocalDate.now())
                .devolutionDate(LocalDate.now().plusDays(14))
                .build();

        Loan savedLoan = loanRepository.save(loan);

        assertNotNull(savedLoan);
        assertNotNull(savedLoan.getId());
        assertEquals(1L, savedLoan.getUser().getId());
        assertEquals(1L, savedLoan.getUser().getId());
    }

    @Test
    void testFindLoanById() {
        Loan loan = Loan.builder()
                .user(user)
                .book(book)
                .status(LoanStatusEnum.PENDING)
                .loanDate(LocalDate.now())
                .devolutionDate(LocalDate.now().plusDays(14))
                .build();

        Loan savedLoan = loanRepository.save(loan);
        Optional<Loan> foundLoan = loanRepository.findById(savedLoan.getId());

        assertTrue(foundLoan.isPresent());
        assertEquals(savedLoan.getId(), foundLoan.get().getId());
    }

    @Test
    void testUpdateLoan() {
        Loan loan = Loan.builder()
                .user(user)
                .book(book)
                .status(LoanStatusEnum.PENDING)
                .loanDate(LocalDate.now())
                .devolutionDate(LocalDate.now().plusDays(14))
                .build();

        Loan savedLoan = loanRepository.save(loan);
        savedLoan.setDevolutionDate(LocalDate.now());
        savedLoan.setStatus(LoanStatusEnum.RETURNED);

        Loan updatedLoan = loanRepository.save(savedLoan);

        assertNotNull(updatedLoan);
        assertEquals(LocalDate.now(), updatedLoan.getDevolutionDate());
        assertEquals(LoanStatusEnum.RETURNED, updatedLoan.getStatus());
    }

    @Test
    void testDeleteLoan() {
        Loan loan = Loan.builder()
                .user(user)
                .book(book)
                .status(LoanStatusEnum.PENDING)
                .loanDate(LocalDate.now())
                .devolutionDate(LocalDate.now().plusDays(14))
                .build();

        Loan savedLoan = loanRepository.save(loan);
        loanRepository.deleteById(savedLoan.getId());

        Optional<Loan> foundLoan = loanRepository.findById(savedLoan.getId());

        assertFalse(foundLoan.isPresent());
    }
}