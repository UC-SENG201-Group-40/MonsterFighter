package seng.monsters.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {
    private GameManager manager;
    private Monster perfectMon;
    private Monster ejectMon;

    @BeforeEach
    void setUp() {
        manager = new GameManager(0, 1, 5, 1, "Tester");
        perfectMon = new Monster("Perfect", 100, 10) {
            @Override
            public int baseDamage() {
                return 0;
            }

            @Override
            public int speed() {
                return 0;
            }

            @Override
            public int healRate() {
                return 1000000;
            }

            @Override
            public Environment idealEnvironment() {
                return Environment.FIELD;
            }

            @Override
            public boolean shouldLevelUp() {
                return true;
            }

            @Override
            public boolean shouldLeave() {
                return false;
            }
        };
        ejectMon = new Monster("Eject", 100, 10) {
            @Override
            public int baseDamage() {
                return 0;
            }

            @Override
            public int speed() {
                return 0;
            }

            @Override
            public int healRate() {
                return 0;
            }

            @Override
            public Environment idealEnvironment() {
                return Environment.FIELD;
            }

            @Override
            public boolean shouldLevelUp() {
                return false;
            }

            @Override
            public boolean shouldLeave() {
                return true;
            }
        };
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * BattleManager's <code>getAvailableBattles</code> should:
     * <ul>
     * <li>Return all the available enemies that has not been defeated</li>
     * </ul>
     */
    @Test
    void getAvailableBattles() {
        assertTrue(manager.getAvailableBattles().size() > 0);
        assertTrue(manager.getAvailableBattles().stream().noneMatch(Trainer::isWhitedOut));

        final var amountEnemies = Math.max(1, Math.min(5, 5 * manager.getDifficulty() * manager.getCurrentDay() / manager.getMaxDays()));
        final var amountMonster = Math.max(1, Math.min(4, 4 * manager.getDifficulty() * manager.getCurrentDay() / manager.getMaxDays()));
        assertEquals(amountEnemies, manager.getAvailableBattles().size());
        assertTrue(manager.getAvailableBattles().stream().allMatch(trainer -> trainer.getParty().size() == amountMonster));

        manager.getAvailableBattles()
            .stream()
            .findFirst()
            .ifPresent(trainer -> trainer
                .getParty()
                .forEach(mon -> mon.takeDamage(mon.maxHp()))
            );

        assertNotEquals(amountEnemies, manager.getAvailableBattles().size());
    }

    /**
     * BattleManager's <code>hasNotEnoughMoneyForMonster</code> should:
     * <ul>
     * <li>Return true if the player has no monster and no money </li>
     * <li>Return false if the player has at least 1 active monster even with no money and the monster is fainted </li>
     * <li>Return false if the player has no monster but enough money to buy a new one</li>
     * </ul>
     */
    @Test
    void hasNotEnoughMoneyForMonster() {
        assertTrue(manager.hasNotEnoughMoneyForMonster());

        manager.getTrainer().add(new Monster.Shark(10));

        assertFalse(manager.hasNotEnoughMoneyForMonster());

        manager.getTrainer().getParty().forEach(mon -> mon.takeDamage(mon.maxHp()));

        assertFalse(manager.hasNotEnoughMoneyForMonster());

        manager.getTrainer().remove(0);
        manager.setGold(manager.getShop().getMonsterStock().stream().findFirst().map(Monster::buyPrice).orElse(1000));

        assertFalse(manager.hasNotEnoughMoneyForMonster());
    }

    /**
     * BattleManager's <code>hasNoPossibilityForRevive</code> should:
     * <ul>
     * <li>Return true if the player has no monster and no money </li>
     * <li>Return false if the player has at least 1 active monster even with no money </li>
     * <li>Return false if the player has all fainted monster but enough money to buy a revive</li>
     * </ul>
     */
    @Test
    void hasNoPossibilityForRevive() {
        assertTrue(manager.hasNoPossibilityForRevive());

        manager.getTrainer().add(new Monster.Shark(10));

        assertFalse(manager.hasNoPossibilityForRevive());

        manager.getTrainer().getParty().forEach(mon -> mon.takeDamage(mon.maxHp()));
        manager.setGold(new Item.Revive().buyPrice());

        assertFalse(manager.hasNoPossibilityForRevive());
    }

    /**
     * BattleManager's <code>hasNotBattleOnce</code> should:
     * <ul>
     * <li>Return true if all enemy is available enemy trainers has full health monster </li>
     * <li>Return false if at least 1 enemy has monster who has been attacked </li>
     * </ul>
     */
    @Test
    void hasNotBattleOnce() {
        assertTrue(manager.hasNotBattleOnce());

        manager.getAvailableBattles()
            .stream()
            .findFirst()
            .map(Trainer::getParty)
            .flatMap(party -> party.stream().findFirst())
            .ifPresent(mon -> mon.takeDamage(1));

        assertFalse(manager.hasNotBattleOnce());
    }

    /**
     * BattleManager's <code>nextDay</code> should:
     * <ul>
     * <li> Increment the current day count </li>
     * <li> Trigger all night events if the day has not ended </li>
     * <li> Return true if has no monster and no money </li>
     * <li> Return true if the day reached the max limit, false otherwise</li>
     * </ul>
     */
    @Test
    void nextDay() {
        manager.setGold(99999);
        manager.getTrainer().add(perfectMon);
        manager.getTrainer().add(ejectMon);
        perfectMon.takeDamage(perfectMon.maxHp() / 2);
        manager.getAvailableBattles().forEach(trainer -> trainer.getParty().forEach(mon -> mon.takeDamage(mon.maxHp())));
        manager.setCurrentDay(1);
        manager.setMaxDays(3);

        final var currentHp = perfectMon.getCurrentHp();
        final var currentLevel = perfectMon.getLevel();
        final var currentDay = manager.getCurrentDay();
        final var itemStock = manager.getShop()
            .getItemStock()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        final var monsterStock = manager.getShop().getMonsterStock();
        final var availableBattles = manager.getAvailableBattles().size();

        assertFalse(manager.nextDay());

        // Increment day
        assertNotEquals(currentDay, manager.getCurrentDay());
        assertEquals(currentDay + 1, manager.getCurrentDay());

        // Trigger heal night event
        assertNotEquals(currentHp, perfectMon.getCurrentHp());
        assertEquals(perfectMon.maxHp(), perfectMon.getCurrentHp());

        // Trigger level up night event
        assertNotEquals(currentLevel, perfectMon.getLevel());
        assertEquals(currentLevel + 1, perfectMon.getLevel());

        // Trigger leave night event
        assertFalse(manager.getTrainer().getParty().contains(ejectMon));

        // New item stock
        assertFalse(manager.getShop()
            .getItemStock()
            .stream()
            .allMatch(entry -> Objects.equals(entry.getValue(), itemStock.getOrDefault(entry.getKey(), 0)))
        );

        // New monster stock
        assertFalse(manager.getShop()
            .getMonsterStock()
            .stream()
            .anyMatch(monsterStock::contains)
        );

        // Refreshed battles
        assertNotEquals(availableBattles, manager.getAvailableBattles().size());

        assertTrue(manager.nextDay());
    }

    /**
     * BattleManager's <code>refreshCurrentDay</code> should:
     * <ul>
     * <li> Restock shop</li>
     * <li> Refresh available battles</li>
     * </ul>
     */
    @Test
    void refreshCurrentDay() {
        manager.getAvailableBattles().forEach(trainer -> trainer.getParty().forEach(mon -> mon.takeDamage(mon.maxHp())));

        final var monsterStock = manager.getShop().getMonsterStock();
        final var availableBattles = manager.getAvailableBattles().size();

        manager.refreshCurrentDay();

        // New monster stock
        assertFalse(manager.getShop()
            .getMonsterStock()
            .stream()
            .anyMatch(monsterStock::contains)
        );

        // Refreshed battles
        assertNotEquals(availableBattles, manager.getAvailableBattles().size());
    }

    /**
     * BattleManager's <code>partyMonstersLeave</code> should:
     * <ul>
     * <li> Kick out all monster only if the monster <code>shouldLeave</code> returns true</li>
     * </ul>
     */
    @Test
    void partyMonstersLeave() {
        manager.getTrainer().add(ejectMon);

        manager.partyMonstersLeave();

        assertTrue(manager.getTrainer().isWhitedOut());

        manager.getTrainer().add(perfectMon);

        manager.partyMonstersLeave();

        assertFalse(manager.getTrainer().isWhitedOut());
    }

    /**
     * BattleManager's <code>partyMonstersHeal</code> should:
     * <ul>
     * <li> Heal all monster in the party </li>
     * </ul>
     */
    @Test
    void partyMonstersHeal() {
        perfectMon.takeDamage(1);
        manager.getTrainer().add(perfectMon);

        final var currentHp = perfectMon.getCurrentHp();

        manager.partyMonstersHeal();

        assertNotEquals(currentHp, perfectMon.getCurrentHp());
        assertEquals(perfectMon.maxHp(), perfectMon.getCurrentHp());
    }

    /**
     * BattleManager's <code>partyMonstersLevelUp</code> should:
     * <ul>
     * <li> Level up all monster only if their <code>shouldLevelUp</code> returns true </li>
     * </ul>
     */
    @Test
    void partyMonstersLevelUp() {
        manager.getTrainer().add(perfectMon);

        final var currentLevel = perfectMon.getLevel();

        manager.partyMonstersLevelUp();

        assertNotEquals(currentLevel, perfectMon.getLevel());
        assertEquals(currentLevel + 1, perfectMon.getLevel());
    }

    /**
     * BattleManager's <code>monsterJoinsParty</code> should:
     * <ul>
     * <li> If lucky, a new monster should be added the player's party </li>
     * </ul>
     */
    @Test
    void monsterJoinsParty() {
        manager.setMaxDays(1);
        manager.setCurrentDay(1);
        manager.setDifficulty(100);

        manager.monsterJoinsParty();

        assertEquals(1, manager.getTrainer().getParty().size());
    }

    /**
     * BattleManager's <code>updateAvailableBattles</code> should:
     * <ul>
     * <li> Remove all previous enemis</li>
     * <li> Add new enemies with fresh new parties</li>
     * </ul>
     */
    @Test
    void updateAvailableBattles() {
        manager.getAvailableBattles().forEach(trainer -> trainer.getParty().forEach(mon -> mon.takeDamage(mon.maxHp())));

        final var availableBattles = manager.getAvailableBattles().size();

        manager.updateAvailableBattles();

        // Refreshed battles
        assertNotEquals(availableBattles, manager.getAvailableBattles().size());
    }

    @Test
    void prepareBattle() {
    }

    @Test
    void useItemFromInventory() {
    }

    @Test
    void testUseItemFromInventory() {
    }

    @Test
    void switchMonsterOnParty() {
    }

    @Test
    void testSwitchMonsterOnParty() {
    }

    @Test
    void buy() {
    }

    @Test
    void sell() {
    }

    @Test
    void setTrainerName() {
    }
}