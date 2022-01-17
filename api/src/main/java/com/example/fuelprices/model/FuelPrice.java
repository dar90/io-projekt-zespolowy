package com.example.fuelprices.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuelPrice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private FuelType fuelType;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "station_id", referencedColumnName = "id")
    private FuelStation fuelStation;

    @JsonIgnore
    @OneToMany(mappedBy = "fuelPrice", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Report> reports;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @NotNull
    private LocalDateTime dateTime;

    @NotNull
    @DecimalMin("0.01")
    @DecimalMax("9.99")
    @Digits(integer = 1, fraction = 2)
    private BigDecimal price;

    public int getReportsQuantity() {
        return reports.size();
    }

}
