package com.collabera.digital.library.management.controller;

import com.collabera.digital.library.management.dto.BorrowerDto;
import com.collabera.digital.library.management.dto.LibraryManagementDTO;
import org.springframework.http.ResponseEntity;

public interface IBorrowerController {
    ResponseEntity<BorrowerDto> register(BorrowerDto borrowerDto);
    ResponseEntity<LibraryManagementDTO> borrowBook(Long borrowerId,Long bookId);
    ResponseEntity<LibraryManagementDTO> returnBook(Long borrowerId,Long bookId);
}
