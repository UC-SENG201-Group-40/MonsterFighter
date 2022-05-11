package seng.monsters.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MonsterTest {
    private List<Monster> all;

    @BeforeEach
    void setUp() {
        all = Monster.all(1);
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * Monster <code>levelUp()</code> should:
     * <ul>
     * <li> Increase a monster level by 1 and only 1 for all monster </li>
     * </ul>
     */
    @Test
    void levelUp() {
        for (final var monster : all) {
            final var prevLevel = monster.getLevel();
            monster.levelUp();
            assertEquals(prevLevel + 1, monster.getLevel());
        }
    }

    /**
     * Monster <code>healSelf()</code> should:
     * <ul>
     * <li> Heal monster health for a certain amount </li>
     * <li> Heal monster health up to the max health even if the amount specified is higher </li>
     * <li> Should not heal if the input is invalid (negative) </li>
     * </ul>
     */
    @Test
    void healSelf() {
        for (final var monster : all) {
            monster.takeDamage(20);
            monster.healSelf(20);
            assertEquals(monster.maxHp(), monster.getCurrentHp());

            monster.healSelf(20);
            assertEquals(monster.maxHp(), monster.getCurrentHp());

            monster.takeDamage(monster.maxHp());
            assertTrue(monster.isFainted());
            monster.healSelf(10);
            assertFalse(monster.isFainted());

            final var prevHp = monster.getCurrentHp();
            monster.healSelf(-10);
            assertEquals(prevHp, monster.getCurrentHp());
        }
    }

    /**
     * Monster <code>takeDamage()</code> should:
     * <ul>
     * <li> Deal exact damage to the monster </li>
     * <li> Not deal anymore if health is already 0 </li>
     * <li> Not deal if the amount is invalid (negative) </li>
     * </ul>
     */
    @Test
    void takeDamage() {
        for (final var monster : all) {
            monster.takeDamage(20);
            assertNotEquals(monster.maxHp(), monster.getCurrentHp());

            final var prevHp = monster.getCurrentHp();
            monster.takeDamage(20);
            assertNotEquals(prevHp, monster.getCurrentHp());

            monster.takeDamage(monster.maxHp());
            assertEquals(0, monster.getCurrentHp());

            monster.takeDamage(20);
            assertEquals(Math.abs(monster.getCurrentHp()), monster.getCurrentHp());
            assertEquals(0, monster.getCurrentHp());

            monster.healSelf(20);
            final var prevHp2 = monster.getCurrentHp();
            monster.takeDamage(-100);
            assertEquals(prevHp2, monster.getCurrentHp());
        }
    }

    /**
     * Monster <code>setName()</code> should:
     * <ul>
     * <li> Change their name </li>
     * </ul>
     */
    @Test
    void setName() {
        for (final var monster : all) {
            final var prevName = monster.getName();
            monster.setName("amongus");
            assertNotEquals(prevName, monster.getName());
            assertEquals("amongus", monster.getName());

            assertThrows(IllegalArgumentException.class, () -> monster.setName("12312313"));
        }
    }

    /**
     * Monster <code>setBaseHp()</code> should:
     * <ul>
     * <li> Change the base hp into a new amount </li>
     * <li> Change the current hp to max hp if it is more than the new max hp </li>
     * <li> Not change the current hp if it is less than the new max hp </li>
     * <li> Not change any properties if the input is invalid (negative) </li>
     * </ul>
     */
    @Test
    void setBaseHp() {
        for (final var monster : all) {
            final var prevHp = monster.getCurrentHp();
            monster.setBaseHp(10);
            assertNotEquals(prevHp, monster.getCurrentHp());
            assertEquals(monster.maxHp(), monster.getCurrentHp());

            final var prevMaxHp = monster.maxHp();
            monster.setBaseHp(2000);
            assertEquals(10, monster.getCurrentHp());
            assertNotEquals(prevMaxHp, monster.maxHp());

            final var prevHp2 = monster.getCurrentHp();
            final var prevMaxHp2 = monster.maxHp();
            monster.setBaseHp(-100);
            assertEquals(prevHp2, monster.getCurrentHp());
            assertEquals(prevMaxHp2, monster.maxHp());
        }
    }

    /**
     * Monster <code>maxHp()</code> should:
     * <ul>
     * <li> Give the max hp given the level and base hp </li>
     * <li> Automatically scale up and down when levelling up or base hp was changed </li>
     * </ul>
     */
    @Test
    void maxHp() {
        for (final var monster : all) {
            var maxHp = monster.maxHp();
            monster.levelUp();
            assertNotEquals(maxHp, monster.maxHp());
            assertEquals(Math.round(maxHp * 1.1), monster.maxHp());

            maxHp = monster.maxHp();
            monster.setBaseHp(10);
            assertNotEquals(maxHp, monster.maxHp());
        }
    }

    /**
     * Monster <code>damage()</code> should:
     * <ul>
     * <li> Return the maximum damage output given the current environment and monster level </li>
     * <li> Return 1.5x damage if the environment is ideal to the monster </li>
     * </ul>
     */
    @Test
    void damage() {
        for (final var monster : all) {
            monster.levelUp();
            final var unboosted = monster.damage(Environment.FIELD);
            assertNotEquals(monster.baseDamage(), unboosted);

            final var boosted = monster.damage(monster.idealEnvironment());
            assertNotEquals(unboosted, boosted);
        }
    }

    /**
     * Monster <code>isFainted()</code> should:
     * <ul>
     * <li> Return always accurate boolean about the state of the monster </li>
     * </ul>
     */
    @Test
    void isFainted() {
        for (final var monster : all) {
            assertFalse(monster.isFainted());

            monster.takeDamage(monster.maxHp() / 2);
            assertFalse(monster.isFainted());

            monster.takeDamage(monster.maxHp());
            assertTrue(monster.isFainted());
        }
    }

    /**
     * Monster <code>sellPrice()</code> should:
     * <ul>
     * <li> be 1/2 of te buy price </li>
     * </ul>
     */
    @Test
    void sellPrice() {
        for (final var monster : all) {
            assertEquals(monster.buyPrice() / 2, monster.sellPrice());
        }
    }

    /**
     * Monster <code>equals()</code> should:
     * <ul>
     * <li> Should only match two of the same instance (using the id)a </li>
     * </ul>
     */
    @Test
    void testEquals() {
        final var monster = new Monster.Eel(1);
        assertNotEquals(new Monster.Eel(1), monster);

        final var pointer = (Monster) monster;
        assertEquals(monster, pointer);

        pointer.levelUp();

        assertEquals(monster, pointer);
    }
}