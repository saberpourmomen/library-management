package com.collabera.digital.library.management.service.impl;

import com.collabera.digital.library.management.dto.BookDto;
import com.collabera.digital.library.management.exception.ISBNConflictException;
import com.collabera.digital.library.management.exception.ResourceNotFoundException;
import com.collabera.digital.library.management.model.Book;
import com.collabera.digital.library.management.model.Borrower;
import com.collabera.digital.library.management.repository.IBookRepository;
import com.collabera.digital.library.management.service.IBookService;
import jakarta.persistence.criteria.Predicate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookServiceImpl implements IBookService {

    private final IBookRepository bookRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public BookServiceImpl(IBookRepository bookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public BookDto register(BookDto bookDto) {
            Optional<List<Book>> existBooks=bookRepository.findByIsbn(bookDto.getIsbn());
            Book book;
            if (existBooks.isPresent() && existBooks.get().size()>0){
                Book existBook=existBooks.get().get(0);
                if(!existBook.getTitle().equals(bookDto.getTitle()) && existBook.getAuthor().equals(bookDto.getAuthor()) ){
                    throw new ISBNConflictException("Books with the same ISBN must have the same title and author");
                }
            }
                book=modelMapper.map(bookDto,Book.class);
                book.setIsBorrowed(Boolean.FALSE);
        book=bookRepository.save(book);
        return modelMapper.map(book,BookDto.class);
    }

    @Override
    public Page<BookDto> list(Pageable pageable) {
        Page<Book> books=bookRepository.findAll(pageable);
        return books.map(book -> modelMapper.map(book, BookDto.class));
    }

    @Override
    public BookDto findByIsbn(String isbn) {
        Optional<List<Book>> existBooks=bookRepository.findByIsbn(isbn);
        Book book=new Book();
        if (existBooks.isPresent()){
            book= existBooks.get().get(0);
        }
        return modelMapper.map(book, BookDto.class);
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public BookDto bookBorrow(Long id, Borrower borrower){
        Optional<Book> existBook=bookRepository.findById(id);
        if (existBook.isPresent()){
            if (existBook.get().getIsBorrowed()){
                throw new ResourceNotFoundException("the book has been borrowed");
            }
        }else{
            throw new ResourceNotFoundException("Book not found!");
        }
        Book book=existBook.get();
        book.setBorrower(borrower);
        book.setIsBorrowed(Boolean.TRUE);
        book=bookRepository.save(book);
        return modelMapper.map(book, BookDto.class);
    }
    @Override
    public BookDto bookReturn(Long id){
        Optional<Book> existBook=bookRepository.findById(id);
        if (existBook.isPresent()){
            if (!existBook.get().getIsBorrowed()){
                throw new ResourceNotFoundException("the book hasn't been borrowed yet");
            }
        }else{
            throw new ResourceNotFoundException("Book not found!");
        }
        Book book=existBook.get();
        book.setBorrower(null);
        book.setIsBorrowed(Boolean.FALSE);
        book=bookRepository.save(book);
        return modelMapper.map(book, BookDto.class);
    }
    @Override
    public Page<BookDto> search(BookDto bookDto,Pageable pageable) {
        Page<Book> books=bookRepository.findAll(getSearchCriteria(bookDto),pageable);
        return books.map(book -> modelMapper.map(book, BookDto.class));
    }
    private Specification<Book> getSearchCriteria(BookDto bookDto){
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (Objects.nonNull(bookDto.getId())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("id"), bookDto.getId()));
            }
            if (Objects.nonNull(bookDto.getTitle())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("title"), bookDto.getTitle()));
            }
            if (Objects.nonNull(bookDto.getAuthor())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("author"), bookDto.getAuthor()));
            }
            if (Objects.nonNull(bookDto.getIsbn())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isbn"), bookDto.getIsbn()));
            }
            if (Objects.nonNull(bookDto.getIsBorrowed())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isBorrowed"), bookDto.getIsBorrowed()));
            }

            return predicate;
        };
    }

}
