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
import com.collabera.digital.library.management.service.IBorrowerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BorrowerServiceImpl implements IBorrowerService {
    private final IBookService bookService;
    private final IBorrowRepository borrowRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public BorrowerServiceImpl(IBookService bookService, IBorrowRepository borrowRepository, ModelMapper modelMapper) {
        this.bookService = bookService;
        this.borrowRepository = borrowRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public BorrowerDto register(BorrowerDto borrowerDto) {
        try{
            Borrower borrower=modelMapper.map(borrowerDto, Borrower.class);
            borrower= borrowRepository.save(borrower);
            return modelMapper.map(borrower, BorrowerDto.class);
        }catch (DataIntegrityViolationException e){
            throw new DuplicateBorrowerException("Borrower is already exist!");
        }

    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED)
    public LibraryManagementDTO borrowBook(Long borrowerId, Long bookId) {
        Borrower borrower = borrowRepository.findById(borrowerId).orElseThrow(() -> new ResourceNotFoundException("Borrower not found!"));
        Book book = bookService.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found!"));
        BookDto bookDto = bookService.bookBorrow(bookId,borrower);
        borrower.getBorrowedBooks().add(book);
        borrower = borrowRepository.save(borrower);
        BorrowerDto borrowerDto=modelMapper.map(borrower, BorrowerDto.class);
        return  new LibraryManagementDTO(bookDto,borrowerDto,"book is borrowed successfully");
    }

    @Override
    public LibraryManagementDTO returnBook(Long borrowerId, Long bookId) {
        Borrower borrower = borrowRepository.findById(borrowerId).orElseThrow(() -> new ResourceNotFoundException("Borrower not found!"));
        Book book = bookService.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found!"));
        BookDto bookDto = bookService.bookReturn(bookId);
        borrower.getBorrowedBooks().remove(book);
        borrower=borrowRepository.save(borrower);
        BorrowerDto borrowerDto=modelMapper.map(borrower, BorrowerDto.class);
        return  new LibraryManagementDTO(bookDto,borrowerDto,"book is returned successfully");
    }
}
