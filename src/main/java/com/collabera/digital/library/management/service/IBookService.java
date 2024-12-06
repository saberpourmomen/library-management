package com.collabera.digital.library.management.service;

import com.collabera.digital.library.management.dto.BookDto;
import com.collabera.digital.library.management.model.Book;
import com.collabera.digital.library.management.model.Borrower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IBookService {

    BookDto register(BookDto bookDto);

    Page<BookDto> list(Pageable pageable);

    BookDto findByIsbn(String isbn);
    Optional<Book> findById(Long id);

    BookDto bookBorrow(Long id, Borrower borrower);

    BookDto bookReturn(Long id);

    Page<BookDto> search(BookDto bookDto, Pageable pageable);
}
