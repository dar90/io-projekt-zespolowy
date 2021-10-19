package com.example.fuelprices.repository;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.fuelprices.model.User;
import com.google.common.base.Optional;

import java.util.List;

public interface UserRepository extends Repository<User, Long> {
    
    // @PreAuthorize("hasAnyAuthority('USER', 'STATION_OWNER', 'MODERATOR', 'ADMIN')")
    User findById(Long id);

    @PreAuthorize("hasAuthority('ADMIN')")
    List<User> findAll();

    @RestResource(exported = false)
    User save(User user);

    @RestResource(exported = false)
    Optional<User> findByFirebaseId(String firebaseId);

}
