package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Data
@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String activityName;
    private String description;
    private double cost;
    private int capacity;

    @ManyToOne
    @JoinColumn(name = "destination_id")
    private Destination destination;

    @ManyToMany(mappedBy = "signedUpActivities")
    private List<Passenger> passengers;
}
