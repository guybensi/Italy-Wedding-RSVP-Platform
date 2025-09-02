package com.wedding.guests;

import java.util.*;

public interface InviterRepository {
    Inviter save(Inviter inviter);
    Optional<Inviter> findById(InviterId id);
    List<Inviter> findAll();
    void delete(InviterId id);
    Optional<Inviter> findByFullName(String fullName);
    Inviter saveEnforcingUniqueName(Inviter inviter); // יזרוק אם כבר קיים שם (case-insensitive) למזהה אחר
}
