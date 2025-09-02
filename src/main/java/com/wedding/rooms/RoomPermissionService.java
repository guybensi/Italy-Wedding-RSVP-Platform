package com.wedding.rooms;

import com.wedding.guests.*;

import java.util.Objects;

public class RoomPermissionService {
    private final RoomService rooms;

    public RoomPermissionService(RoomService rooms, InviterRepository inviters) {
        this.rooms = rooms;
    }

    public void managerAddToRoom(GuestType actorRole, InviterId target, RoomId roomId) {
        ensureManager(actorRole);
        rooms.joinExistingRoom(target, roomId);
    }

    public void managerRemoveFromRoom(GuestType actorRole, InviterId target) {
        ensureManager(actorRole);
        rooms.leaveRoom(target);
    }

    public RoomId managerCreateRoom(GuestType actorRole, String roomName, RoomType type) {
        ensureManager(actorRole);
        return rooms.createRoom(roomName, type, java.util.Set.of());
    }

    public void userJoinRoom(GuestType myRole, InviterId me, RoomId roomId) {
        ensureUserOrManager(myRole);
        rooms.joinExistingRoom(me, roomId);
    }

    public RoomId userCreateAndJoinMyRoom(GuestType myRole, InviterId me, String roomName, RoomType type) {
        ensureUserOrManager(myRole);
        return rooms.createAndJoinMyRoom(me, roomName, type);
    }

    public void userLeaveRoom(GuestType myRole, InviterId me) {
        ensureUserOrManager(myRole);
        rooms.leaveRoom(me);
    }

    private void ensureManager(GuestType role) {
        if (!Objects.equals(role, GuestType.MANAGER)) throw new SecurityException("manager role required");
    }
    private void ensureUserOrManager(GuestType role) {
        if (role == null) throw new SecurityException("role required");
    }
}
