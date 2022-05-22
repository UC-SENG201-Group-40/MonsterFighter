package seng.monsters.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
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
        for (final Monster monster : all) {
            final int prevLevel = monster.getLevel();
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
        for (final Monster monster : all) {
            // Heal by a certain amount
            monster.takeDamage(20);
            monster.healSelf(20);
            assertEquals(monster.maxHp(), monster.getCurrentHp());

            // No healing at max hp
            monster.healSelf(20);
            assertEquals(monster.maxHp(), monster.getCurrentHp());

            // Heal from fainted
            monster.takeDamage(monster.maxHp());
            assertTrue(monster.isFainted());
            monster.healSelf(10);
            assertFalse(monster.isFainted());

            // No change if heal value is negative
            final int prevHp = monster.getCurrentHp();
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
        for (final Monster monster : all) {
            // Damage by a certain amount from max hp
            monster.takeDamage(20);
            assertNotEquals(monster.maxHp(), monster.getCurrentHp());

            // Damage by a certain amount
            final int prevHp = monster.getCurrentHp();
            monster.takeDamage(20);
            assertNotEquals(prevHp, monster.getCurrentHp());

            // Damage by monster's max hp, should not drop below 0
            monster.takeDamage(monster.maxHp());
            assertEquals(0, monster.getCurrentHp());

            // Monster's hp does not drop below 0
            monster.takeDamage(20);
            assertEquals(Math.abs(monster.getCurrentHp()), monster.getCurrentHp());
            assertEquals(0, monster.getCurrentHp());

            // No change if damage value is negative
            monster.healSelf(20);
            final int prevHp2 = monster.getCurrentHp();
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
        for (final Monster monster : all) {
            // Valid monster name change
            final String prevName = monster.getName();
            monster.setName("amongus");
            assertNotEquals(prevName, monster.getName());
            assertEquals("amongus", monster.getName());

            // Invalid monster name change
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
        for (final Monster monster : all) {
            // Set new monster base hp
            final int prevHp = monster.getCurrentHp();
            monster.setBaseHp(10);
            assertNotEquals(prevHp, monster.getCurrentHp());
            assertEquals(monster.maxHp(), monster.getCurrentHp());

            // No change in current hp if new base hp is higher
            final int prevMaxHp = monster.maxHp();
            monster.setBaseHp(2000);
            assertEquals(10, monster.getCurrentHp());
            assertNotEquals(prevMaxHp, monster.maxHp());

            // No change if input is negative
            final int prevHp2 = monster.getCurrentHp();
            final int prevMaxHp2 = monster.maxHp();
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
        for (final Monster monster : all) {
            // Max hp change after a level up
            int maxHp = monster.maxHp();
            monster.levelUp();
            assertNotEquals(maxHp, monster.maxHp());
            assertEquals(Math.round(maxHp * 1.1), monster.maxHp());

            // Base hp change
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
        for (final Monster monster : all) {
            // Monster damage without environment boost
            monster.levelUp();
            final int unboosted = monster.damage(Environment.FIELD);
            assertNotEquals(monster.baseDamage(), unboosted);

            // Monster damage with environment boost
            final int boosted = monster.damage(monster.idealEnvironment());
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
        for (final Monster monster : all) {
            // Monster is at full hp (not fainted)
            assertFalse(monster.isFainted());

            // Monster has taken damage (still not fainted)
            monster.takeDamage(monster.maxHp() / 2);
            assertFalse(monster.isFainted());

            // Monster has taken more than or equal to max hp in damage (i.e. fainted)
            monster.takeDamage(monster.maxHp());
            assertTrue(monster.isFainted());
        }
    }

    /**
     * Monster <code>sellPrice()</code> should:
     * <ul>
     * <li> be 1/2 of the buy price </li>
     * </ul>
     */
    @Test
    void sellPrice() {
        for (final Monster monster : all) {
            if (monster instanceof Monster.Tree) {
                assertEquals(monster.buyPrice(), monster.sellPrice());
            } else {
                assertEquals(monster.buyPrice() / 2, monster.sellPrice());
            }
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
        final Monster.Shark monster = new Monster.Shark("FishAndChips", 1);
        assertNotEquals(new Monster.Shark(1), monster);

        final Monster pointer = monster;
        assertEquals(monster, pointer);

        pointer.levelUp();

        assertEquals(monster, pointer);
    }

    /**
     * Monster's random chances method such as:
     * <ul>
     * <li><code>shouldLeave</code> should be unlikely</li>
     * <li><code>shouldLevelUp</code> should happen once</li>
     * </ul>
     */
    @Test
    void testRandomness() {
        final boolean shouldLeaveTest = all.stream()
            .mapToInt(monster -> {
                int res = 0;
                for (int i = 0; i < 100; i++) {
                    if (monster.shouldLeave())
                        res++;
                }
                return res;
            })
            .allMatch(freq -> 100 >= 2 * freq);
        assertTrue(shouldLeaveTest);

        for (final Monster monster: all) {
            boolean hasDone = false;
            for (int i = 0; i < 200; i++) {
                if (monster.shouldLevelUp()) {
                    hasDone = true;
                    break;
                }
            }
            assertTrue(hasDone);
        }
    }

    /**
     * Monster's <code>uniqueName</code> should:
     * <ul>
     * <li>Return a combination of name and id</li>
     * <li>Be unique in respect to other monster even the same type</li>
     * </ul>
     */
    @Test
    void uniqueName() {
        final String sameName = "GoodName";
        all.forEach(monster -> monster.setName(sameName));

        final HashSet<String> uniqueNames = new HashSet<>();
        for (final Monster monster: all) {
            final String name = monster.uniqueName();
            assertFalse(uniqueNames.contains(name));
            uniqueNames.add(name);
        }

        final HashSet<String> descriptions = new HashSet<>();
        for (final Monster monster: all) {
            final String desc = monster.description();
            assertFalse(descriptions.contains(desc));
            descriptions.add(desc);
        }

        assertNotEquals(
            new Monster.Quacker(sameName, 1).uniqueName(),
            new Monster.Quacker(sameName, 1).uniqueName()
        );
    }
}