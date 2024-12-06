package com.collabera.digital.library.management.controller;

import com.collabera.digital.library.management.commonUtils.CommonUtils;
import com.collabera.digital.library.management.dto.BookDto;
import com.collabera.digital.library.management.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController implements IBookController{
    @Autowired
    private IBookService bookService;

    @Override
    @PostMapping("/register")
    public ResponseEntity<BookDto> register(@RequestBody(required = true) BookDto bookDto) {
        return  ResponseEntity.status(HttpStatus.CREATED).body(bookService.register(bookDto));
    }

    @Override
    @PostMapping("/search")
    public ResponseEntity<Page<BookDto>> search(@RequestBody BookDto bookDto,
                                                @RequestParam(defaultValue = "0") int offset,
                                                @RequestParam(defaultValue = "10")int limit,
                                                @RequestParam(defaultValue = "-id")String sort) {
        Pageable pageable= PageRequest.of(offset,limit, CommonUtils.getSort(sort));
        return ResponseEntity.ok(bookService.search(bookDto,pageable));
    }

    @Override
    @GetMapping("/{isbn}")
    public ResponseEntity<BookDto> findByIsbn(@PathVariable String isbn) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findByIsbn(isbn));
    }

    @Override
    @GetMapping("/list")
    public ResponseEntity<Page<BookDto>> getAll(@RequestParam(defaultValue = "0") int offset,
                                                @RequestParam(defaultValue = "10")int limit,
                                                @RequestParam(defaultValue = "-id")String sort) {
        Pageable pageable=PageRequest.of(offset,limit,CommonUtils.getSort(sort));
        return ResponseEntity.status(HttpStatus.OK).body(bookService.list(pageable));
    }
}
