package com.wedding.flights;

import com.wedding.guests.Inviter;
import com.wedding.guests.InviterId;
import java.time.LocalDate;
import java.util.List;

public interface FlightService {
    /** בני־טיסה של אורח מסוים (ללא עצמו) */
    List<Inviter> findMyFlightMates(InviterId me);

    /** כל האורחים בטיסה מסוימת (כולל מגדיר הקריטריון) */
    List<Inviter> findByFlight(String airline, String flightNumber, LocalDate departureDate);
}
