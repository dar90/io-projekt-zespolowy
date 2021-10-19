package com.example.fuelprices.service;

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

}
