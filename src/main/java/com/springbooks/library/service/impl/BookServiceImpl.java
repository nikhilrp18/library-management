package com.springbooks.library.service.impl;

import com.springbooks.library.exception.*;
import com.springbooks.library.model.entity.Book;
import com.springbooks.library.model.mapper.BookMapper;
import com.springbooks.library.model.request.BookRequest;
import com.springbooks.library.model.response.BookResponse;
import com.springbooks.library.repository.BookRepository;
import com.springbooks.library.repository.MemberRepository;
import com.springbooks.library.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookServiceImpl implements BookService {

    @Autowired BookRepository bookRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired BookMapper bookMapper;

    @Override
    public BookResponse createBook(BookRequest request) {
        log.debug("Creating book with ISBN: {}", request.getIsbn());
        
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateResourceException("Book with ISBN " + request.getIsbn() + " already exists");
        }
        
        Book book = bookMapper.toEntity(request);
        Book savedBook = bookRepository.save(book);
        
        log.info("Book created successfully with ID: {}", savedBook.getId());
        return bookMapper.toResponse(savedBook);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks() {
        log.debug("Fetching all books");
        
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(bookMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse getBookById(Long id) {
        log.debug("Fetching book with ID: {}", id);
        
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
        
        return bookMapper.toResponse(book);
    }

    @Override
    public BookResponse updateBook(Long id, BookRequest request) {
        log.debug("Updating book with ID: {}", id);
        
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
        
        // Check if ISBN is being changed and if it already exists for another book
        if (!existingBook.getIsbn().equals(request.getIsbn()) && 
            bookRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateResourceException("Book with ISBN " + request.getIsbn() + " already exists");
        }
        
        bookMapper.updateEntity(existingBook, request);
        Book updatedBook = bookRepository.save(existingBook);
        
        log.info("Book updated successfully with ID: {}", updatedBook.getId());
        return bookMapper.toResponse(updatedBook);
    }

    @Override
    public void deleteBook(Long id) {
        log.debug("Deleting book with ID: {}", id);
        
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book not found with ID: " + id);
        }
        
        bookRepository.deleteById(id);
        log.info("Book deleted successfully with ID: {}", id);
    }

    @Override
    public void borrowBook(Long bookId, Long memberId) {
        log.debug("Processing borrow request for book ID: {} by member ID: {}", bookId, memberId);
        
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));
        
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException("Member not found with ID: " + memberId);
        }
        
        if (book.getIsBorrowed()) {
            throw new BookAlreadyBorrowedException("Book with ID " + bookId + " is already borrowed");
        }
        
        book.setIsBorrowed(true);
        bookRepository.save(book);
        
        log.info("Book with ID: {} successfully borrowed by member ID: {}", bookId, memberId);
    }

    @Override
    public void returnBook(Long bookId) {
        log.debug("Processing return request for book ID: {}", bookId);
        
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));
        
        if (!book.getIsBorrowed()) {
            throw new BookNotBorrowedException("Book with ID " + bookId + " is not currently borrowed");
        }
        
        book.setIsBorrowed(false);
        bookRepository.save(book);
        
        log.info("Book with ID: {} successfully returned", bookId);
    }
}
