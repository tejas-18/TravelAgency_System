package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Data
@Entity
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String destinationName;

    @ManyToOne
    @JoinColumn(name = "travelPackage_id")
    private TravelPackage travelPackage;

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL)
    private List<Activity> activities;
}
