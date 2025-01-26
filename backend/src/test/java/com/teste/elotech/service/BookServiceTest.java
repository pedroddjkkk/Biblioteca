package com.teste.elotech.service;

import com.teste.elotech.dto.CreateBookDTO;
import com.teste.elotech.dto.UpdateBookDTO;
import com.teste.elotech.exception.ResourceNotFoundException;
import com.teste.elotech.mapper.BookMapper;
import com.teste.elotech.model.Book;
import com.teste.elotech.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    private CreateBookDTO createBookDTO;
    private UpdateBookDTO updateBookDTO;
    private Book book;

    @BeforeEach
    void setUp() {
        createBookDTO = CreateBookDTO.builder()
                .title("Test Book")
                .author("Test Author")
                .build();

        updateBookDTO = UpdateBookDTO.builder()
                .id(1L)
                .title("Updated Test Book")
                .author("Updated Test Author")
                .build();

        book = Book.builder()
                .id(1L)
                .title("Test Book")
                .author("Test Author")
                .build();
    }

    @Test
    void testCreateBookSuccess() {
        when(bookMapper.toEntity(any(CreateBookDTO.class))).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book createdBook = bookService.create(createBookDTO);

        assertNotNull(createdBook);
        assertEquals(book.getId(), createdBook.getId());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testUpdateBookSuccess() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toEntity(any(UpdateBookDTO.class))).thenReturn(book);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        Book updatedBook = bookService.update(updateBookDTO);

        assertNotNull(updatedBook);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testUpdateBookNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                bookService.update(updateBookDTO)
        );

        assertEquals("Book not found", exception.getMessage());
    }

    @Test
    void testFindByIdSuccess() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        Book foundBook = bookService.findById(1L).orElse(null);

        assertNotNull(foundBook);
        assertEquals(book.getId(), foundBook.getId());
        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    void testFindByIdNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Book> foundBook = bookService.findById(1L);

        assertTrue(foundBook.isEmpty());
        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    void testFindAll() {
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<Book> books = bookService.findAll();

        assertNotNull(books);
        assertEquals(1, books.size());
        verify(bookRepository, times(1)).findAll();
    }
}