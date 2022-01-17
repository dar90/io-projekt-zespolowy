package com.example.fuelprices.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;

import com.example.fuelprices.dto.AddOrEditFuelStationDTO;
import com.example.fuelprices.dto.ErrorDTO;
import com.example.fuelprices.model.FuelStation;
import com.example.fuelprices.service.FuelStationService;

import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/fuelStations")
public class FuelStationController {
    
    private final FuelStationService service;
    private final EntityLinks links;

    public FuelStationController(FuelStationService service, EntityLinks links) {
        this.service = service;
        this.links = links;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR', 'USER')")
    @PostMapping("/create")
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

    @PreAuthorize("hasAuthority('ADMIN', 'MODERATOR')")
    @PutMapping("/update")
    public ResponseEntity<?> editFuelStation(@RequestBody @Valid AddOrEditFuelStationDTO dto, Errors validationResult) {
        if(validationResult.hasErrors())
            return Validation.validationFailResponse(validationResult);

        return service.addOrEditFuelStation(dto).isEmpty() ? 
                ResponseEntity.notFound().build() : ResponseEntity.ok(null);
    }

    @GetMapping("/closest")
    public ResponseEntity<List<FuelStation>> getClosestStations(
        @RequestParam @Max(15) Byte quantity,
        @RequestParam @DecimalMin("-90.0") @DecimalMax("90.0") BigDecimal lat,
        @RequestParam @DecimalMin("-180.0") @DecimalMax("180.0") BigDecimal lng
    ) {
        return ResponseEntity.ok(service.findClosestStations(lat, lng, quantity));
    }

}
