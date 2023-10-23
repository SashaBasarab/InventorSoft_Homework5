package com.example.inventorsoft_homework5.service;

import com.example.inventorsoft_homework5.dto.request.LoginAndSignupRequest;
import com.example.inventorsoft_homework5.dto.response.JwtResponse;

public interface AuthService {

    JwtResponse authenticateUser(LoginAndSignupRequest loginAndSignupRequest);

    void registerUser(LoginAndSignupRequest loginAndSignupRequest);

}