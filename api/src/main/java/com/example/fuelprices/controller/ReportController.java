package com.example.fuelprices.controller;

import java.util.Optional;

import com.example.fuelprices.dto.ErrorDTO;
import com.example.fuelprices.model.Report;
import com.example.fuelprices.service.ReportService;

import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/reports")
public class ReportController {
    
    private final ReportService service;
    private EntityLinks links;

    public ReportController(ReportService service, EntityLinks links) {
        this.service = service;
        this.links = links;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR', 'USER')")
    @PostMapping("/{fuelPriceId}")
    public ResponseEntity<?> reportFuelPrice(@PathVariable("fuelPriceId") Long fuelPriceId) {
        
        Optional<Report> report = service.addReport(fuelPriceId);

        if(report.isEmpty())
        return ResponseEntity.badRequest()
                            .body(new ErrorDTO(
                                            HttpStatus.BAD_REQUEST.value(), 
                                            "Resource cannot be created", 
                                            "Wrong fuelPriceId or you're trying to report second time same price"
                                        ));

        URI reportURI = links.linkToItemResource(Report.class, report.get().getId()).toUri();
        return ResponseEntity.created(reportURI).build();

    }

}
