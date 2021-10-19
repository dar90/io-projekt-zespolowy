package com.example.fuelprices.service;

import java.util.HashMap;
import java.util.Map;

import com.example.fuelprices.dto.EditUserRoleDTO;
import com.example.fuelprices.model.UserRole;
import com.example.fuelprices.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
    
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public boolean setUserRole(EditUserRoleDTO dto) {

        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(dto.email());
            Map<String, Object> claims = new HashMap<>();
            if(userRecord.getCustomClaims().containsKey("locked"))
                claims.put("locked", userRecord.getCustomClaims().get("locked"));
            claims.put("moderator", dto.role().equals(UserRole.MODERATOR) || dto.role().equals(UserRole.ADMIN));
            claims.put("admin", dto.role().equals(UserRole.ADMIN));
            FirebaseAuth.getInstance().setCustomUserClaims(userRecord.getUid(), claims);
        } catch (FirebaseAuthException e) {
            log.error(dto.email(), e.getMessage());
            return false;
        }

        return true;
    }

}
