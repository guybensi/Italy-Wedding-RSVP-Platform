package com.wedding.rooms;

import java.util.*;

public interface RoomRepository {
    Room save(Room r);
    Optional<Room> findById(RoomId id);
    Optional<Room> findByName(String name);
    List<Room> findAll();
    void delete(RoomId id);

    long countMembers(RoomId id); // ימומש דרך InviterRepository (בהזרקה)
}
