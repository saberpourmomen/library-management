package com.collabera.digital.library.management.controller;

import com.collabera.digital.library.management.dto.BorrowerDto;
import com.collabera.digital.library.management.dto.LibraryManagementDTO;
import com.collabera.digital.library.management.service.IBorrowerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class BorrowerControllerTest {
    @Mock
    private IBorrowerService iBorrowerService;

    @InjectMocks
    private BorrowerController borrowerController;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register() {
        BorrowerDto borrowerDto=new BorrowerDto();
        borrowerDto.setId(1L);
        when(iBorrowerService.register(any())).thenReturn(borrowerDto);
        ResponseEntity<BorrowerDto> response= borrowerController.register(borrowerDto);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void borrowBook() {
        LibraryManagementDTO managementDTO=new LibraryManagementDTO();
        managementDTO.setMessage("book is borrowed successfully");
        when(iBorrowerService.borrowBook(any(),any())).thenReturn(managementDTO);
        ResponseEntity<LibraryManagementDTO> response= borrowerController.borrowBook(1L,1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("book is borrowed successfully", response.getBody().getMessage());
    }

    @Test
    void returnBook() {
        LibraryManagementDTO managementDTO=new LibraryManagementDTO();
        managementDTO.setMessage("book is returned successfully");
        when(iBorrowerService.returnBook(any(),any())).thenReturn(managementDTO);
        ResponseEntity<LibraryManagementDTO> response= borrowerController.returnBook(1L,1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("book is returned successfully", response.getBody().getMessage());
    }
}