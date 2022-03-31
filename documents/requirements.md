# Requirements

### Monsters

- 6+ different types
- Variying stats (max hp, damage, heal amount, curr hp) maybe using level to scale difficulty and/or using randomised ranges.
- [Purchasable](#purchasable) (price can be computed depending on properties)
- Heals itself [during night](#night-random-events)
- Environment properties affect the monsters properties during [battle](#battle)
- A chance to level up (higher if kills an enemy monsters that day) [during night](#night-random-events)
- A chance to leave (higher if fainted) [during night](#night-random-events)

### Item

- 4+ different types
- Affect stats / properties of [monsters](#monsters) when consumed
- [Purchasable](#purchasable) (price can be set on types)
- Removed when used

### Purchasable

- Must include a price
- Can be bought from shop
- Can be sold back
- Gold / points is earned through [battle](#battle)

### Battle

- A multiple [monsters](#monsters) battle (team)
- Win condition is all [monsters](#monsters) in a team is wiped
- There are environment properties that affect the [monsters](#monsters) effectiveness
- May or may not fight another player
- Selection of battle each day
- Battle victory gives gold

### Night random events

- [Monsters](#monsters) leveling up, leaving, or joining
- [Monsters](#monsters) joining should be rare

### Environment properties

- Day is tracked, day should affect the difficulty (I was thinking in a similar sense to Minecraft where the [monsters](#monsters) got stronger after some in game days)
- Weather or other condition such as landscape that change the balance of [monsters](#monsters)
- The maximum amount of days is selected before the game started
- The difficulty of the game is also selected before the game started

## General software requirements

It's probably best to seperate these classes from the UI like have the game works completely UI-less first.
