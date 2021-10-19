package com.example.fuelprices.controller;

import com.example.fuelprices.dto.ErrorDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

public class Validation {

    public static ResponseEntity<?> validationFailResponse(Errors validationResult) {
        String message = validationResult.getFieldErrors()
                                            .stream()
                                            .map(FieldError::getField)
                                            .reduce("Invalid fields: ", (a, b) -> a.concat(b + " "));
        return ResponseEntity.badRequest()
                            .body(new ErrorDTO(
                                    HttpStatus.BAD_REQUEST.value(), 
                                    "Validation error", 
                                    message
                                ));
    } 
    
}
