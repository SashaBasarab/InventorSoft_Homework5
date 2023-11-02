package com.example.inventorsoft_homework5.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginAndSignupRequest {

    @NotNull(message = "Login is required")
    private String login;

    @NotNull(message = "Password is required")
    @Size(min = 8, max = 16)
    private String password;

}
