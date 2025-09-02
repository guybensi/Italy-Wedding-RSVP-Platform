package com.wedding.rooms;

public enum RoomType {
    DOUBLE(2),
    TRIPLE(3);

    private final int capacity;
    RoomType(int capacity){ this.capacity = capacity; }
    public int capacity(){ return capacity; }
}
