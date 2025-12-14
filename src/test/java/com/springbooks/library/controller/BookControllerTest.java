package com.springbooks.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbooks.library.config.TestSecurityConfig;
import com.springbooks.library.model.request.BookRequest;
import com.springbooks.library.model.response.BookResponse;
import com.springbooks.library.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import(TestSecurityConfig.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createBook_WithValidToken_Success() throws Exception {
        BookRequest request = new BookRequest("Test Book", "Test Author", "123456789");
        BookResponse response = new BookResponse(1L, "Test Book", "Test Author", "123456789", false);
        
        when(bookService.createBook(any(BookRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/books")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Test Book"))
                .andExpect(jsonPath("$.data.author").value("Test Author"))
                .andExpect(jsonPath("$.data.isbn").value("123456789"));
    }

    @Test
    void createBook_WithoutToken_Unauthorized() throws Exception {
        BookRequest request = new BookRequest("Test Book", "Test Author", "123456789");

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createBook_InvalidInput_BadRequest() throws Exception {
        BookRequest request = new BookRequest("", "", ""); // Invalid input

        mockMvc.perform(post("/api/books")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllBooks_WithValidToken_Success() throws Exception {
        List<BookResponse> books = Arrays.asList(
            new BookResponse(1L, "Book 1", "Author 1", "111111111", false),
            new BookResponse(2L, "Book 2", "Author 2", "222222222", true)
        );
        
        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/books")
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].title").value("Book 1"))
                .andExpect(jsonPath("$.data[1].title").value("Book 2"));
    }

    @Test
    void getBookById_WithValidToken_Success() throws Exception {
        BookResponse response = new BookResponse(1L, "Test Book", "Test Author", "123456789", false);
        
        when(bookService.getBookById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/books/1")
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Test Book"));
    }

    @Test
    void updateBook_WithValidToken_Success() throws Exception {
        BookRequest request = new BookRequest("Updated Book", "Updated Author", "123456789");
        BookResponse response = new BookResponse(1L, "Updated Book", "Updated Author", "123456789", false);
        
        when(bookService.updateBook(eq(1L), any(BookRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/books/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Updated Book"));
    }

    @Test
    void deleteBook_WithValidToken_Success() throws Exception {
        mockMvc.perform(delete("/api/books/1")
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
