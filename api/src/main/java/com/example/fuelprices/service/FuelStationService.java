package com.example.fuelprices.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.example.fuelprices.dto.AddOrEditFuelStationDTO;
import com.example.fuelprices.model.FuelStation;
import com.example.fuelprices.model.User;
import com.example.fuelprices.repository.FuelStationRepository;
import com.example.fuelprices.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class FuelStationService {
    
    private final FuelStationRepository repository;
    private final UserRepository userRepository;

    public FuelStationService(FuelStationRepository repository,
                                UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public Optional<FuelStation> addOrEditFuelStation(AddOrEditFuelStationDTO dto) {
        return dto.id() == null ? addFuelStation(dto) : editFuelStation(dto);
    }

    private Optional<FuelStation> addFuelStation(AddOrEditFuelStationDTO dto) { 
        FuelStation fuelStation = new FuelStation();

        if(dto.ownerId() != null) {
            User stationOwner = userRepository.findById(dto.ownerId());
            if(stationOwner == null) return Optional.empty();
            fuelStation.setStationOwner(stationOwner);
        }
        
        fuelStation.setBrand(dto.brand());
        fuelStation.setLatitude(dto.latitude());
        fuelStation.setLongitude(dto.longitude());
        fuelStation.setName(dto.name());
        fuelStation.setCity(dto.city());
        fuelStation.setStreet(dto.street());
        fuelStation.setPlotNumber(dto.plotNumber());
        fuelStation.setServices(dto.services());
        fuelStation.setLogoUrl(dto.logoUrl());

        return Optional.of(repository.save(fuelStation));
    } 

    private Optional<FuelStation> editFuelStation(AddOrEditFuelStationDTO dto) {
        FuelStation fuelStation = repository.findById(dto.id());

        if(fuelStation == null) 
            return Optional.empty();

        if(!dto.ownerId().equals(fuelStation.getStationOwner().getId())) {
            User stationOwner = userRepository.findById(dto.ownerId());
            if(stationOwner == null) return Optional.empty();
            fuelStation.setStationOwner(stationOwner);
        }

        fuelStation.setBrand(dto.brand());
        fuelStation.setName(dto.name());

        return Optional.of(repository.save(fuelStation));
    } 

    public List<FuelStation> findClosestStations(BigDecimal lat, BigDecimal lng, int quantity) {
        return repository.findAll().stream()
                            .sorted((s1,s2) -> {
                                BigDecimal dist1 = BigDecimal.valueOf(
                                    Math.sqrt(
                                        Math.pow((s1.getLatitude().subtract(lat)).doubleValue(), 2.0)) +
                                        Math.pow((s1.getLongitude().subtract(lng).doubleValue()), 2.0)
                                    );
                                BigDecimal dist2 = BigDecimal.valueOf(
                                    Math.sqrt(
                                        Math.pow((s2.getLatitude().subtract(lat)).doubleValue(), 2.0)) +
                                        Math.pow((s2.getLongitude().subtract(lng).doubleValue()), 2.0)
                                    );
                                return dist1.compareTo(dist2);
                            })
                            .limit(quantity)
                            .toList();
    }

}
