package com.springbooks.library.service;

import com.springbooks.library.exception.*;
import com.springbooks.library.model.entity.Book;
import com.springbooks.library.model.mapper.BookMapper;
import com.springbooks.library.model.request.BookRequest;
import com.springbooks.library.model.response.BookResponse;
import com.springbooks.library.repository.BookRepository;
import com.springbooks.library.repository.MemberRepository;
import com.springbooks.library.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book testBook;
    private BookRequest testBookRequest;
    private BookResponse testBookResponse;

    @BeforeEach
    void setUp() {
        testBook = new Book(1L, "Test Book", "Test Author", "123456789", false);
        testBookRequest = new BookRequest("Test Book", "Test Author", "123456789");
        testBookResponse = new BookResponse(1L, "Test Book", "Test Author", "123456789", false);
    }

    @Test
    void createBook_Success() {
        when(bookRepository.existsByIsbn(testBookRequest.getIsbn())).thenReturn(false);
        when(bookMapper.toEntity(testBookRequest)).thenReturn(testBook);
        when(bookRepository.save(testBook)).thenReturn(testBook);
        when(bookMapper.toResponse(testBook)).thenReturn(testBookResponse);

        BookResponse result = bookService.createBook(testBookRequest);

        assertNotNull(result);
        assertEquals(testBookResponse.getTitle(), result.getTitle());
        verify(bookRepository).existsByIsbn(testBookRequest.getIsbn());
        verify(bookRepository).save(testBook);
    }

    @Test
    void createBook_DuplicateISBN_ThrowsException() {
        when(bookRepository.existsByIsbn(testBookRequest.getIsbn())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> bookService.createBook(testBookRequest));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void getAllBooks_Success() {
        
        List<Book> books = Arrays.asList(testBook);
        when(bookRepository.findAll()).thenReturn(books);
        when(bookMapper.toResponse(testBook)).thenReturn(testBookResponse);

        List<BookResponse> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testBookResponse.getTitle(), result.get(0).getTitle());
    }

    @Test
    void getBookById_Success() {
        
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookMapper.toResponse(testBook)).thenReturn(testBookResponse);

        BookResponse result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(testBookResponse.getTitle(), result.getTitle());
    }

    @Test
    void getBookById_NotFound_ThrowsException() {
        
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(1L));
    }

    @Test
    void updateBook_Success() {
        
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(testBook)).thenReturn(testBook);
        when(bookMapper.toResponse(testBook)).thenReturn(testBookResponse);

        BookResponse result = bookService.updateBook(1L, testBookRequest);

        assertNotNull(result);
        verify(bookMapper).updateEntity(testBook, testBookRequest);
        verify(bookRepository).save(testBook);
    }

    @Test
    void deleteBook_Success() {
        
        when(bookRepository.existsById(1L)).thenReturn(true);

        bookService.deleteBook(1L);
        
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteBook_NotFound_ThrowsException() {
        
        when(bookRepository.existsById(1L)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(1L));
        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    void borrowBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(memberRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.save(testBook)).thenReturn(testBook);

        bookService.borrowBook(1L, 1L);

        assertTrue(testBook.getIsBorrowed());
        verify(bookRepository).save(testBook);
    }

    @Test
    void borrowBook_BookNotFound_ThrowsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.borrowBook(1L, 1L));
    }

    @Test
    void borrowBook_MemberNotFound_ThrowsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(memberRepository.existsById(1L)).thenReturn(false);

        assertThrows(MemberNotFoundException.class, () -> bookService.borrowBook(1L, 1L));
    }

    @Test
    void borrowBook_AlreadyBorrowed_ThrowsException() {
        testBook.setIsBorrowed(true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(memberRepository.existsById(1L)).thenReturn(true);

        assertThrows(BookAlreadyBorrowedException.class, () -> bookService.borrowBook(1L, 1L));
    }

    @Test
    void returnBook_Success() {
        testBook.setIsBorrowed(true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(testBook)).thenReturn(testBook);

        bookService.returnBook(1L);

        assertFalse(testBook.getIsBorrowed());
        verify(bookRepository).save(testBook);
    }

    @Test
    void returnBook_BookNotFound_ThrowsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.returnBook(1L));
    }

    @Test
    void returnBook_NotBorrowed_ThrowsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        assertThrows(BookNotBorrowedException.class, () -> bookService.returnBook(1L));
    }
}
