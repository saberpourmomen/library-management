package com.collabera.digital.library.management.controller;

import com.collabera.digital.library.management.dto.BookDto;
import com.collabera.digital.library.management.dto.BorrowerDto;
import com.collabera.digital.library.management.dto.LibraryManagementDTO;
import com.collabera.digital.library.management.service.IBorrowerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BorrowerControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private IBorrowerService borrowerService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Test
    void testRegisterBorrower() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        BorrowerDto borrowerDto = new BorrowerDto();
        borrowerDto.setId(1L);
        borrowerDto.setName("John Doe");

        when(borrowerService.register(any())).thenReturn(borrowerDto);

        mockMvc.perform(post("/api/borrower/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(borrowerDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(borrowerService, times(1)).register(any(BorrowerDto.class));
    }

    @Test
    void testBorrowBook() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        BookDto bookDto=new BookDto(101L,"book","123","test",true);
        BorrowerDto borrowerDto=BorrowerDto.builder()
                .id(1L)
                .name("borrower")
                .email("test@test.com").build();
        LibraryManagementDTO libraryManagementDTO =LibraryManagementDTO.builder()
                .bookDto(bookDto).borrowerDto(borrowerDto).build();

        when(borrowerService.borrowBook(1L, 101L)).thenReturn(libraryManagementDTO);

        mockMvc.perform(get("/api/borrower/1/borrow/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.borrow.id").value(1))
                .andExpect(jsonPath("$.book.id").value(101));

        verify(borrowerService, times(1)).borrowBook(1L, 101L);
    }

    @Test
    void testReturnBook() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        BookDto bookDto=new BookDto(101L,"book","123","test",true);
        BorrowerDto borrowerDto=BorrowerDto.builder()
                .id(1L)
                .name("borrower")
                .email("test@test.com").build();
        LibraryManagementDTO libraryManagementDTO =LibraryManagementDTO.builder()
                .bookDto(bookDto).borrowerDto(borrowerDto).build();

        when(borrowerService.returnBook(1L, 101L)).thenReturn(libraryManagementDTO);

        mockMvc.perform(get("/api/borrower/1/return/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.borrow.id").value(1))
                .andExpect(jsonPath("$.book.id").value(101));

        verify(borrowerService, times(1)).returnBook(1L, 101L);
    }
}

