package com.example.fuelprices.repository;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.fuelprices.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {
    
    User findById(Long id);

    @PreAuthorize("hasAuthority('ADMIN')")
    List<User> findAll();

    @RestResource(exported = false)
    User save(User user);

    @RestResource(exported = false)
    Optional<User> findByFirebaseId(String firebaseId);

}
