The complete, detailed project documentation and architecture overview are available in [DOCUMENTATION.pdf](DOCUMENTATION.pdf).

# Smart home system - quick overview:

Smart home system built on Raspberry Pi 5 with sensor monitoring and remote device control via a web dashboard.

**Authors:** 

Eryk Tamm (Server, Database, Frontend)

Kacper Radomski (Device, Frontend)

---

## Architecture

- **Device** — Raspberry Pi 5 (Python/Flask) — collects sensor data, executes commands
- **Main Server** — Spring Boot + MySQL — auth, data storage, REST API
- **User** — browser-based dashboard

---

## Hardware

| Component | Role | GPIO |
|---|---|---|
| DHT11 | Temperature & humidity | GPIO4 |
| HC-SR04 | Motion detection | GPIO23/24 |
| SG90 servo | Door lock | GPIO18 |
| 5× buttons | PIN keypad | GPIO5,6,13,19,26 |
| Red/Green LED | Lock status | GPIO21/20 |
| Blue LED | Controllable light | GPIO16 |

---

## Tech Stack

- **Device:** Python, Flask, `requests`
- **Server:** Java, Spring Boot (Security, Data JPA, WebFlux)
- **Database:** MySQL (`users`, `measurements`, `states` tables)
- **Frontend:** Vanilla JS, responsive HTML

---

## Features

- Real-time temperature, humidity, motion display
- Remote door lock/unlock; physical PIN keypad with LED feedback
- Door state synced between keypad and web dashboard
- Light brightness control
- Historical sensor data charts
- Role-based access (`USER` / `DEVICE`), BCrypt passwords
