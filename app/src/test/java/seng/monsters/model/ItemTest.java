package seng.monsters.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
     * <ul>
     * <li>Potion should add 50 HP</li>
     * <li>Potion should not heal if monster is fainted, and throw an Exception</li>
     * <li>Potion should not heal if monster has full health, and throw an Exception</li>
     * <li>Revive should set the fainted monster health to 1/4 of its health</li>
     * <li>Revive should not heal if monster is not fainted, and throw an Exception</li>
     * <li>RareCandy should increase the level of monster by 1 only 1</li>
     * <li>RareCandy should never throw an Exception</li>
     * <li>FullRestore should heal monster to full health </li>
     * <li>FullRestore should not heal if monster's health is full</li>
     * </ul>
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
        final int prevLevel = labMonster.getLevel();
        new Item.RareCandy().applyTo(labMonster);
        assertEquals(prevLevel + 1, labMonster.getLevel());

        // FullRestore should heal monster to full health
        labMonster.takeDamage(labMonster.maxHp());
        assertTrue(labMonster.isFainted());
        new Item.FullRestore().applyTo(labMonster);
        assertEquals(labMonster.maxHp(), labMonster.getCurrentHp());

        labMonster.takeDamage(labMonster.maxHp() / 2);
        assertFalse(labMonster.isFainted());
        new Item.FullRestore().applyTo(labMonster);
        assertEquals(labMonster.maxHp(), labMonster.getCurrentHp());

        // FullRestore should not heal if monster's health is full
        assertThrows(Item.NoEffectException.class, () -> new Item.FullRestore().applyTo(labMonster));
    }

    /**
     * Check if all types item should:
     * <ul>
     * <li>have their sellPrice be 1/2 buyPrice </li>
     * </ul>
     */
    @Test
    void sellPrice() {
        final boolean res = Item.all()
            .stream()
            .map(item -> item.buyPrice() / 2 == item.sellPrice())
            .reduce(true, (acc, bool) -> acc && bool);

        assertTrue(res);
    }

    /**
     * Check if two instance of the same type
     */
    @Test
    void testEquals() {
        assertEquals(new Item.Potion(), new Item.Potion());
        assertEquals(new Item.Revive(), new Item.Revive());
        assertEquals(new Item.RareCandy(), new Item.RareCandy());
        assertEquals(new Item.FullRestore(), new Item.FullRestore());
        assertNotEquals(new Item.Potion(), new Item.Revive());
        assertNotEquals(new Item.Revive(), new Item.RareCandy());
        assertNotEquals(new Item.RareCandy(), new Item.Potion());
        assertNotEquals(new Item.FullRestore(), new Item.Revive());
        assertNotEquals(new Item.FullRestore(), new Item.RareCandy());
        assertNotEquals(new Item.FullRestore(), new Item.Potion());
    }

    /**
     * Check if all items has their hashcode from the hash of their name
     */
    @Test
    void testHashCode() {
        final Item potion = new Item.Potion();
        assertEquals(Objects.hash(potion.getName()), potion.hashCode());

        final Item revive = new Item.Revive();
        assertEquals(Objects.hash(revive.getName()), revive.hashCode());

        final Item rareCandy = new Item.RareCandy();
        assertEquals(Objects.hash(rareCandy.getName()), rareCandy.hashCode());

        final Item fullRestore = new Item.FullRestore();
        assertEquals(Objects.hash(fullRestore.getName()), fullRestore.hashCode());
    }
}