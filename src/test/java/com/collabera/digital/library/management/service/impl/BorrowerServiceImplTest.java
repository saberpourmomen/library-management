package com.collabera.digital.library.management.service.impl;

import com.collabera.digital.library.management.dto.BookDto;
import com.collabera.digital.library.management.dto.BorrowerDto;
import com.collabera.digital.library.management.dto.LibraryManagementDTO;
import com.collabera.digital.library.management.exception.DuplicateBorrowerException;
import com.collabera.digital.library.management.exception.ResourceNotFoundException;
import com.collabera.digital.library.management.model.Book;
import com.collabera.digital.library.management.model.Borrower;
import com.collabera.digital.library.management.repository.IBorrowRepository;
import com.collabera.digital.library.management.service.IBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class BorrowerServiceImplTest {

    @Mock
    private IBookService bookService;

    @Mock
    private IBorrowRepository borrowRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BorrowerServiceImpl borrowerService;

    private Borrower borrower;
    private BorrowerDto borrowerDto;
    private Book book;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Sample Borrower
        borrower = new Borrower();
        borrower.setId(1L);
        borrower.setName("Test Borrower");

        borrowerDto = new BorrowerDto();
        borrowerDto.setId(1L);
        borrowerDto.setName("Test Borrower");

        // Sample Book
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setIsbn("123456789");

        bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Test Book");
        bookDto.setIsbn("123456789");
    }

    @Test
    void testRegisterBorrower_WhenBorrowerIsNew_Success() {
        
        when(modelMapper.map(borrowerDto, Borrower.class)).thenReturn(borrower);
        when(borrowRepository.save(borrower)).thenReturn(borrower);
        when(modelMapper.map(borrower, BorrowerDto.class)).thenReturn(borrowerDto);

       
        BorrowerDto result = borrowerService.register(borrowerDto);

        
        assertNotNull(result);
        assertEquals(borrowerDto.getName(), result.getName());
        verify(borrowRepository, times(1)).save(borrower);
    }

    @Test
    void testRegisterBorrower_WhenBorrowerAlreadyExists_ThrowsException() {
        
        when(modelMapper.map(borrowerDto, Borrower.class)).thenReturn(borrower);
        when(borrowRepository.save(borrower)).thenThrow(DataIntegrityViolationException.class);

        
        assertThrows(DuplicateBorrowerException.class, () -> borrowerService.register(borrowerDto));
    }

    @Test
    void testBorrowBook_WhenBorrowerAndBookExist_Success() {
        
        when(borrowRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookService.findById(1L)).thenReturn(Optional.of(book));
        when(bookService.bookBorrow(1L, borrower)).thenReturn(bookDto);
        when(borrowRepository.save(borrower)).thenReturn(borrower);
        when(modelMapper.map(borrower, BorrowerDto.class)).thenReturn(borrowerDto);

       
        LibraryManagementDTO result = borrowerService.borrowBook(1L, 1L);

        
        assertNotNull(result);
        assertEquals("book is borrowed successfully", result.getMessage());
        assertEquals(bookDto, result.getBookDto());
        assertEquals(borrowerDto, result.getBorrowerDto());
    }

    @Test
    void testBorrowBook_WhenBorrowerNotFound_ThrowsException() {
        
        when(borrowRepository.findById(1L)).thenReturn(Optional.empty());

        
        assertThrows(ResourceNotFoundException.class, () -> borrowerService.borrowBook(1L, 1L));
    }

    @Test
    void testBorrowBook_WhenBookNotFound_ThrowsException() {
        
        when(borrowRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookService.findById(1L)).thenReturn(Optional.empty());

        
        assertThrows(ResourceNotFoundException.class, () -> borrowerService.borrowBook(1L, 1L));
    }

    @Test
    void testReturnBook_WhenBorrowerAndBookExist_Success() {
        
        when(borrowRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookService.findById(1L)).thenReturn(Optional.of(book));
        when(bookService.bookReturn(1L)).thenReturn(bookDto);
        when(borrowRepository.save(borrower)).thenReturn(borrower);
        when(modelMapper.map(borrower, BorrowerDto.class)).thenReturn(borrowerDto);

       
        LibraryManagementDTO result = borrowerService.returnBook(1L, 1L);

        
        assertNotNull(result);
        assertEquals("book is returned successfully", result.getMessage());
        assertEquals(bookDto, result.getBookDto());
        assertEquals(borrowerDto, result.getBorrowerDto());
    }

    @Test
    void testReturnBook_WhenBorrowerNotFound_ThrowsException() {
        
        when(borrowRepository.findById(1L)).thenReturn(Optional.empty());

        
        assertThrows(ResourceNotFoundException.class, () -> borrowerService.returnBook(1L, 1L));
    }

    @Test
    void testReturnBook_WhenBookNotFound_ThrowsException() {
        
        when(borrowRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookService.findById(1L)).thenReturn(Optional.empty());

        
        assertThrows(ResourceNotFoundException.class, () -> borrowerService.returnBook(1L, 1L));
    }
}
