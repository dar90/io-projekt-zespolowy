package com.example.fuelprices.dto;

public record AppUserRegisterFormDTO(
    String username,
    String password,
    String repeatedPassword,
    String email
) {
    
}
