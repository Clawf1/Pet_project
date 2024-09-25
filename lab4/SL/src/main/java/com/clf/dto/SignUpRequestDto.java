package com.clf.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequestDto {
    @Size(min = 3, max = 30, message = "Name length should be in range[3, 30]")
    @NotBlank(message = "Username can't be blank")
    private String username;

    @Size(min = 5, max = 100, message = "Email length should be in range[5, 100]")
    @NotBlank(message = "Email can't be blank")
    @Email(message = "Email should be alike user@example.com")
    private String email;

    @Size(min = 5, max = 100, message = "Password length must be in range [5, 100]")
    private String password;
}