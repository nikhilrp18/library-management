package com.springbooks.library.controller;

import com.springbooks.library.model.response.ApiResponse;
import com.springbooks.library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Library Operations", description = "Book borrowing and returning operations")
@SecurityRequirement(name = "bearerAuth")
public class LibraryController {

    private final BookService bookService;

    @PostMapping("/borrow/{bookId}/member/{memberId}")
    @Operation(summary = "Borrow a book", description = "Allows a member to borrow a book from the library")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Book borrowed successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Book or member not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Book already borrowed"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Object>> borrowBook(
            @Parameter(description = "Book ID") @PathVariable Long bookId,
            @Parameter(description = "Member ID") @PathVariable Long memberId) {
        log.info("Processing borrow request for book ID: {} by member ID: {}", bookId, memberId);
        
        bookService.borrowBook(bookId, memberId);
        ApiResponse<Object> response = ApiResponse.success(
            "Book borrowed successfully"
        );
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/return/{bookId}")
    @Operation(summary = "Return a book", description = "Allows returning a borrowed book to the library")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Book returned successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Book not currently borrowed"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Book not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Object>> returnBook(
            @Parameter(description = "Book ID") @PathVariable Long bookId) {
        log.info("Processing return request for book ID: {}", bookId);
        
        bookService.returnBook(bookId);
        ApiResponse<Object> response = ApiResponse.success(
            "Book returned successfully"
        );
        
        return ResponseEntity.ok(response);
    }
}
