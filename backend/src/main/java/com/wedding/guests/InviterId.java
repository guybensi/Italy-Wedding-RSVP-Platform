package com.wedding.guests;

import java.util.UUID;

public record InviterId(UUID value) {
    public static InviterId newId() { 
        return new InviterId(UUID.randomUUID()); 
    }
}
