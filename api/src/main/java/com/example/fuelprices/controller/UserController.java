package com.example.fuelprices.controller;

import com.example.fuelprices.dto.EditUserRoleDTO;
import com.example.fuelprices.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/roles")
    public ResponseEntity<Void> setUserRole(@RequestBody EditUserRoleDTO dto) {
        return service.setUserRole(dto) ? ResponseEntity.ok(null) : ResponseEntity.badRequest().build();
    }

}
