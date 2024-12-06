package com.collabera.digital.library.management.controller;

import com.collabera.digital.library.management.dto.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface IBookController {
    ResponseEntity<BookDto> register(BookDto bookDto);
    ResponseEntity<Page<BookDto>> search(BookDto bookDto,int offset,int limit,String sort);
    ResponseEntity<BookDto> findByIsbn(String isbn);
    ResponseEntity<Page<BookDto>> getAll(int offset,int limit,String sort);
}
