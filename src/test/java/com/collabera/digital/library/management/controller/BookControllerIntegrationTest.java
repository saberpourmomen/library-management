package com.collabera.digital.library.management.controller;

import com.collabera.digital.library.management.dto.BookDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerBook_shouldReturnCreatedBook() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Test Book");
        bookDto.setAuthor("Test Author");
        bookDto.setIsbn("1234567890");

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/books/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDto)));

        response.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.isbn").value("1234567890"));
    }

    @Test
    void searchBooks_shouldReturnPaginatedResults() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Test Book");

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/books/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("offset", "0")
                .param("limit", "10")
                .param("sort", "-id")
                .content(objectMapper.writeValueAsString(bookDto)));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void findByIsbn_shouldReturnBook() throws Exception {
        String isbn = "1234567890";

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/books/{isbn}", isbn)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isbn").value(isbn));
    }

    @Test
    void getAllBooks_shouldReturnPaginatedResults() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/books/list")
                .contentType(MediaType.APPLICATION_JSON)
                .param("offset", "0")
                .param("limit", "10")
                .param("sort", "-id"));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray());
    }
}
