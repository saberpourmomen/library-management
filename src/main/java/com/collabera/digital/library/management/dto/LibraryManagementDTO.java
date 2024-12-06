package com.collabera.digital.library.management.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LibraryManagementDTO {
    @JsonProperty("book")
    private BookDto bookDto;
    @JsonProperty("borrow")
    private BorrowerDto borrowerDto;
    @JsonProperty("message")
    private String message;
}
