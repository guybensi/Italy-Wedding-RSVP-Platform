# Wedding RSVP Platform

![Java CI](https://github.com/<USER>/<REPO>/actions/workflows/maven.yml/badge.svg)

A Java-based RSVP and event management system for weddings.  
Guests can register flights, choose songs, manage dietary preferences, and join rooms.  
The manager has elevated permissions (add/remove guests, override room assignments, etc).

---

## Features (current)
- Guests:
  - Register flight details ✈️
  - Choose favorite song 🎵
  - Select dietary preferences 🍽️
  - Join or create a room (double or triple) 🛏️
- Manager:
  - Full control over rooms and guests  
  - Can add/remove/update any guest  

---

## Tech Stack
- **Java 17**
- **Maven** (build & dependency management)
- **JUnit 5** (tests)

---

## How to Run / Compile

Make sure you have:
- **Java 17+** installed (`java -version`)
- **Maven 3.9+** installed (`mvn -v`)

Clone the repository:
```bash
git clone https://github.com/<USER>/<REPO>.git
cd <REPO>
