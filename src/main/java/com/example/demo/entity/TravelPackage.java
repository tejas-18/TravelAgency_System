package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
public class TravelPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String travelPackageName;
    private int passengerCapacity;

    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
        passenger.setTravelPackage(this);
    }

    @OneToMany(mappedBy = "travelPackage", cascade = CascadeType.ALL)
    private List<Destination> destinations;

    @OneToMany(mappedBy = "travelPackage", cascade = CascadeType.ALL)
    private List<Passenger> passengers;
}

