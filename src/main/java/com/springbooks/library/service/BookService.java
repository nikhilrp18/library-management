package com.springbooks.library.service;

import com.springbooks.library.model.request.BookRequest;
import com.springbooks.library.model.response.BookResponse;

import java.util.List;

public interface BookService {

    BookResponse createBook(BookRequest request);

    List<BookResponse> getAllBooks();

    BookResponse getBookById(Long id);

    BookResponse updateBook(Long id, BookRequest request);

    void deleteBook(Long id);

    void borrowBook(Long bookId, Long memberId);

    void returnBook(Long bookId);
}
