package com.wedding.rooms;

import com.wedding.guests.InviterId; // אם אתה משתמש במזהה אורח; אם לא – החלף ל-ID המתאים
import java.util.*;

public interface RoomService {
    RoomId createRoom(String name, RoomType type, Set<String> tags); // MANAGER או self-create
    void renameRoom(RoomId id, String newName);                       // MANAGER
    void changeType(RoomId id, RoomType newType);                     // MANAGER (מוודא שלא יחרוג)
    void deleteRoom(RoomId id, boolean force);                        // MANAGER

    List<Room> listRooms();
    Map<RoomId, Long> roomOccupancy();

    // Self (האורח על עצמו)
    void joinExistingRoom(InviterId me, RoomId roomId);
    RoomId createAndJoinMyRoom(InviterId me, String roomName, RoomType type);
    void leaveRoom(InviterId me); // מאפשר "ללא חדר"
}
