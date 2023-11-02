package com.example.inventorsoft_homework5.controller;

import com.example.inventorsoft_homework5.dto.request.LoginAndSignupRequest;
import com.example.inventorsoft_homework5.dto.response.JwtResponse;
import com.example.inventorsoft_homework5.dto.response.MessageResponse;
import com.example.inventorsoft_homework5.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Test
    public void testUnauthorizedAccessToAdminEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/admin"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testAuthorizedAccessToAdminEndpoint() throws Exception {
        UserDetails userDetails = User.withUsername("admin")
                .password("password")
                .authorities(Collections.singletonList(() -> "ROLE_ADMIN"))
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/admin"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUnauthorizedAccessToTextEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/text"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testAuthorizedAccessToTextEndpoint() throws Exception {
        UserDetails userDetails = User.withUsername("user")
                .password("password")
                .authorities(Collections.singletonList(() -> "USER_ROLE"))
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/text"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testAuthenticateUser() {
        LoginAndSignupRequest request = new LoginAndSignupRequest();

        JwtResponse expectedResponse = new JwtResponse();
        Mockito.when(authService.authenticateUser(Mockito.eq(request))).thenReturn(expectedResponse);

        ResponseEntity<JwtResponse> response = authController.authenticateUser(request);

        Mockito.verify(authService).authenticateUser(request);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void testRegisterUser() {
        LoginAndSignupRequest request = new LoginAndSignupRequest();

        Mockito.doNothing().when(authService).registerUser(Mockito.eq(request));

        ResponseEntity<?> response = authController.registerUser(request);

        Mockito.verify(authService).registerUser(request);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals("User registered successfully!", ((MessageResponse) response.getBody()).getMessage());
    }
}