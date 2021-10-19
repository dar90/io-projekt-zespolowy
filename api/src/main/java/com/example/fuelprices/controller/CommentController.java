package com.example.fuelprices.controller;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import com.example.fuelprices.dto.AddOrEditCommentDTO;
import com.example.fuelprices.dto.ErrorDTO;
import com.example.fuelprices.model.Comment;
import com.example.fuelprices.service.CommentService;

import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
public class CommentController {
    
    private final CommentService service;
    private final RepositoryEntityLinks links;

    public CommentController(CommentService service,
                            RepositoryEntityLinks links) {
        this.service = service;
        this.links = links;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR', 'USER')")
    @PostMapping
    public ResponseEntity<?> addComment(@Valid @RequestBody AddOrEditCommentDTO dto, Errors validationResult) {     
        
        if(validationResult.hasErrors()) 
            return Validation.validationFailResponse(validationResult);

        Optional<Comment> comment = service.addOrEditComment(dto);

        if(comment.isEmpty())
            return ResponseEntity.badRequest()
                                .body(new ErrorDTO(
                                                HttpStatus.BAD_REQUEST.value(), 
                                                "Resource cannot be created", 
                                                "Wrong stationId or you're not logged in"
                                            ));

        URI commentURI = links.linkToItemResource(Comment.class, comment.get().getId()).toUri();
        return ResponseEntity.created(commentURI).build();

    }


    @PreAuthorize("hasAuthority('ADMIN', 'MODERATOR')")
    @PutMapping
    public ResponseEntity<?> editComment(@RequestBody @Valid AddOrEditCommentDTO dto, Errors validationResult) {

        if(validationResult.hasErrors())
            return Validation.validationFailResponse(validationResult);

        return service.addOrEditComment(dto).isEmpty() ? 
                ResponseEntity.notFound().build() : ResponseEntity.ok(null);

    }

}
