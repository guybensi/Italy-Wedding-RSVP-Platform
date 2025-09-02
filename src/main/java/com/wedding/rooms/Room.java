package com.wedding.rooms;

import java.util.HashSet;
import java.util.Set;

public class Room {
    private final RoomId id;
    private String name;
    private RoomType type;    // DOUBLE/TRIPLE בלבד
    private final Set<String> tags = new HashSet<>();

    public Room(RoomId id, String name, RoomType type, Set<String> tags) {
        this.id = id;
        this.name = name;
        this.type = type;
        if (tags != null) this.tags.addAll(tags);
    }

    public RoomId id(){ return id; }
    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }
    public RoomType getType(){ return type; }
    public void setType(RoomType type){ this.type = type; }
    public Set<String> getTags(){ return tags; }

    @Override
    public String toString() {
        return "Room{" + id().value() + ", name=" + getName() + ", type=" + getType() + "}";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room r)) return false;
        return java.util.Objects.equals(id(), r.id());
    }
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id());
    }

}
