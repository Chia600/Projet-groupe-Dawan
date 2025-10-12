package com.dawanproject.booktracker.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequestDto {

    @NotEmpty
    @Size(min = 1, max = 150)
    private final String username;

    @NotEmpty
    @Size(min = 6, max = 40)
    private final String password;
}

