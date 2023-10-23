package com.example.inventorsoft_homework5.controller;

import com.example.inventorsoft_homework5.dto.request.LoginAndSignupRequest;
import com.example.inventorsoft_homework5.dto.response.JwtResponse;
import com.example.inventorsoft_homework5.dto.response.MessageResponse;
import com.example.inventorsoft_homework5.exception.UnauthorizedException;
import com.example.inventorsoft_homework5.exception.UserAlreadyExists;
import com.example.inventorsoft_homework5.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody @Valid LoginAndSignupRequest loginAndSignupRequest) {
        log.info("request: {}", loginAndSignupRequest);
        return ResponseEntity.ok(authService.authenticateUser(loginAndSignupRequest));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody @Valid LoginAndSignupRequest loginAndSignupRequest) {
        log.info("request: {}", loginAndSignupRequest);
        try {
            authService.registerUser(loginAndSignupRequest);
            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (UserAlreadyExists e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: login or password are already taken!"));
        }
    }

    @GetMapping("/text")
    public ResponseEntity<String> getText(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return ResponseEntity.ok("Hello, " + username + "! You are authorized.");
        } else {
            throw new UnauthorizedException("You are not authorized.");
        }
    }

    @GetMapping("/admin")
    public ResponseEntity<String> getAdminText(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.ok("This is admin content.");
        } else {
            throw new UnauthorizedException("You are not authorized to access this resource.");
        }
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

}

