package com.wedding.rooms;

import com.wedding.guests.Inviter;
import com.wedding.guests.InviterId;
import com.wedding.guests.InviterRepository;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultRoomService implements RoomService {
    private final RoomRepository rooms;
    private final InviterRepository inviters;

    public DefaultRoomService(RoomRepository rooms, InviterRepository inviters){
        this.rooms = rooms; this.inviters = inviters;
    }

    @Override public RoomId createRoom(String name, RoomType type, Set<String> tags){
        validateNewRoomName(name);
        Room r = new Room(RoomId.newId(), name, type, tags == null ? Set.of() : tags);
        rooms.save(r);
        return r.id();
    }

    @Override public void renameRoom(RoomId id, String newName){
        validateNewRoomName(newName);
        Room r = rooms.findById(id).orElseThrow();
        r.setName(newName);
        rooms.save(r);
    }

    @Override public void changeType(RoomId id, RoomType newType){
        Room r = rooms.findById(id).orElseThrow();
        long occ = rooms.countMembers(id);
        if (occ > newType.capacity())
            throw new IllegalStateException("room type capacity ("+newType.capacity()+") is smaller than current occupancy ("+occ+")");
        r.setType(newType);
        rooms.save(r);
    }

    @Override public void deleteRoom(RoomId id, boolean force){
        long occ = rooms.countMembers(id);
        if (occ > 0 && !force) throw new IllegalStateException("room not empty");
        if (force){
            inviters.findAll().forEach(i -> {
                if (i.getRoomId().map(id::equals).orElse(false)) { i.clearRoom(); inviters.save(i); }
            });
        }
        rooms.delete(id);
    }

    @Override public List<Room> listRooms(){ return rooms.findAll(); }

    @Override public Map<RoomId, Long> roomOccupancy(){
        return rooms.findAll().stream().collect(Collectors.toMap(Room::id, r -> rooms.countMembers(r.id())));
    }

    @Override public void joinExistingRoom(InviterId me, RoomId roomId){
        Room r = rooms.findById(roomId).orElseThrow();
        long occ = rooms.countMembers(roomId);
        if (occ >= r.getType().capacity())
            throw new IllegalStateException("room is full ("+r.getType().capacity()+")");
        Inviter inv = inviters.findById(me).orElseThrow();
        inv.assignRoom(roomId);
        inviters.save(inv);
    }

    @Override public RoomId createAndJoinMyRoom(InviterId me, String roomName, RoomType type){
        RoomId id = createRoom(roomName, type, Set.of());
        joinExistingRoom(me, id);
        return id;
    }

    @Override public void leaveRoom(InviterId me){
        Inviter inv = inviters.findById(me).orElseThrow();
        inv.clearRoom(); // המשמעות: אין חדר
        inviters.save(inv);
    }

    private void validateNewRoomName(String name){
        if (name == null || name.isBlank()) throw new IllegalArgumentException("room name required");
        if (rooms.findByName(name).isPresent()) throw new IllegalStateException("room name already exists");
    }
}
