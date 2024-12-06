package com.collabera.digital.library.management.repository;

import com.collabera.digital.library.management.model.Book;
import com.collabera.digital.library.management.model.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBorrowRepository extends JpaRepository<Borrower, Long> {
    Optional<Borrower> findByNameAndEmail(String name,String email);
}
