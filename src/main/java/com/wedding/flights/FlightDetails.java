package com.wedding.flights;

import java.time.LocalDate;
import java.time.LocalTime;

public class FlightDetails {
    private String flightNumber;
    private String airline;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private Airport origin;
    private Airport destination;

    public FlightDetails() {
        this.flightNumber = null;
        this.airline = null;
        this.departureDate = null;
        this.departureTime = null;
        this.origin = null;
        this.destination = null;
    }

    public FlightDetails(String flightNumber, String airline,
                         LocalDate departureDate, LocalTime departureTime,
                         Airport origin, Airport destination) {
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.origin = origin;
        this.destination = destination;
    }

    // Getters & Setters
    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getAirline() { return airline; }
    public void setAirline(String airline) { this.airline = airline; }

    public LocalDate getDepartureDate() { return departureDate; }
    public void setDepartureDate(LocalDate departureDate) { this.departureDate = departureDate; }

    public LocalTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalTime departureTime) { this.departureTime = departureTime; }

    public Airport getOrigin() { return origin; }
    public void setOrigin(Airport origin) { this.origin = origin; }

    public Airport getDestination() { return destination; }
    public void setDestination(Airport destination) { this.destination = destination; }

    @Override
    public String toString() {
        return "FlightDetails{" +
                airline + "|" + flightNumber + "|" +
                (departureDate == null ? "" : departureDate) + " " +
                (departureTime == null ? "" : departureTime) + " " +
                origin + "→" + destination +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlightDetails f)) return false;
        return java.util.Objects.equals(airline, f.airline) &&
            java.util.Objects.equals(flightNumber, f.flightNumber) &&
            java.util.Objects.equals(departureDate, f.departureDate);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(airline, flightNumber, departureDate);
    }

}

