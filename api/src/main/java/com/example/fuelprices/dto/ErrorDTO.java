package com.example.fuelprices.dto;

public record ErrorDTO(
    int status,
    String error,
    String message
) {
    
}
