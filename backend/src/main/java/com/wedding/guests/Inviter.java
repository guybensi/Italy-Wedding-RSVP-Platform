package com.wedding.guests;

import com.wedding.flights.FlightDetails;
import com.wedding.music.Song;
import com.wedding.rooms.RoomId;

import java.util.Optional;

public class Inviter {
    // מזהה ייחודי לאורח
    private InviterId id;

    private String fullName;
    private FlightDetails flight;
    private GuestStatus status;
    private DietaryPreference diet;
    private Song favoriteSong;

    // שיוך חדר אופציונלי (null = אין חדר)
    private RoomId roomId;

    public Inviter() {
        this.status = GuestStatus.PENDING;
        this.diet = DietaryPreference.NONE;
    }

    public Inviter(InviterId id,
                   String fullName,
                   FlightDetails flight,
                   GuestStatus status,
                   DietaryPreference diet,
                   Song favoriteSong,
                   RoomId roomId) {
        this.id = id;
        this.fullName = fullName;
        this.flight = flight;
        this.status = status;
        this.diet = diet;
        this.favoriteSong = favoriteSong;
        this.roomId = roomId;
    }

    // === ID ===
    public InviterId getId() { return id; }
    public void setId(InviterId id) { this.id = id; }

    // === Basic fields ===
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public FlightDetails getFlight() { return flight; }
    public void setFlight(FlightDetails flight) { this.flight = flight; }

    public GuestStatus getStatus() { return status; }
    public void setStatus(GuestStatus status) { this.status = status; }

    public DietaryPreference getDiet() { return diet; }
    public void setDiet(DietaryPreference diet) { this.diet = diet; }

    public Song getFavoriteSong() { return favoriteSong; }
    public void setFavoriteSong(Song favoriteSong) { this.favoriteSong = favoriteSong; }

    // === Room linkage (optional) ===
    /** null-safe accessor: אם אין חדר, מחזיר Optional.empty() */
    public Optional<RoomId> getRoomId() { return Optional.ofNullable(roomId); }

    /** שיוך לחדר */
    public void assignRoom(RoomId roomId) { this.roomId = roomId; }

    /** יציאה מהחדר (אין חדר) */
    public void clearRoom() { this.roomId = null; }

    @Override
    public String toString() {
        return "Inviter{" + (id == null ? "no-id" : id.value()) +
                ", name=" + fullName +
                ", status=" + status +
                ", diet=" + diet +
                ", room=" + getRoomId().map(r -> r.value().toString()).orElse("-") +
                "}";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inviter other)) return false;
        return java.util.Objects.equals(id, other.id);
    }
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }

}
