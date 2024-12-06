package com.collabera.digital.library.management.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class ISBNConflictException extends RuntimeException{

    public ISBNConflictException(String message) {
        super(message);
    }

    public ISBNConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
