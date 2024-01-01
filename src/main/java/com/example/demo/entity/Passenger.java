package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int passengerNumber;

    @Enumerated(EnumType.STRING)
    private PassengerType passengerType;

    private double balance;

    @ManyToMany
    @JoinTable(
            name = "passenger_activity",
            joinColumns = @JoinColumn(name = "passenger_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id"))
    private List<Activity> signedUpActivities;

    @ManyToOne
    @JoinColumn(name = "travelPackage_id")
    private TravelPackage travelPackage;

    public void signUpForActivity(Activity activity) {
        // Check if the passenger has sufficient balance
        if (hasSufficientBalance(activity.getCost())) {
            // Apply discount for gold passengers
            double discountedCost = calculateDiscount(activity.getCost());
            // Deduct the cost from the balance
            this.balance -= discountedCost;

            // Sign up the passenger for the activity
            signedUpActivities.add(activity);
            activity.getPassengers().add(this);
        }
    }

    private boolean hasSufficientBalance(double cost) {
        // Check if the passenger has sufficient balance
        return this.balance >= cost;
    }

    private double calculateDiscount(double cost) {
        // Calculate discount based on passenger type
        if (PassengerType.GOLD.equals(this.passengerType)) {
            return 0.9 * cost; // 10% discount for gold passengers
        } else {
            return cost; // No discount for other passenger types
        }
    }
}

