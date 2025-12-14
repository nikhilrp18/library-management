package com.springbooks.library.model.mapper;

import com.springbooks.library.model.entity.Book;
import com.springbooks.library.model.request.BookRequest;
import com.springbooks.library.model.response.BookResponse;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toEntity(BookRequest request) {
        if (request == null) {
            return null;
        }
        
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setIsBorrowed(false);
        
        return book;
    }

    public BookResponse toResponse(Book book) {
        if (book == null) {
            return null;
        }
        
        return new BookResponse(
            book.getId(),
            book.getTitle(),
            book.getAuthor(),
            book.getIsbn(),
            book.getIsBorrowed()
        );
    }

    public void updateEntity(Book book, BookRequest request) {
        if (book != null && request != null) {
            book.setTitle(request.getTitle());
            book.setAuthor(request.getAuthor());
            book.setIsbn(request.getIsbn());
        }
    }
}
