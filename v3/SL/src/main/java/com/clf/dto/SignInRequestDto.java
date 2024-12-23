package com.clf.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignInRequestDto {
    @Size(min = 3, max = 30, message = "Name length should be in range[3, 30]")
    @NotBlank(message = "Username can't be empty")
    private String username;

    @Size(min = 5, max = 100, message = "Password length must be in range [5, 50]")
    private String password;
}