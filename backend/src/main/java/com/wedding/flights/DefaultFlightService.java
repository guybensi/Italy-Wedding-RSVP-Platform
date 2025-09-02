package com.wedding.flights;

import com.wedding.guests.Inviter;
import com.wedding.guests.InviterId;
import com.wedding.guests.InviterRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultFlightService implements FlightService {
    private final InviterRepository inviters;

    public DefaultFlightService(InviterRepository inviters) {
        this.inviters = inviters;
    }

    @Override
    public List<Inviter> findMyFlightMates(InviterId me) {
        Inviter self = inviters.findById(me).orElseThrow();
        FlightDetails mf = self.getFlight();
        if (mf == null || mf.getAirline() == null || mf.getFlightNumber() == null || mf.getDepartureDate() == null) {
            return List.of(); // אין טיסה מוגדרת לעצמו
        }
        return inviters.findAll().stream()
                .filter(i -> i.getId() != null && !i.getId().equals(me))
                .filter(i -> sameFlight(mf, i.getFlight()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Inviter> findByFlight(String airline, String flightNumber, LocalDate departureDate) {
        return inviters.findAll().stream()
                .filter(i -> sameFlight(airline, flightNumber, departureDate, i.getFlight()))
                .collect(Collectors.toList());
    }

    private boolean sameFlight(FlightDetails a, FlightDetails b) {
        if (a == null || b == null) return false;
        return sameFlight(a.getAirline(), a.getFlightNumber(), a.getDepartureDate(), b);
    }

    private boolean sameFlight(String airline, String flightNumber, LocalDate date, FlightDetails b) {
        if (airline == null || flightNumber == null || date == null || b == null) return false;
        return Objects.equals(airline, b.getAirline()) &&
               Objects.equals(flightNumber, b.getFlightNumber()) &&
               Objects.equals(date, b.getDepartureDate());
    }
}
