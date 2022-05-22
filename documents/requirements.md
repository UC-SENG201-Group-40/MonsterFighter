# Requirements

### Monsters

- [x] 6+ different types
- [x] Variying stats (max hp, damage, heal amount, curr hp) maybe using level to scale difficulty and/or using
  randomised ranges.
- [x] [Purchasable](#purchasable) (price can be computed depending on properties)
- [x] Heals itself [during night](#night-random-events)
- [x] Environment properties affect the monsters properties during [battle](#battle)
- [x] A chance to level up (higher if kills an enemy monsters that day) [during night](#night-random-events)
- [x] A chance to leave (higher if fainted) [during night](#night-random-events)

### Item

- [x] 4+ different types
- [x] Affect stats / properties of [monsters](#monsters) when consumed
- [x] [Purchasable](#purchasable) (price can be set on types)
- [x] Removed when used

### Purchasable

- [x] Must include a price
- [x] Can be bought from shop
- [x] Can be sold back
- [x] Gold / points is earned through [battle](#battle)

### Battle

- [x] A multiple [monsters](#monsters) battle (team)
- [x] Win condition is all [monsters](#monsters) in a team is wiped
- [x] There are environment properties that affect the [monsters](#monsters) effectiveness
- [ ] May or may not fight another player
- [x] Selection of battle each day
- [x] Battle victory gives gold

### Night random events

- [x] [Monsters](#monsters) leveling up, leaving, or joining
- [x] [Monsters](#monsters) joining should be rare

### Environment properties

- [x] Day is tracked, day should affect the difficulty (I was thinking in a similar sense to Minecraft where
  the [monsters](#monsters) got stronger after some in game days)
- [x] Environment or other condition such as landscape that change the balance of [monsters](#monsters)
- [x] The maximum amount of days is selected before the game started
- [x] The difficulty of the game is also selected before the game started

## General software requirements

- [x] It's probably best to separate these classes from the UI like have the game works completely UI-less first.

---

## Setting Up the Game

1. [x] Choose a player name. The length must be between 3 and 15 characters and must not include numbers or special
   characters.
2. [x] Select how many days they would like the game to last (between 5 and 15 days).
3. [x] Choose a starting monster for your team:
    1. [x] Each [monster](#monsters) should have different characteristics. Including max health, damage, heal amount,
       current
       health.
    2. [x] The player shall have 3 to 5 [monsters](#monsters) to choose from.
    3. [x] The player shall be able to name the monster (or have a default name)
4. [x] Choose a difficulty
    1. [x] There must be at least 2 options.
    2. [x] This should scale how difficult the game is in a noticeable way.
    3. [x] You may want the harder difficulties to give a higher score for the same action.

---

## Playing the Main Game

1. [x] View the amount of gold you have, the current day, and the number of days remaining.
2. [x] View the properties of your team.
    - [x] The name of each monster.
    - [x] The properties of each monster (importantly attack, and current health though all should be viewable).
    - [x] It should be clear to the player what order their monsters are in (if this matters for your battling mechanic)
      .
3. [x] View the player inventory, and the current items they have. Each item shall:
    - [x] Show the effects of the [item](#item).
    - [x] Allow for the player to use the item on a [monster](#monsters).
4. [x] View the possible battles
    - [x] The player should be able to see a small number (3-5) of optional [battles](#battle) they can take on.
        - [x] The [battles](#battle) should be generated somewhat randomly, but largely influenced by the current day,
          and
          optionally the difficulty setting
        - [x] The gold and points gained for winning a [battle](#battle) may scale with
          the difficulty (e.g. less gold on hard, but more points given)
    - [x] The player should be able to choose a [battle](#battle) to fight.
        - [x] A player can battle each [battle](#battle) once.
        - [x] If all of a player’s monsters have fainted they can no longer battle.
        - [x] If all of a player’s monsters faint during a [battle](#battle) they lose the [battle](#battle) and do not
          receive any gold or
          points
        - [x] If a player wins a [battle](#battle) (by fainting all opposing monsters) they are rewarded with gold and
          points
5. [x] [Battling](#battle)
    - [x] Within a [battle](#battle) monsters should fight in some meaningful way
    - [x] Once a battle has concluded the outcome should be clear, and the updated status of the team should be shown to
      the player.
    - [x] You may optionally show each step in a battle, with the current leading monsters and their attack and health.
6. [x] Visit the shop and:
    - [x] While at the shop, the player must be able to see their current gold.
    - [x] [Monsters](#monsters) and [items](#items) can be sold back to the shop.
    - [x] View [monsters](#monsters) that are for sale including their price and their properties
        - [x] There should be 3 to 5 [monsters](#monsters) to choose from
        - [x] [Monsters](#monsters) can only be bought if there is space for them on the
          team.
        - [x] The type of [monsters](#monsters) sold may depend on other factors
    - [x] View items that are for sale including their price and properties
        - [x] There should be at least three [items](#items) in the shop
        - [x] When an [item](#item) is bought, it should go to the player’s inventory
        - [x] Items should enhance [monster](#monsters) stats
7. [x] Go to sleep (move to next day)
    - [x] All items in the shop are updated (possibly randomly)
    - [x] All battles are updated (following 4)
    - [x] Each [monster](#monsters) heals for their heal amount not exceeding their max health
8. [x] A [monster](#monsters) levels up overnight

    - [x] There should be a small chance a [monster](#monsters) levels up overnight

9. [x] A [monster](#monsters) leaves overnight
    - [x] The chance should increase if the [monster](#monsters) fainted the previous day(s)
    - [x] The chance should be quite low
10. [x] A random new [monster](#monsters) joins overnight
    - [ ] The chance should increase depending on how many free slots the player has in their team
    - [x] The chance should be quite low

---

## Finishing the game

The game ends when one of the following occurs:

1. [x] All days have passed
2. [x] The player has no monsters and not enough gold to buy another

---

### UML Diagrams

1. [x] Use-case UML diagrams
2. [x] Class (`model`) UML diagrams

### CLI

- [x] Working CLI applications

### GUI

- [x] Working GUI applications

### Others

1. [x] Javadocs for all code
2. [x] Unit test for all code

### Report

- [x] Write a short two page report describing your work.
- [x] Include on the first page:
    1. [x] Student names and ID numbers.
    2. [x] The structure of your application and any design decisions you had to make. We are particularity interested
       in communication between classes and how interfaces and/or inheritance were used. You might want to reference
       your UML class diagram.
    3. [x] An explanation of unit test coverage, and why you managed to get a high/low percentage coverage.
- [x] Include on the second page:
    1. [x] Your thoughts and feedback on the project.
    2. [x] A brief retrospective of what went well, what did not go well, and what improvements you could make for your
       next project.
    3. [x] The effort spent (in hours) in the project per student.
    4. [x] A statement of agreed % contribution from both partners.

### Subsmission

Please create a ZIP archive with the following:

- [x] Your source code (including unit tests). We want your exported project as well so that we can easily import it
  into Eclipse.
- [x] Javadoc (already compiled and ready to view).
- [x] UML use case and class diagrams as a PDF or PNG (do not submit Umbrello or Dia files; these will not be marked).
- [x] Your report as a PDF file (do not submit MS Word or LibreOffice docu- ments; these will not be marked).
- [x] A README.txt file describing how to build your source code, import your project into Eclipse and run your program.
- [x] A packaged version of your program as a JAR. We must be able to run your program in one of the lab machines along
  the lines of: java -jar usercode1 usercode2 MonsterFighter.jar.
