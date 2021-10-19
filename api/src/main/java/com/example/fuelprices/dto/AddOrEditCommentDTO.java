package com.example.fuelprices.dto;

import javax.validation.constraints.NotNull;

import com.example.fuelprices.model.Rate;

public record AddOrEditCommentDTO(
    Long id,
    @NotNull Rate rate,
    String content,
    @NotNull Long stationId
) {
    
}
