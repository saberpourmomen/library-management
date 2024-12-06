package com.collabera.digital.library.management.service;

import com.collabera.digital.library.management.dto.BorrowerDto;
import com.collabera.digital.library.management.dto.LibraryManagementDTO;
import org.springframework.http.ResponseEntity;

public interface IBorrowerService {
    BorrowerDto register(BorrowerDto borrowerDto);
    LibraryManagementDTO borrowBook(Long borrowerId, Long bookId);
    LibraryManagementDTO returnBook(Long borrowerId,Long bookId);
}
