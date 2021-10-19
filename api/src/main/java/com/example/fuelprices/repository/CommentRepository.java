package com.example.fuelprices.repository;

import com.example.fuelprices.model.Comment;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface CommentRepository extends Repository<Comment, Long> {
    Comment findById(Long id);

    @PreAuthorize("hasAuthority('ADMIN')")
    List<Comment> findAll();

    @RestResource(exported = false)
    Comment save(Comment comment);
}
