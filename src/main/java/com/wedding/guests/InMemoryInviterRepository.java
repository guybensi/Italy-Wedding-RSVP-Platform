package com.wedding.guests;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryInviterRepository implements InviterRepository {
    private final Map<UUID, Inviter> storage = new ConcurrentHashMap<>();
    // אינדקס שמות (lowercase) -> inviterId
    private final Map<String, UUID> nameIndex = new ConcurrentHashMap<>();

    @Override
    public Inviter save(Inviter inviter) {
        if (inviter.getId() == null) {
            inviter.setId(InviterId.newId());
        }
        if (inviter.getId() == null) {
            throw new IllegalStateException("Inviter must have id");
        }

        // עדכון אינדקס שם אם יש שם
        if (inviter.getFullName() != null && !inviter.getFullName().isBlank()) {
            nameIndex.put(inviter.getFullName().trim().toLowerCase(), inviter.getId().value());
        }

        storage.put(inviter.getId().value(), inviter);
        return inviter;
    }

    @Override
    public Optional<Inviter> findById(InviterId id) {
        return Optional.ofNullable(storage.get(id.value()));
    }

    @Override
    public List<Inviter> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(InviterId id) {
        Inviter removed = storage.remove(id.value());
        if (removed != null && removed.getFullName() != null) {
            nameIndex.remove(removed.getFullName().trim().toLowerCase());
        }
    }

    // NEW:
    @Override
    public Optional<Inviter> findByFullName(String fullName) {
        if (fullName == null) return Optional.empty();
        UUID uid = nameIndex.get(fullName.trim().toLowerCase());
        return uid == null ? Optional.empty()
                           : storage.values().stream().filter(i -> i.getId() != null && i.getId().value().equals(uid)).findFirst();
    }

    @Override
    public Inviter saveEnforcingUniqueName(Inviter inviter) {
        String key = inviter.getFullName() == null ? "" : inviter.getFullName().trim().toLowerCase();
        Optional<Inviter> existing = findByFullName(inviter.getFullName());
        if (existing.isPresent()) {
            // אם קיים כבר רשומה עם שם זהה ושונה ב-id → זרוק
            if (inviter.getId() == null || !existing.get().getId().equals(inviter.getId())) {
                throw new IllegalStateException("Inviter with same fullName already exists: " + inviter.getFullName());
            }
        }
        return save(inviter);
    }

    // אופציונלי: יוצר מהר בשם בלבד
    public Inviter create(String fullName) {
        Inviter inv = new Inviter();
        inv.setFullName(fullName);
        return save(inv);
    }
}
