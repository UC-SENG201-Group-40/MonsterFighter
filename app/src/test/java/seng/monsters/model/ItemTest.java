package seng.monsters.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    private Monster labMonster;

    @BeforeEach
    void setUp() {
        labMonster = new Monster.Quacker(1);
    }

    /**
     * Given Item when applied to Monster:
     * - Potion should add 50 HP
     * - Potion should not heal if monster is fainted, and throw an Exception
     * - Potion should not heal if monster has full health, and throw an Exception
     *
     * - Revive should set the fainted monster health to 1/4 of its health
     * - Revive should not heal if monster is not fainted, and throw an Exception
     *
     * - RareCandy should increase the level of monster by 1 only 1
     * - RareCandy should never throw an Exception
     */
    @Test
    void applyTo() {
        // Potion should add 50 HP
        labMonster.takeDamage(labMonster.maxHp() - 10);
        new Item.Potion().applyTo(labMonster);
        assertFalse(labMonster.isFainted());
        assertEquals(60, labMonster.getCurrentHp());

        // Potion should not heal if monster is fainted, and throw an Exception
        labMonster.takeDamage(labMonster.maxHp());
        assertTrue(labMonster.isFainted());
        assertThrows(Item.NoEffectException.class, () -> new Item.Potion().applyTo(labMonster));
        assertTrue(labMonster.isFainted());

        // Potion should not heal if monster has full health, and throw an Exception
        labMonster.healSelf(labMonster.maxHp());
        assertThrows(Item.NoEffectException.class, () -> new Item.Potion().applyTo(labMonster));

        // Revive should set the fainted monster health to 1/4 of its health
        labMonster.takeDamage(labMonster.maxHp());
        assertTrue(labMonster.isFainted());
        new Item.Revive().applyTo(labMonster);
        assertFalse(labMonster.isFainted());
        assertEquals(labMonster.maxHp() / 4, labMonster.getCurrentHp());

        // Revive should not heal if monster is not fainted, and throw an Exception
        assertFalse(labMonster.isFainted());
        assertThrows(Item.NoEffectException.class, () -> new Item.Revive().applyTo(labMonster));

        // RareCandy should increase the level of monster by 1 only 1
        // RareCandy should never throw an Exception
        final var prevLevel = labMonster.getLevel();
        new Item.RareCandy().applyTo(labMonster);
        assertEquals(prevLevel + 1, labMonster.getLevel());
    }

    /**
     * Check if all types item should:
     * - have their sellPrice be 1/2 buyPrice
     */
    @Test
    void sellPrice() {
        final var res = Stream.of(new Item.Potion(), new Item.Revive(), new Item.RareCandy())
            .map(item -> item.buyPrice() / 2 == item.sellPrice())
            .reduce(true, (acc, bool) -> acc && bool);

        assertTrue(res);
    }

    /**
     * Check if two instance of the same type
     */
    @Test
    void testEquals() {
        assertTrue(new Item.Potion().equals(new Item.Potion()));
        assertTrue(new Item.Revive().equals(new Item.Revive()));
        assertTrue(new Item.RareCandy().equals(new Item.RareCandy()));
        assertFalse(new Item.Potion().equals(new Item.Revive()));
        assertFalse(new Item.Revive().equals(new Item.RareCandy()));
        assertFalse(new Item.RareCandy().equals(new Item.Potion()));
    }

    /**
     * Check if all items has their hashcode from the hash of their name
     */
    @Test
    void testHashCode() {
        final var potion = new Item.Potion();
        assertEquals(Objects.hash(potion.getName()), potion.hashCode());

        final var revive = new Item.Revive();
        assertEquals(Objects.hash(revive.getName()), revive.hashCode());

        final var rareCandy = new Item.RareCandy();
        assertEquals(Objects.hash(rareCandy.getName()), rareCandy.hashCode());
    }
}