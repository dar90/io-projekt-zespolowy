package com.example.fuelprices.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "application_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @NotNull
    @Column(unique = true)
    private String firebaseId;

    @Transient
    private String name;

    // @JsonIgnore
    // private String password;

    // @JsonIgnore
    // @Email
    // @Column(unique = true)
    @Transient
    private String email;

    @OneToMany(mappedBy = "author", cascade = {CascadeType.PERSIST})
    private List<Report> reports;

    @OneToMany(mappedBy = "author", cascade = {CascadeType.PERSIST})
    private List<FuelPrice> fuelPrices;

    // @NotNull
    // @Enumerated(EnumType.STRING)
    // private UserAccountType accountType;

    @Transient
    private UserRole role;

    @OneToMany(mappedBy = "author", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Comment> comments;

    @OneToMany(mappedBy = "stationOwner", cascade = {CascadeType.PERSIST})
    private List<FuelStation> fuelStations;

    @Transient
    private boolean locked = false;

    @Transient
    private boolean emailConfirmed;

    public int getFuelStationsQuantity() {
        return fuelStations.size();
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.toString()));
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return name;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return emailConfirmed;
    }

    @Override
    public String getPassword() {
        return null;
    }

}
