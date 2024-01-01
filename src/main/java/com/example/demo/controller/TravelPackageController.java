package com.example.demo.controller;

import com.example.demo.entity.Passenger;
import com.example.demo.service.TravelPackageService;
import com.example.demo.entity.TravelPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travel-packages")
public class TravelPackageController {

    @Autowired
    private TravelPackageService travelPackageService;

    @GetMapping("/hello/{name}")    /// localhost:8081/api/travel-packages/hello/tejas
    public String hello(@PathVariable String name){
        return "Hello " + name;
    }

    @PostMapping("/create")
    public String createTravelPackage() {
        travelPackageService.createSampleTravelPackage();
        return "Travel Package Created!";
    }

    @GetMapping("/itinerary/{packageId}")
    public String getItinerary(@PathVariable Long packageId) {
        return travelPackageService.getItinerary(packageId);
    }

    @GetMapping("/a")
    @ResponseBody
    public String getItinerary() {
        return "a";
    }

    @GetMapping("/all")
    public List<TravelPackage> getAllTravelPackages() {
        return travelPackageService.getAllTravelPackages();
    }

    @PostMapping("/sign-up/{passengerId}/{activityId}")
    public String signUpForActivity(
            @PathVariable Long passengerId,
            @PathVariable Long activityId
    ) {
        try {
            travelPackageService.signUpPassengerForActivity(passengerId, activityId);
            return "Passenger signed up for the activity!";
        } catch (Exception e) {
            return "Failed to sign up for the activity. Reason: " + e.getMessage();
        }
    }

    /*@PostMapping("/add-passenger")
    public String addPassenger(@RequestBody Passenger passenger) {
        travelPackageService.addPassenger(passenger);
        return "Passenger added successfully!";
    }*/

    @PostMapping("/add-passenger/{travelPackageId}")
    public String addPassenger(@RequestBody Passenger passenger, @PathVariable Long travelPackageId) {
        travelPackageService.addPassenger(passenger, travelPackageId);
        return "Passenger added successfully!";
    }

    @GetMapping("/available-activities/{packageId}")
    public String getAvailableActivities(@PathVariable Long packageId) {
        return travelPackageService.getAvailableActivities(packageId);
    }

    @GetMapping("/passenger-details/{passengerId}")
    public String getPassengerDetails(@PathVariable Long passengerId) {
        return travelPackageService.getPassengerDetails(passengerId);
    }

    @GetMapping("/passenger-list/{packageId}")
    public String getPassengerList(@PathVariable Long packageId) {
        return travelPackageService.getPassengerList(packageId);
    }



}


