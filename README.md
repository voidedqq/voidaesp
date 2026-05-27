<div align="center">

# VoidAESP

### advanced anti-esp plugin for Paper servers

blocks players from seeing things they shouldnt be able to see

built for survival servers that are tired of stashfinders, storage esp, mob esp, freecam abuse and other cringe client shit

---

![Platform](https://img.shields.io/badge/platform-paper-blue?style=for-the-badge)
![Version](https://img.shields.io/badge/minecraft-1.21.x-green?style=for-the-badge)
![Status](https://img.shields.io/badge/status-active-purple?style=for-the-badge)

</div>

---

# what is this

VoidAESP (Voided's Anti-ESP) is a server-side anti esp plugin made for Paper and its forks.

Instead of letting the client see everything around them, the plugin hides entities and storage containers that the player realistically should not be able to see.

if a player cant see something legitimately, the server wont send it to them.

simple as that.

---

# features

- 👁️ anti player esp
- 📦 anti storage esp
- 🧱 anti stash finder
- 🐷 anti mob esp
- 🌀 anti freecam support
- ⚡ optimized visibility checks
- 🔧 configurable
- 🖥️ fully server-side
- 🚫 no mods required

---

# supported software

| software | support |
|---|---|
| Paper | ✅ |
| Pufferfish | ✅ |
| Purpur | probably |
| Spigot | not recommended |

---

# why i made this

because cheating on survival servers got insanely boring

people using esp clients to find bases through walls and thousands of blocks underground completely ruins progression and kills the fun for everyone else

so i made something to fight back against it

---

# performance

VoidAESP was made with performance in mind.

the plugin focuses on:
- low overhead
- smart visibility checks
- minimal unnecessary processing
- not turning your server into a slideshow

---

# installation

```txt
1. download the plugin
2. put it in /plugins
3. restart server
4. configure if needed
5. done
```

---

# config

example config options:

```yaml
anti-storage-esp: true
anti-mob-esp: true
anti-freecam: true
check-through-blocks: true
entity-update-rate: 2
```

---

# roadmap

- better anti freecam checks
- packet optimizations
- chunk-based visibility caching
- more bypass detection
- more config options
- better compatibility

---

# disclaimer

this plugin is not magic

some cheat clients will always try to bypass visibility systems in weird ways, but this massively reduces what ESP clients can actually abuse

---

# credits

inspired by the idea that clients shouldnt know everything happening around them

---

<div align="center">

# license

Copyright (c) 2026 voided

All rights reserved.

You may NOT:
- redistribute
- modify
- resell
- repost
- claim this project as your own

without explicit permission from the author.

</div>
