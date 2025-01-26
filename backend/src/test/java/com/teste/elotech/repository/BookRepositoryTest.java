package com.teste.elotech.repository;

import com.teste.elotech.model.Book;
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
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void testSaveBook() {
        Book book = Book.builder()
                .title("Test Book")
                .author("Test Author")
                .isbn("123-4567890123")
                .publishedAt(LocalDate.now())
                .category("Test Category")
                .build();

        Book savedBook = bookRepository.save(book);

        assertNotNull(savedBook);
        assertNotNull(savedBook.getId());
        assertEquals("Test Book", savedBook.getTitle());
        assertEquals("Test Author", savedBook.getAuthor());
        assertEquals("123-4567890123", savedBook.getIsbn());
        assertEquals(LocalDate.now(), savedBook.getPublishedAt());
        assertEquals("Test Category", savedBook.getCategory());
    }

    @Test
    void testFindBookById() {
        Book book = Book.builder()
                .title("Test Book")
                .author("Test Author")
                .isbn("123-4567890123")
                .publishedAt(LocalDate.now())
                .category("Test Category")
                .build();

        Book savedBook = bookRepository.save(book);
        Optional<Book> foundBook = bookRepository.findById(savedBook.getId());

        assertTrue(foundBook.isPresent());
        assertEquals(savedBook.getId(), foundBook.get().getId());
    }

    @Test
    void testUpdateBook() {
        Book book = Book.builder()
                .title("Test Book")
                .author("Test Author")
                .isbn("123-4567890123")
                .publishedAt(LocalDate.now())
                .category("Test Category")
                .build();

        Book savedBook = bookRepository.save(book);
        savedBook.setTitle("Updated Test Book");
        savedBook.setAuthor("Updated Test Author");
        savedBook.setIsbn("123-4567890124");
        savedBook.setPublishedAt(LocalDate.now().minusDays(1));
        savedBook.setCategory("Updated Test Category");

        Book updatedBook = bookRepository.save(savedBook);

        assertNotNull(updatedBook);
        assertEquals("Updated Test Book", updatedBook.getTitle());
        assertEquals("Updated Test Author", updatedBook.getAuthor());
        assertEquals("123-4567890124", updatedBook.getIsbn());
        assertEquals(LocalDate.now().minusDays(1), updatedBook.getPublishedAt());
        assertEquals("Updated Test Category", updatedBook.getCategory());
    }

    @Test
    void testDeleteBook() {
        Book book = Book.builder()
                .title("Test Book")
                .author("Test Author")
                .isbn("123-4567890123")
                .publishedAt(LocalDate.now())
                .category("Test Category")
                .build();

        Book savedBook = bookRepository.save(book);
        bookRepository.deleteById(savedBook.getId());

        Optional<Book> foundBook = bookRepository.findById(savedBook.getId());

        assertFalse(foundBook.isPresent());
    }
}