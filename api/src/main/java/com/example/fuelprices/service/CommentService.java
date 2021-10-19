package com.example.fuelprices.service;

import java.util.Optional;

import com.example.fuelprices.dto.AddOrEditCommentDTO;
import com.example.fuelprices.model.Comment;
import com.example.fuelprices.model.FuelStation;
import com.example.fuelprices.model.User;
import com.example.fuelprices.repository.CommentRepository;
import com.example.fuelprices.repository.FuelStationRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    
    private final CommentRepository repository;
    private final FuelStationRepository fuelStationRepository;

    public CommentService(CommentRepository repository,
                        FuelStationRepository fuelStationRepository) {
        this.repository = repository;
        this.fuelStationRepository = fuelStationRepository;
    }

    public Optional<Comment> addOrEditComment(AddOrEditCommentDTO dto) {
        return dto.id() == null ? addComment(dto) : editComment(dto);
    }

    private Optional<Comment> addComment(AddOrEditCommentDTO dto) {
        User author = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FuelStation station = fuelStationRepository.findById(dto.stationId());

        if(author == null || station == null)
            return Optional.empty();

        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setContent(dto.content());
        comment.setRate(dto.rate());
        comment.setStation(station);

        return Optional.of(repository.save(comment));
    }

    private Optional<Comment> editComment(AddOrEditCommentDTO dto) {
        Comment comment = repository.findById(dto.id());

        if(comment == null)
            return Optional.empty();

        comment.setRate(dto.rate());
        comment.setContent(dto.content());

        return Optional.of(repository.save(comment));
    }

}
