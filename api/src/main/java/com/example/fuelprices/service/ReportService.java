package com.example.fuelprices.service;

import java.util.Optional;

import com.example.fuelprices.model.FuelPrice;
import com.example.fuelprices.model.Report;
import com.example.fuelprices.model.User;
import com.example.fuelprices.repository.FuelPriceRepository;
import com.example.fuelprices.repository.ReportRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
    
    private final ReportRepository repository;
    private final FuelPriceRepository fuelPriceRepository;

    public ReportService(ReportRepository repository,
                        FuelPriceRepository fuelPriceRepository) {
        this.repository = repository;
        this.fuelPriceRepository = fuelPriceRepository;
    }

    public Optional<Report> addReport(Long fuelPriceId) {
        User author = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FuelPrice fuelPrice = fuelPriceRepository.findById(fuelPriceId);

        if(fuelPrice == null
            || repository.findByAuthorIdAndFuelPriceId(author.getId(), fuelPriceId) != null) 
            return Optional.empty();
        
        Report report = new Report();
        report.setAuthor(author);
        report.setFuelPrice(fuelPrice);

        return Optional.of(repository.save(report));
    }

}
