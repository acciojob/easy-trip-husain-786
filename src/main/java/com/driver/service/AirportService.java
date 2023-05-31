package com.driver.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import com.driver.repos.AirportRepository;

@Service
public class AirportService {

    @Autowired
    private AirportRepository airportRepository;

    public String bookATicket(Integer flightId, Integer passengerId) {
        Optional<String> response = airportRepository.bookATicket(flightId, passengerId);
        if (response.isEmpty()){
            return "FAILURE";
        }
        return response.get();
    }

    public void addAirport(Airport airport) {
        System.out.println("Service");
        airportRepository.addAirport(airport);
    }

    public String getLargestAirportName() {
        Optional<String> largestAirport = airportRepository.getLargestAirportName();
        if (!largestAirport.isEmpty()) {
            return largestAirport.get();
        }
        return null;
    }

    public void addFlight(Flight flight) {
        airportRepository.addFlight(flight);
    }

    public void addPassenger(Passenger passenger) {
        airportRepository.addPassenger(passenger);
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {
        Optional<Double> shortestDuration = airportRepository.getShortestDurationOfPossibleBetweenTwoCities(fromCity, toCity);
        if (shortestDuration.isEmpty()) {
//			System.out.println(shortestDuration.get());
//			System.out.println("Getting Nothing...");
            return (double)(-1);
        }
        System.out.println("Sajjad Husain" + shortestDuration.get());
        return shortestDuration.get();
    }

    public List<Flight> getAllFlight() {
        Optional<List<Flight>> allFlight = airportRepository.getAllFlight();
        if (allFlight.isEmpty()) {
            return null;
        }
        return allFlight.get();
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
        Optional<Integer> totalPassengers = airportRepository.getNumberOfPeopleOn(date, airportName);
//		if ()
        return 0;
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {
        Optional<String> message = airportRepository.cancelATicket(flightId, passengerId);
        if (message.isEmpty()){
            return "FAILURE";
        }
        else{
            return message.get();
        }
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
        Optional<Integer> response = airportRepository.countOfBookingsDoneByPassengerAllCombined(passengerId);
        if (response.isEmpty()){
            return 0;
        }
        return response.get();
    }

    public String getAirportNameFromFlightId(Integer flightId) {
        Optional<String> response = airportRepository.getAirportNameFromFlightId(flightId);
        if (response.isEmpty()){
            return null;
        }
        return response.get();
    }

    public int calculateFlightFare(Integer flightId) {
        return airportRepository.calculateFlightFare(flightId);
    }

    public int calculateRevenueOfAFlight(Integer flightId) {
        return airportRepository.calculateRevenueOfAFlight(flightId);
    }
}
