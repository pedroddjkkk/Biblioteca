package com.teste.elotech.service;

import com.teste.elotech.dto.CreateBookDTO;
import com.teste.elotech.dto.UpdateBookDTO;
import com.teste.elotech.exception.ResourceNotFoundException;
import com.teste.elotech.mapper.BookMapper;
import com.teste.elotech.model.Book;
import com.teste.elotech.model.Loan;
import com.teste.elotech.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.StaleObjectStateException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    public Book create(CreateBookDTO createBookDTO) {
        Book book = bookMapper.toEntity(createBookDTO);
        return bookRepository.save(book);
    }

    public Book update(UpdateBookDTO updateBookDTO) {
        bookRepository.findById(updateBookDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        Book book = bookMapper.toEntity(updateBookDTO);
        return bookRepository.save(book);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> findAllByRecommendation(List<Loan> userLoans) {
        List<String> categories = userLoans.stream().map(Loan::getBook).map(Book::getCategory).toList();

        if (categories.isEmpty()) {
            return bookRepository.findAll();
        } else {
            return bookRepository.findAllByCategoryNotIn(categories);
        }
    }
}
