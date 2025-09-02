package com.wedding.rooms;

import java.util.UUID;

public record RoomId(UUID value) {
    public static RoomId newId() { return new RoomId(UUID.randomUUID()); }
}
