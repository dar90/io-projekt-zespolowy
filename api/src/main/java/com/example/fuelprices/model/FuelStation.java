package com.example.fuelprices.model;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuelStation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    @Digits(integer = 2, fraction = 8)
    private BigDecimal latitude;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    @Digits(integer = 3, fraction = 8)
    private BigDecimal longitude;

    @NotBlank
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FuelStationBrand brand;

    @JsonIgnore
    @OneToMany(mappedBy = "station", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Comment> comments;

    @JsonIgnore
    @OneToMany(mappedBy = "fuelStation", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<FuelPrice> prices;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User stationOwner;

    private boolean verified = false;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<FuelStationServices> services;

    @NotBlank
    private String city;

    private String street;

    @NotBlank
    private String plotNumber;

    private URL logoUrl;

}
