package com.teste.elotech.controller;

import com.teste.elotech.dto.CreateBookDTO;
import com.teste.elotech.dto.UpdateBookDTO;
import com.teste.elotech.model.Book;
import com.teste.elotech.model.Loan;
import com.teste.elotech.service.BookService;
import com.teste.elotech.service.LoanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
@Tag(name = "Book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final LoanService loanService;

    @GetMapping
    public ResponseEntity<List<Book>> findAll() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @PostMapping
    public ResponseEntity<Book> create(@Valid @RequestBody CreateBookDTO createBookDTO) {
        return new ResponseEntity<>(bookService.create(createBookDTO), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Book> update(@RequestBody UpdateBookDTO updateBookDTO) {
        return ResponseEntity.ok(bookService.update(updateBookDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recommendation/{userId}")
    public ResponseEntity<List<Book>> findAllByRecommendation(@PathVariable Long userId) {
        List<Loan> userLoans = loanService.findAllByUserId(userId);
        return ResponseEntity.ok(bookService.findAllByRecommendation(userLoans));
    }
}
