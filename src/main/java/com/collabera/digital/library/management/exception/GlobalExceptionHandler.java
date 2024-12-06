package com.collabera.digital.library.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("error-status: "+HttpStatus.NOT_FOUND.value()+"\nerror-code: "+HttpStatus.NOT_FOUND.getReasonPhrase()+"\nerror-message: "+ex.getMessage());
    }
    @ExceptionHandler(DuplicateBorrowerException.class)
    public ResponseEntity<String> handleDuplicateBorrowerException(DuplicateBorrowerException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("error-status: "+HttpStatus.BAD_REQUEST.value()+"\nerror-code: "+HttpStatus.BAD_REQUEST.getReasonPhrase()+"\nerror-message: "+ex.getMessage());
    }

    @ExceptionHandler(ISBNConflictException.class)
    public ResponseEntity<String> handleDuplicateEntryException(ISBNConflictException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("error-status: "+HttpStatus.CONFLICT.value()+"\nerror-code: "+HttpStatus.CONFLICT.getReasonPhrase()+"\nerror-message: "+ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("error-status: "+HttpStatus.INTERNAL_SERVER_ERROR.value()+"\nerror-code: "+HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()+"\nAn unexpected error occurred: " + ex.getMessage());
    }
}
