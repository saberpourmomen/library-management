package com.collabera.digital.library.management.controller;

import com.collabera.digital.library.management.dto.BookDto;
import com.collabera.digital.library.management.dto.BorrowerDto;
import com.collabera.digital.library.management.dto.LibraryManagementDTO;
import com.collabera.digital.library.management.service.IBorrowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrower")
public class BorrowerController implements IBorrowerController{
    @Autowired
    private IBorrowerService borrowerService;

    @Override
    @PostMapping("register")
    public ResponseEntity<BorrowerDto> register(@RequestBody BorrowerDto borrowerDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(borrowerService.register(borrowerDto));
    }

    @Override
    @GetMapping("/{borrowerId}/borrow/{bookId}")
    public ResponseEntity<LibraryManagementDTO> borrowBook(@PathVariable Long borrowerId,@PathVariable Long bookId) {
        return ResponseEntity.status(HttpStatus.OK).body(borrowerService.borrowBook(borrowerId,bookId));
    }

    @Override
    @GetMapping("/{borrowerId}/return/{bookId}")
    public ResponseEntity<LibraryManagementDTO> returnBook(@PathVariable Long borrowerId,@PathVariable Long bookId) {
        return ResponseEntity.status(HttpStatus.OK).body(borrowerService.returnBook(borrowerId,bookId));
    }
}
