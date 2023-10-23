package com.example.inventorsoft_homework5.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {

    private Long id;

    private String token;

    private String login;

    private String password;

    private List<String> roles;

}
