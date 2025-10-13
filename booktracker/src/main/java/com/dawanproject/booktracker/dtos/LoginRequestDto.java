package com.dawanproject.booktracker.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(@NotEmpty @Size(min = 1, max = 150) String username,
                              @NotEmpty @Size(min = 6, max = 40) String password) {

}

