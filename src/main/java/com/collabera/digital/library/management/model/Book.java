package com.collabera.digital.library.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="TITLE",nullable = false)
    private String title;
    @Column(name="ISBN",nullable = false)
    private String isbn;
    @Column(name="AUTHOR",nullable = false)
    private String author;
    @Column(name="IS_BORROWED",nullable = false)
    private Boolean isBorrowed;
    @OneToOne
    @JoinColumn(name = "borrower_id", unique = true)
    private Borrower borrower;
}
