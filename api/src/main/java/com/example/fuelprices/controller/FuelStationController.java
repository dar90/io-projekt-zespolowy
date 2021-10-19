package com.example.fuelprices.controller;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import com.example.fuelprices.dto.AddOrEditFuelStationDTO;
import com.example.fuelprices.dto.ErrorDTO;
import com.example.fuelprices.model.FuelStation;
import com.example.fuelprices.service.FuelStationService;

import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fuelStations")
public class FuelStationController {
    
    private final FuelStationService service;
    private final EntityLinks links;

    public FuelStationController(FuelStationService service, EntityLinks links) {
        this.service = service;
        this.links = links;
    }

    @PostMapping
    public ResponseEntity<?> addFuelStation(@RequestBody @Valid AddOrEditFuelStationDTO dto, Errors validationResult) {

        if(validationResult.hasErrors()) 
            return Validation.validationFailResponse(validationResult);

        Optional<FuelStation> fuelStation = service.addOrEditFuelStation(dto);

        if(fuelStation.isEmpty())
            return ResponseEntity.badRequest()
                                .body(new ErrorDTO(
                                                HttpStatus.BAD_REQUEST.value(), 
                                                "Resource cannot be created", 
                                                "Wrong ownerId"
                                            ));

        URI fuelStationURI = links.linkToItemResource(FuelStation.class, fuelStation.get().getId()).toUri();
        return ResponseEntity.created(fuelStationURI).build();

    }

    @PutMapping
    public ResponseEntity<?> editFuelStation(@RequestBody @Valid AddOrEditFuelStationDTO dto, Errors validationResult) {
        if(validationResult.hasErrors())
            return Validation.validationFailResponse(validationResult);

        return service.addOrEditFuelStation(dto).isEmpty() ? 
                ResponseEntity.notFound().build() : ResponseEntity.ok(null);
    }

}
