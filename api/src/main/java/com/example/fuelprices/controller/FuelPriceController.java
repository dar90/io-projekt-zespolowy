package com.example.fuelprices.controller;

import java.util.Optional;

import javax.validation.Valid;

import com.example.fuelprices.dto.AddOrEditFuelPriceDTO;
import com.example.fuelprices.dto.ErrorDTO;
import com.example.fuelprices.model.FuelPrice;
import com.example.fuelprices.service.FuelPriceService;

import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/fuelPrices")
public class FuelPriceController {

    private final FuelPriceService service;
    private final EntityLinks links;

    public FuelPriceController(FuelPriceService service, EntityLinks links) {
        this.service = service;
        this.links = links;
    } 

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR', 'USER')")
    @PostMapping("/create")
    public ResponseEntity<?> addFuelPrice(@RequestBody @Valid AddOrEditFuelPriceDTO dto, Errors validationResult) {

        if(validationResult.hasErrors()) 
            return Validation.validationFailResponse(validationResult);

        Optional<FuelPrice> fuelPrice = service.addOrEditFuelPrice(dto);

        if(fuelPrice.isEmpty())
            return ResponseEntity.badRequest()
                                .body(new ErrorDTO(
                                                HttpStatus.BAD_REQUEST.value(), 
                                                "Resource cannot be created", 
                                                "Wrong stationId or you're not logged in"
                                            ));

        URI fuelPriceURI = links.linkToItemResource(FuelPrice.class, fuelPrice.get().getId()).toUri();
        return ResponseEntity.created(fuelPriceURI).build();

    }

    @PreAuthorize("hasAuthority('ADMIN', 'MODERATOR')")
    @PutMapping("/update")
    public ResponseEntity<?> editFuelPrice(@RequestBody @Valid AddOrEditFuelPriceDTO dto, Errors validationResult) {
        if(validationResult.hasErrors())
            return Validation.validationFailResponse(validationResult);

        return service.addOrEditFuelPrice(dto).isEmpty() ? 
                ResponseEntity.notFound().build() : ResponseEntity.ok(null);
    }
    
}
