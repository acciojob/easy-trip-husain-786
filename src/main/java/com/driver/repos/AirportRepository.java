package com.driver.repos;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class AirportRepository {

    private Map<String, Airport> airportMap = new HashMap<>();	// name, AirportObject
    private Map<Integer, Flight> flightMap = new HashMap<>();	// id, FlightObject
    private Map<Integer, Passenger> passengerMap = new HashMap<>();	// id, FlightObject
    private Map<Integer, List<Integer>> ticketMap = new HashMap<>();  // flightId, passengerId
    private Map<Integer, List<Integer>> bookingMap = new HashMap<>();     // passengerId, flightId
    private Map<Integer, Integer> revenueMap = new HashMap<>();  // flightId, passengerId
    public void addAirport(Airport airport) {
        System.out.println("Repository");
        airportMap.put(airport.getAirportName(), airport);
    }

    public Optional<String> getLargestAirportName() {
        String name = null;	// denotes the name of largest airport....
        int terminals = 0;	// denotes the terminal of largest airport....

        for (String airportName: airportMap.keySet()) {
            if (name == null && terminals == 0) {
                name = airportName;
                terminals = airportMap.get(airportName).getNoOfTerminals();
            }
            else {
                if (airportMap.get(airportName).getNoOfTerminals() > terminals) {
                    name = airportName;
                    terminals = airportMap.get(airportName).getNoOfTerminals();
                }
                else if (airportMap.get(airportName).getNoOfTerminals() == terminals) {
                    if (airportName.charAt(0) < name.charAt(0)) {
                        name = airportName;
                    }
                }
            }
        }

        if (name.length() != 0)
            return Optional.of(name);

        return null;
    }

    // adding flight...
    public void addFlight(Flight flight) {
        flightMap.put(flight.getFlightId(), flight);
    }

    // adding passenger....
    public void addPassenger(Passenger passenger) {
        passengerMap.put(passenger.getPassengerId(), passenger);
    }

    // finding shortest duration of flight between the given cities.....
    public Optional<Double> getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {

        if (flightMap.size() != 0) {
            System.out.println("Inside repo..");
            double shortestDuration = Double.MAX_VALUE;
            for (int id: flightMap.keySet()) {
                Flight flight = flightMap.get(id);

//				if (((flightMap.get(id).getFromCity()).equals(toCity)) && ((flightMap.get(id).getToCity()).equals(toCity))) {
//					shortestDuration = Math.min(flightMap.get(id).getDuration(), shortestDuration);
//				}

                if ((flight.getFromCity()).equals(fromCity) && (flight.getToCity()).equals(toCity)) {
                    shortestDuration = Math.min(flight.getDuration(), shortestDuration);
                }
            }

            if (shortestDuration == Double.MAX_VALUE) {
                return Optional.empty();
            }

            return Optional.of(shortestDuration);
        }

        return Optional.empty();
    }

    public Optional<List<Flight>> getAllFlight() {
        if (flightMap.size() == 0) {
            return Optional.empty();
        }

        List<Flight> allFlight = new ArrayList<>();
        for (int flightId: flightMap.keySet()) {
            allFlight.add(flightMap.get(flightId));
        }

        return Optional.of(allFlight);
    }

    public Optional<Integer> getNumberOfPeopleOn(Date date, String airportName) {
        int n = 0;
//        List<Integer> list = new ArrayList<>();
        int sum = 0;
		for (int id: flightMap.keySet()) {
			Flight flight = flightMap.get(id);
			if (flight.getFlightDate().equals(date)) {
				n += flight.getMaxCapacity();
                sum += bookingMap.get(flight.getFlightId()).size();
			}
		}
        if (n == 0) {
            return Optional.empty();
        }
//        return Optional.of(n);
        return Optional.of(sum);
    }

    public Optional<String> bookATicket(Integer flightId, Integer passengerId) {
        if ((ticketMap.containsKey(flightId) && flightMap.get(flightId).getMaxCapacity() <= ticketMap.get(flightId).size())){
            return Optional.empty();
        }
        else if (ticketMap.get(flightId).contains(passengerId)){
            return Optional.empty();
        }
        else{
            if (!ticketMap.containsKey(flightId)){
                ticketMap.put(flightId, new ArrayList<>());
            }

            // calculating revenue.......
            int rvnu = 0;
            for (int i=1; i<ticketMap.size(); i++){
                rvnu += (3000 + (i-1)*50);
            }
            revenueMap.put(flightId, rvnu);


            ticketMap.get(flightId).add(passengerId);

            if (!bookingMap.containsKey(passengerId)){
                bookingMap.put(passengerId, new ArrayList<>());
            }
            bookingMap.get(passengerId).add(flightId);
            return Optional.of("SUCCESS");
        }
    }

    public Optional<String> cancelATicket(Integer flightId, Integer passengerId) {
        if (ticketMap.containsKey(flightId) && ticketMap.get(flightId).contains(passengerId)){
            ticketMap.get(flightId).remove(passengerId);

            int n = 3000 + (ticketMap.get(flightId).size()-1)*50;
            int rvnu = revenueMap.get(flightId) - n;
            revenueMap.put(flightId, rvnu);

            return Optional.of("SUCCESS");
        }
        else{
            return Optional.empty();
        }

//        if (!ticketMap.containsKey(flightId) || (ticketMap.containsKey(flightId) && flightMap.get(flightId).getMaxCapacity() == 0)){
//            return Optional.empty();
//        }
//        else if (!ticketMap.get(flightId).contains(passengerId)){
//            return Optional.empty();
//        }
//        else{
//            if (!ticketMap.containsKey(flightId)){
//                ticketMap.put(flightId, new ArrayList<>());
//            }
//            ticketMap.get(flightId).add(passengerId);
//            return Optional.of("SUCCESS");
//        }
    }

    public Optional<Integer> countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
        if (!bookingMap.containsKey(passengerId)){
            return Optional.empty();
        }
        return Optional.of(bookingMap.get(passengerId).size());
    }

    public Optional<String> getAirportNameFromFlightId(Integer flightId) {
        if (!flightMap.containsKey(flightId)){
            return Optional.empty();
        }
        City city = flightMap.get(flightId).getFromCity();

        for (String id: airportMap.keySet()){
            Airport airport = airportMap.get(id);
            if ((airport.getCity()).equals(city)){
                return Optional.of(airport.getAirportName());
            }
        }

        return Optional.empty();
    }

    public int calculateFlightFare(Integer flightId) {
        int fare = 0;
        if (!ticketMap.containsKey(flightId)){
            fare = 3000;
        }
        else if (ticketMap.containsKey(flightId)){
            fare = (50*(ticketMap.get(flightId).size())) + 3000;
        }
        return fare;
    }

    public int calculateRevenueOfAFlight(Integer flightId) {
        return revenueMap.get(flightId);
    }
}