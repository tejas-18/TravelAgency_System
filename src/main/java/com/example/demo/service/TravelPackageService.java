package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.ActivityRepository;
import com.example.demo.repository.DestinationRepository;
import com.example.demo.repository.PassengerRepository;
import com.example.demo.repository.TravelPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TravelPackageService {

    @Autowired
    private TravelPackageRepository travelPackageRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private PassengerRepository passengerRepository;


    public void createSampleTravelPackage() {
        // Create a sample travel package
        TravelPackage travelPackage = new TravelPackage();
        travelPackage.setTravelPackageName("Summer Vacation");
        travelPackage.setPassengerCapacity(50);

        // Create a destination
        Destination destination = new Destination();
        destination.setDestinationName("Beach Paradise");
        destination.setTravelPackage(travelPackage);

        // Create an activity
        Activity activity = new Activity();
        activity.setActivityName("Scuba Diving");
        activity.setDescription("Explore the underwater world");
        activity.setCost(100.0);
        activity.setCapacity(20);
        activity.setDestination(destination);

        // Add the activity to the destination
        destination.setActivities(List.of(activity));

        // Add the destination to the travel package
        travelPackage.setDestinations(List.of(destination));

        // Create sample passengers
        Passenger standardPassenger = new Passenger();
        standardPassenger.setName("John Doe");
        standardPassenger.setPassengerType(PassengerType.STANDARD);
        standardPassenger.setBalance(500.0);
        standardPassenger.setTravelPackage(travelPackage);
        standardPassenger.setPassengerNumber(1);


        Passenger goldPassenger = new Passenger();
        goldPassenger.setName("Jane Doe");
        goldPassenger.setPassengerType(PassengerType.GOLD);
        goldPassenger.setBalance(800.0);
        goldPassenger.setTravelPackage(travelPackage);
        goldPassenger.setPassengerNumber(2);



        Passenger premiumPassenger = new Passenger();
        premiumPassenger.setName("Alice");
        premiumPassenger.setPassengerType(PassengerType.PREMIUM);
        premiumPassenger.setTravelPackage(travelPackage);
        premiumPassenger.setPassengerNumber(3);

//        // Save entities to the database
//        travelPackageRepository.save(travelPackage);
//        passengerRepository.saveAll(List.of(standardPassenger, goldPassenger, premiumPassenger));

        // Save entities to the database
        travelPackageRepository.save(travelPackage);

        // Set the travel package for each passenger
        standardPassenger.setTravelPackage(travelPackage);
        goldPassenger.setTravelPackage(travelPackage);
        premiumPassenger.setTravelPackage(travelPackage);

        // Save passengers to the database
        passengerRepository.saveAll(List.of(standardPassenger, goldPassenger, premiumPassenger));

    }
    public String getItinerary(Long packageId) {
        Optional<TravelPackage> travelPackageOptional = travelPackageRepository.findById(packageId);
        if (travelPackageOptional.isPresent()) {
            TravelPackage travelPackage = travelPackageOptional.get();

            StringBuilder itinerary = new StringBuilder();
            itinerary.append("Travel Package Name: ").append(travelPackage.getTravelPackageName()).append("\n");

            for (Destination destination : travelPackage.getDestinations()) {
                itinerary.append("Destination: ").append(destination.getDestinationName()).append("\n");

                for (Activity activity : destination.getActivities()) {
                    itinerary.append("  Activity: ").append(activity.getActivityName())
                            .append(", Cost: ").append(activity.getCost())
                            .append(", Capacity: ").append(activity.getCapacity())
                            .append(", Description: ").append(activity.getDescription()).append("\n");
                }
            }

            return itinerary.toString();
        } else {
            return "Travel Package not found.";
        }
    }

    public String getPassengerList(Long packageId) {
        Optional<TravelPackage> travelPackageOptional = travelPackageRepository.findById(packageId);
        if (travelPackageOptional.isPresent()) {
            TravelPackage travelPackage = travelPackageOptional.get();

            StringBuilder passengerList = new StringBuilder();
            passengerList.append("Travel Package Name: ").append(travelPackage.getTravelPackageName()).append("\n");
            passengerList.append("Passenger Capacity: ").append(travelPackage.getPassengerCapacity()).append("\n");
            passengerList.append("Number of Passengers Enrolled: ").append(travelPackage.getPassengers().size()).append("\n");

            for (Passenger passenger : travelPackage.getPassengers()) {
                passengerList.append("  Passenger Name: ").append(passenger.getName())
                        .append(", Passenger Number: ").append(passenger.getId()).append("\n");
            }

            return passengerList.toString();
        } else {
            return "Travel Package not found.";
        }
    }

    public String getPassengerDetails(Long passengerId) {
        Optional<Passenger> passengerOptional = passengerRepository.findById(passengerId);
        if (passengerOptional.isPresent()) {
            Passenger passenger = passengerOptional.get();

            StringBuilder passengerDetails = new StringBuilder();
            passengerDetails.append("Passenger Name: ").append(passenger.getName()).append("\n");
            passengerDetails.append("Passenger Number: ").append(passenger.getId()).append("\n");

            if (passenger.getPassengerType() != null) {
                passengerDetails.append("Passenger Type: ").append(passenger.getPassengerType()).append("\n");
            }

            if (passenger.getBalance() > 0) {
                passengerDetails.append("Balance: ").append(passenger.getBalance()).append("\n");
            }

            if (!passenger.getSignedUpActivities().isEmpty()) {
                passengerDetails.append("Signed Up Activities: \n");

                for (Activity activity : passenger.getSignedUpActivities()) {
                    passengerDetails.append("  Activity Name: ").append(activity.getActivityName())
                            .append(", Destination: ").append(activity.getDestination().getDestinationName())
                            .append(", Price Paid: ").append(activity.getCost()).append("\n");
                }
            }

            return passengerDetails.toString();
        } else {
            return "Passenger not found.";
        }
    }

    public String getAvailableActivities(Long packageId) {
        Optional<TravelPackage> travelPackageOptional = travelPackageRepository.findById(packageId);
        if (travelPackageOptional.isPresent()) {
            TravelPackage travelPackage = travelPackageOptional.get();

            StringBuilder availableActivities = new StringBuilder();
            availableActivities.append("Available Activities: \n");

            for (Destination destination : travelPackage.getDestinations()) {
                for (Activity activity : destination.getActivities()) {
                    int remainingCapacity = activity.getCapacity() - activity.getPassengers().size();
                    if (remainingCapacity > 0) {
                        availableActivities.append("  Activity Name: ").append(activity.getActivityName())
                                .append(", Destination: ").append(destination.getDestinationName())
                                .append(", Remaining Capacity: ").append(remainingCapacity).append("\n");
                    }
                }
            }

            return availableActivities.toString();
        } else {
            return "Travel Package not found.";
        }
    }

    public List<TravelPackage> getAllTravelPackages() {
        return travelPackageRepository.findAll();
    }

    public void signUpPassengerForActivity(Long passengerId, Long activityId) throws Exception {
        Passenger passenger = passengerRepository.findById(passengerId).orElseThrow(Exception::new);
        Activity activity = activityRepository.findById(activityId).orElseThrow(Exception::new);

        // Check if the activity has available capacity
        if (activity.getPassengers().size() < activity.getCapacity()) {
            passenger.signUpForActivity(activity);
            passengerRepository.save(passenger);
            activityRepository.save(activity);
        } else {
            throw new Exception("Activity is already at full capacity.");
        }
    }


    public void addPassenger(Passenger passenger, Long travelPackageId) {
        Optional<TravelPackage> travelPackageOptional = travelPackageRepository.findById(travelPackageId);

        if (travelPackageOptional.isPresent()) {
            TravelPackage travelPackage = travelPackageOptional.get();

            // Set the travel package for the passenger
            passenger.setTravelPackage(travelPackage);

            // Save the passenger to the database
            passengerRepository.save(passenger);

            // Update the travel package to include the new passenger
            travelPackage.addPassenger(passenger);  // Use the addPassenger method
            travelPackageRepository.save(travelPackage);
        } else {
            // Handle case where the specified travel package is not found
            throw new RuntimeException("Travel Package not found for ID: " + travelPackageId);
        }
    }


}
