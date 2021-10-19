package com.example.fuelprices.dto;

import com.example.fuelprices.model.UserRole;

public record EditUserRoleDTO(
    String email,
    UserRole role
) {
    
}
