package com.collabera.digital.library.management.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DuplicateBorrowerException extends RuntimeException{

    public DuplicateBorrowerException(String message) {
        super(message);
    }

    public DuplicateBorrowerException(String message, Throwable cause) {
        super(message, cause);
    }
}
