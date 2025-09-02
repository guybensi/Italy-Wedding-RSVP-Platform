package com.wedding;

import com.wedding.flights.*;
import com.wedding.guests.*;
import com.wedding.rooms.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    private InMemoryInviterRepository inviters;
    private RoomRepository roomRepo;
    private RoomService rooms;
    private RoomPermissionService roomPerms;
    private FlightService flights;

    private InviterId aId, bId, cId, dId;

    @BeforeEach
    void setup() {
        inviters = new InMemoryInviterRepository();
        roomRepo = new InMemoryRoomRepository(inviters);
        rooms = new DefaultRoomService(roomRepo, inviters);
        roomPerms = new RoomPermissionService(rooms, inviters);
        flights = new DefaultFlightService(inviters);

        // אורחים
        aId = inviters.create("Noa Levi").getId();
        bId = inviters.create("Yossi Cohen").getId();
        cId = inviters.create("Dana Azulay").getId();
        dId = inviters.create("Avi Mor").getId();

        // טיסה משותפת A,B,C
        FlightDetails fco = new FlightDetails(
                "AZ807", "ITA",
                LocalDate.of(2026, 7, 10), LocalTime.of(6, 30),
                Airport.TelAviv, Airport.RomeFiumicino
        );
        inviters.findById(aId).orElseThrow().setFlight(fco);
        inviters.findById(bId).orElseThrow().setFlight(fco);
        inviters.findById(cId).orElseThrow().setFlight(fco);

        // D בטיסה אחרת
        FlightDetails piza = new FlightDetails(
                "LY381", "ElAl",
                LocalDate.of(2026, 7, 10), LocalTime.of(7, 45),
                Airport.TelAviv, Airport.Pisa
        );
        inviters.findById(dId).orElseThrow().setFlight(piza);

        // שמירה
        inviters.save(inviters.findById(aId).orElseThrow());
        inviters.save(inviters.findById(bId).orElseThrow());
        inviters.save(inviters.findById(cId).orElseThrow());
        inviters.save(inviters.findById(dId).orElseThrow());
    }

    // 1) הוספת משתמש שכבר קיים (לפי שם) → נכשל
    @Test
    void add_user_already_exists_by_name_should_fail() {
        Inviter dup = new Inviter();
        dup.setFullName("Noa Levi"); // כבר קיימת
        assertThrows(IllegalStateException.class, () -> inviters.saveEnforcingUniqueName(dup));
    }

    // 2) נסיון מחיקת חדר של בן אדם אחר ע"י משתמש שאינו מנהל → נכשל (הרשאות)
    @Test
    void user_cannot_remove_other_user_from_room() {
        // צור חדר והכנס את A
        RoomId r = rooms.createAndJoinMyRoom(aId, "R1", RoomType.DOUBLE);
        assertEquals(1L, roomRepo.countMembers(r));

        // משתמש B (ROLE USER) מנסה להוציא את A → חייב להיכשל (SecurityException)
        assertThrows(SecurityException.class, () ->
                roomPerms.managerRemoveFromRoom(GuestType.NOT_MANAGER, aId));
    }

    // 3) בדיקת האנשים שאיתי בטיסה תקינה
    @Test
    void flight_mates_correct() {
        List<Inviter> matesA = flights.findMyFlightMates(aId);
        var names = matesA.stream().map(Inviter::getFullName).sorted().toList();
        assertEquals(List.of("Dana Azulay","Yossi Cohen"), names);

        assertTrue(flights.findMyFlightMates(dId).isEmpty(), "D supposed to have no mates");
    }

    // 4) מחיקת חדר ע"י מנהל → הצלחה, ומתעדכן אצל המוזמן שאין לו חדר
    @Test
    void manager_delete_room_should_clear_guest_room() {
        // צור TRIPLE והכנס A,B
        RoomId r = rooms.createRoom("AdminRoom", RoomType.TRIPLE, Set.of());
        rooms.joinExistingRoom(aId, r);
        rooms.joinExistingRoom(bId, r);
        assertEquals(2L, roomRepo.countMembers(r));

        // מחיקה עם הרשאת מנהל (אפשר למחוק גם דרך rooms.deleteRoom(r, true))
        roomPerms.managerCreateRoom(GuestType.MANAGER, "Dummy", RoomType.DOUBLE); // סתם לוודא שהרשאות עובדות
        rooms.deleteRoom(r, true); // force: מנקה שיוכים

        // וידוא
        assertTrue(inviters.findById(aId).orElseThrow().getRoomId().isEmpty(), "A should have no room after delete");
        assertTrue(inviters.findById(bId).orElseThrow().getRoomId().isEmpty(), "B should have no room after delete");
        assertTrue(roomRepo.findAll().stream().noneMatch(x -> x.id().equals(r)), "Room should be gone");
    }

    // ✨ עוד כמה בדיקות שימושיות:

    // Duplicate room name should fail
    @Test
    void duplicate_room_name_should_fail() {
        rooms.createRoom("DupName", RoomType.DOUBLE, Set.of());
        assertThrows(IllegalStateException.class, () -> rooms.createRoom("DupName", RoomType.TRIPLE, Set.of()));
    }

    // Change type when occupancy fits → success
    @Test
    void change_type_when_occupancy_fits_should_succeed() {
        RoomId r = rooms.createRoom("ResizeOK", RoomType.TRIPLE, Set.of());
        rooms.joinExistingRoom(aId, r);
        rooms.joinExistingRoom(bId, r);
        // יש 2 בפנים → אפשר להקטין ל-DOUBLE
        ((DefaultRoomService)rooms).changeType(r, RoomType.DOUBLE);
        assertEquals(2L, roomRepo.countMembers(r));
    }

    // Leave room when not in any room → no-op
    @Test
    void leave_room_when_none_should_be_noop() {
        assertTrue(inviters.findById(cId).orElseThrow().getRoomId().isEmpty());
        rooms.leaveRoom(cId); // לא צריך לזרוק
        assertTrue(inviters.findById(cId).orElseThrow().getRoomId().isEmpty());
    }

    // Create invalid room name → fail
    @Test
    void create_invalid_room_name_should_fail() {
        assertThrows(IllegalArgumentException.class, () -> rooms.createRoom("   ", RoomType.DOUBLE, Set.of()));
        assertThrows(IllegalArgumentException.class, () -> rooms.createRoom(null, RoomType.DOUBLE, Set.of()));
    }
}
