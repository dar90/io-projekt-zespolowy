package com.example.fuelprices.repository;

import java.util.List;

import com.example.fuelprices.model.Report;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

public interface ReportRepository extends Repository<Report, Long> {
    Report findById(Long id);

    @PreAuthorize("hasAuthority('ADMIN')")
    List<Report> findAll();

    @RestResource(exported = false)
    Report save(Report report);

    @RestResource(exported = false)
    Report findByAuthorIdAndFuelPriceId(long authorId, Long fuelPriceId);

}
