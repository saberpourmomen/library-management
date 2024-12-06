package com.collabera.digital.library.management.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.collabera.digital.library.management.dto.BookDto;
import com.collabera.digital.library.management.exception.ISBNConflictException;
import com.collabera.digital.library.management.model.Book;
import com.collabera.digital.library.management.model.Borrower;
import com.collabera.digital.library.management.repository.IBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
class BookServiceImplTest {

    @Mock
    private IBookRepository bookRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private BookDto bookDto;
    private BookDto bookDto1;
    private Book book;
    private Book book1;
    private Book wrongBook;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        bookDto = new BookDto();
        bookDto.setIsbn("123456789");
        bookDto.setTitle("Test Book");
        bookDto.setAuthor("Test Author");
        bookDto.setIsBorrowed(Boolean.TRUE);
        bookDto1 = new BookDto();
        bookDto1.setIsbn("123456789");
        bookDto1.setTitle("Test Book");
        bookDto1.setAuthor("Test Author");
        bookDto1.setIsBorrowed(Boolean.FALSE);

        book = new Book();
        book.setIsbn("123456789");
        book.setTitle("Test Book");
        book.setTitle("Test Book");
        book.setIsBorrowed(Boolean.FALSE);

        book1 = new Book();
        book1.setIsbn("123456789");
        book1.setTitle("Test Book");
        book1.setTitle("Test Book");
        book1.setIsBorrowed(Boolean.TRUE);

        wrongBook = new Book();
        wrongBook.setIsbn("123456789");
        wrongBook.setTitle("Test Book1");
        wrongBook.setAuthor("Test Author");
        wrongBook.setIsBorrowed(Boolean.FALSE);
    }

    @Test
    void testRegisterBook_WhenBookAlreadyExistsWithDifferentTitleAndSameAuthor_ThrowsISBNConflictException() {
        List<Book> existingBooks = List.of(wrongBook);
        when(bookRepository.findByIsbn(bookDto.getIsbn())).thenReturn(Optional.of(existingBooks));

        ISBNConflictException exception = assertThrows(ISBNConflictException.class, () -> {
            bookService.register(bookDto);
        });
        assertEquals("Books with the same ISBN must have the same title and author", exception.getMessage());
    }

    @Test
    void testRegisterBook_WhenBookDoesNotExist_SuccessfulRegistration() {
        
        when(bookRepository.findByIsbn(bookDto.getIsbn())).thenReturn(Optional.empty());
        when(modelMapper.map(bookDto, Book.class)).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(modelMapper.map(book, BookDto.class)).thenReturn(bookDto);

      
        BookDto result = bookService.register(bookDto);

        
        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
        assertEquals("Test Author", result.getAuthor());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testFindByIsbn_WhenBookExists_ReturnsBookDto() {
        when(bookRepository.findByIsbn(bookDto.getIsbn())).thenReturn(Optional.of(List.of(book)));
        when(modelMapper.map(book, BookDto.class)).thenReturn(bookDto);
        
        BookDto result = bookService.findByIsbn(bookDto.getIsbn());
        
        assertNotNull(result);
        assertEquals(bookDto.getIsbn(), result.getIsbn());
    }

    @Test
    void testBookBorrow_WhenBookAvailable_SuccessfulBorrow() {
        Borrower borrower = new Borrower(); // assume Borrower class is defined
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book1);
        when(modelMapper.map(book1, BookDto.class)).thenReturn(bookDto);
        
        BookDto result = bookService.bookBorrow(1L, borrower);

        
        assertNotNull(result);
        assertTrue(result.getIsBorrowed());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testBookReturn_WhenBookAlreadyReturned_SuccessfulReturn() {
        
        book.setIsBorrowed(true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(modelMapper.map(book, BookDto.class)).thenReturn(bookDto1);

      
        BookDto result = bookService.bookReturn(1L);

        
        assertNotNull(result);
        assertFalse(result.getIsBorrowed());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testSearchBooks_ReturnsFilteredResults() {
        
        Pageable pageable = Pageable.unpaged();
        List<Book> books = List.of(book);
        Page<Book> page = new PageImpl<>(books);
        when(bookRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(modelMapper.map(book, BookDto.class)).thenReturn(bookDto);

      
        Page<BookDto> result = bookService.search(bookDto, pageable);

        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}