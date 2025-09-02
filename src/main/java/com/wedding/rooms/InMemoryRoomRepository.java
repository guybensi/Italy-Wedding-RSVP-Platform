package com.wedding.rooms;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import com.wedding.guests.Inviter;
import com.wedding.guests.InviterRepository; // אם יש לך כזה; אחרת ספק פונקציה סופרת

public class InMemoryRoomRepository implements RoomRepository {
    private final ConcurrentMap<UUID, Room> rooms = new ConcurrentHashMap<>();
    private final InviterRepository inviters;

    public InMemoryRoomRepository(InviterRepository inviters){ this.inviters = inviters; }

    @Override public Room save(Room r){ rooms.put(r.id().value(), r); return r; }
    @Override public Optional<Room> findById(RoomId id){ return Optional.ofNullable(rooms.get(id.value())); }
    @Override public Optional<Room> findByName(String name){
        String key = name == null ? "" : name.trim().toLowerCase();
        return rooms.values().stream().filter(r -> r.getName().trim().toLowerCase().equals(key)).findFirst();
    }
    @Override public List<Room> findAll(){ return new ArrayList<>(rooms.values()); }
    @Override public void delete(RoomId id){ rooms.remove(id.value()); }
    @Override public long countMembers(RoomId id){
        // נסמך על שדה roomId ב-Inviter (להלן)
        return inviters.findAll().stream().filter(i -> i.getRoomId().map(id::equals).orElse(false)).count();
    }
}
