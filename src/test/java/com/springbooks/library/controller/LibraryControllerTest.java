package com.springbooks.library.controller;

import com.springbooks.library.config.TestSecurityConfig;
import com.springbooks.library.exception.BookAlreadyBorrowedException;
import com.springbooks.library.exception.BookNotFoundException;
import com.springbooks.library.exception.BookNotBorrowedException;
import com.springbooks.library.exception.MemberNotFoundException;
import com.springbooks.library.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibraryController.class)
@Import(TestSecurityConfig.class)
class LibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void borrowBook_WithValidToken_Success() throws Exception {
        doNothing().when(bookService).borrowBook(1L, 1L);

        mockMvc.perform(post("/api/borrow/1/member/1")
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Book borrowed successfully"));
    }

    @Test
    void borrowBook_WithoutToken_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/borrow/1/member/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void borrowBook_BookNotFound_NotFound() throws Exception {
        doThrow(new BookNotFoundException("Book not found")).when(bookService).borrowBook(1L, 1L);

        mockMvc.perform(post("/api/borrow/1/member/1")
                .with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Book not found"));
    }

    @Test
    void borrowBook_MemberNotFound_NotFound() throws Exception {
        doThrow(new MemberNotFoundException("Member not found")).when(bookService).borrowBook(1L, 1L);

        mockMvc.perform(post("/api/borrow/1/member/1")
                .with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Member not found"));
    }

    @Test
    void borrowBook_AlreadyBorrowed_Conflict() throws Exception {
        doThrow(new BookAlreadyBorrowedException("Book already borrowed")).when(bookService).borrowBook(1L, 1L);

        mockMvc.perform(post("/api/borrow/1/member/1")
                .with(jwt()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Book already borrowed"));
    }

    @Test
    void returnBook_WithValidToken_Success() throws Exception {
        doNothing().when(bookService).returnBook(1L);

        mockMvc.perform(post("/api/return/1")
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Book returned successfully"));
    }

    @Test
    void returnBook_BookNotFound_NotFound() throws Exception {
        doThrow(new BookNotFoundException("Book not found")).when(bookService).returnBook(1L);

        mockMvc.perform(post("/api/return/1")
                .with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Book not found"));
    }

    @Test
    void returnBook_NotBorrowed_BadRequest() throws Exception {
        doThrow(new BookNotBorrowedException("Book not borrowed")).when(bookService).returnBook(1L);

        mockMvc.perform(post("/api/return/1")
                .with(jwt()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Book not borrowed"));
    }
}
