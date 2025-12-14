package com.springbooks.library.controller;

import com.springbooks.library.model.request.MemberRequest;
import com.springbooks.library.model.response.ApiResponse;
import com.springbooks.library.model.response.MemberResponse;
import com.springbooks.library.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Members", description = "Member management operations")
@SecurityRequirement(name = "bearerAuth")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @Operation(summary = "Register a new member", description = "Registers a new member in the library system")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Member registered successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Member with email already exists"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<MemberResponse>> registerMember(
            @Valid @RequestBody MemberRequest request) {
        log.info("Registering new member with email: {}", request.getEmail());
        
        MemberResponse memberResponse = memberService.registerMember(request);
        ApiResponse<MemberResponse> response = ApiResponse.success(
            memberResponse, 
            "Member registered successfully"
        );
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all members", description = "Retrieves a list of all members in the library")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Members retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getAllMembers() {
        log.info("Fetching all members");
        
        List<MemberResponse> members = memberService.getAllMembers();
        ApiResponse<List<MemberResponse>> response = ApiResponse.success(
            members, 
            "Members retrieved successfully"
        );
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get member by ID", description = "Retrieves details of a specific member")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Member retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Member not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<MemberResponse>> getMemberById(
            @Parameter(description = "Member ID") @PathVariable Long id) {
        log.info("Fetching member with ID: {}", id);
        
        MemberResponse memberResponse = memberService.getMemberById(id);
        ApiResponse<MemberResponse> response = ApiResponse.success(
            memberResponse, 
            "Member retrieved successfully"
        );
        
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update member", description = "Updates details of an existing member")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Member updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Member not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Member with email already exists"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<MemberResponse>> updateMember(
            @Parameter(description = "Member ID") @PathVariable Long id,
            @Valid @RequestBody MemberRequest request) {
        log.info("Updating member with ID: {}", id);
        
        MemberResponse memberResponse = memberService.updateMember(id, request);
        ApiResponse<MemberResponse> response = ApiResponse.success(
            memberResponse, 
            "Member updated successfully"
        );
        
        return ResponseEntity.ok(response);
    }
}
