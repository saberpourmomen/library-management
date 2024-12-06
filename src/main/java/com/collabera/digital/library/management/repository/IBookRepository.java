package com.collabera.digital.library.management.repository;

import com.collabera.digital.library.management.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBookRepository extends JpaRepository<Book, Long>,JpaSpecificationExecutor<Book> {
    Optional<List<Book>> findByIsbn(String isbn);
}
