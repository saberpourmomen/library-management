package com.collabera.digital.library.management.controller;

import com.collabera.digital.library.management.dto.BookDto;
import com.collabera.digital.library.management.service.IBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class BookControllerTest {
    @Mock
    private IBookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void register() {
        BookDto mockBook = new BookDto();
        mockBook.setTitle("Test Book");

        when(bookService.register(any(BookDto.class))).thenReturn(mockBook);

        ResponseEntity<BookDto> response = bookController.register(mockBook);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Test Book", response.getBody().getTitle());
    }

    @Test
    void search() {
        BookDto searchCriteria = new BookDto();
        Page<BookDto> mockPage = new PageImpl<>(Collections.singletonList(new BookDto()));
        Pageable pageable = PageRequest.of(0, 10);

        when(bookService.search(any(BookDto.class), any(Pageable.class))).thenReturn(mockPage);

        ResponseEntity<Page<BookDto>> response = bookController.search(searchCriteria, 0, 10, "-id");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void findByIsbn() {
        String isbn = "123";
        BookDto mockBook = new BookDto();
        mockBook.setIsbn(isbn);

        when(bookService.findByIsbn(isbn)).thenReturn(mockBook);

        ResponseEntity<BookDto> response = bookController.findByIsbn(isbn);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(isbn, response.getBody().getIsbn());
    }

    @Test
    void getAll() {
        Page<BookDto> mockPage = new PageImpl<>(Collections.singletonList(new BookDto()));
        Pageable pageable = PageRequest.of(0, 10);

        when(bookService.list(any(Pageable.class))).thenReturn(mockPage);

        ResponseEntity<Page<BookDto>> response = bookController.getAll(0, 10, "-id");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getTotalElements());
    }
}