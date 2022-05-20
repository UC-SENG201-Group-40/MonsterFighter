package seng.monsters.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class BattleManagerTest {
    private BattleManager battleManager;
    private Trainer player;
    private Trainer enemy;
    private BattleManager.UI dud;

    private static abstract class DudUI implements BattleManager.UI {
        public void onEachAttackProgress(int percentage) {
        }

        public void onEachLandedAttack(boolean isPlayerTurn, int dmg) {
        }

        public void onEachNextMonster(boolean isPlayerTurn) {
        }

        public void onEnd() {
        }
    }

    @BeforeEach
    void setUp() {
        player = new Trainer("Player");
        enemy = new Trainer("Enemy");
        dud = new DudUI() {
        };
        battleManager = null;
    }

    /**
     * BattleManager <code>nextIteration</code> should:
     * <ul>
     * <li>Be settled immediately and do nothing if either has no monster</li>
     * <li>Should execute the <code>onEachNextMonster</code> callback and switch monster
     *   if either battling monster is fainted and there is monster to replace it</li>
     * <li>Should execute the <code>onEnd</code> callback
     *   if either battling monster is fainted and there is no monster to replace it</li>
     * <li>Should execute normally and call both <code>onEachFrame</code> and <code>onEachDamage</code> when appropriate</li>
     * </ul>
     */
    @Test
    void nextIteration() {
        // Both trainer has no monsters, nextIteration should do anything
        battleManager = new BattleManager(dud, player, enemy, Environment.FIELD);
        final int prevFeedSize = battleManager.getFeeds().size();
        battleManager.nextIteration();
        assertEquals(prevFeedSize, battleManager.getFeeds().size());

        // --- Setup 1 ---
        final Monster mon0 = new Monster.Tree(10);
        final Monster mon1 = new Monster.Doger(10);
        final AtomicBoolean isChangeMonsterCalled = new AtomicBoolean(false);
        final AtomicBoolean isEndGameCalled = new AtomicBoolean(false);
        player.add(mon0);
        player.add(mon1);
        enemy.add(new Monster.Raver(10));
        battleManager = new BattleManager(
            new DudUI() {
                @Override
                public void onEachNextMonster(boolean isPlayerTurn) {
                    isChangeMonsterCalled.set(true);
                }

                @Override
                public void onEnd() {
                    isEndGameCalled.set(true);
                }
            },
            player, enemy, Environment.FIELD
        );

        // Player has 1 monster but immediately faints afterwards (has replacement)
        mon0.takeDamage(mon0.maxHp());
        assertTrue(battleManager.isEitherFallen());
        battleManager.nextIteration();
        assertTrue(isChangeMonsterCalled.get());

        // Player has 1 monster but immediately faints afterwards (no replacement)
        mon1.takeDamage(mon1.maxHp());
        assertTrue(battleManager.isEitherFallen());
        battleManager.nextIteration();
        assertTrue(isEndGameCalled.get());
        assertTrue(battleManager.isSettled());
        assertEquals(0, battleManager.getPlayer().getParty().stream().filter(mon -> !mon.isFainted()).count());
        assertEquals(1, battleManager.getEnemy().getParty().stream().filter(mon -> !mon.isFainted()).count());

        // --- Setup 2 ---
        mon0.healSelf(mon0.maxHp());
        mon1.healSelf(mon1.maxHp());
        final AtomicBoolean isEachFrameCalled = new AtomicBoolean(false);
        final AtomicBoolean isEachDamageCalled = new AtomicBoolean(false);
        battleManager = new BattleManager(
            new DudUI() {
                @Override
                public void onEachLandedAttack(boolean isPlayerTurn, int dmg) {
                    isEachDamageCalled.set(true);
                }

                @Override
                public void onEachAttackProgress(int percentage) {
                    isEachFrameCalled.set(true);
                }
            },
            player, enemy, Environment.FIELD
        );

        // The battle is run normally so each pseudo progress (frame) should be called
        battleManager.nextIteration();
        assertFalse(battleManager.isSettled());
        assertTrue(isEachFrameCalled.get());

        // The battle is run normally until the attack "lands"
        assertFalse(battleManager.isSettled());
        for (int i = 0; i < 26; i++) {
            battleManager.nextIteration();
        }
        assertTrue(isEachDamageCalled.get());
    }

    /**
     * BattleManager <code>getFeeds</code> should:
     * <ul>
     * <li> Give all the accumulated feed broadcast by the manager
     * </ul>
     */
    @Test
    void getFeeds() {
        for (int i = 0; i < 4; i++) {
            player.add(new Monster.Doger(1));
            enemy.add(new Monster.Tree(1));
        }
        final AtomicBoolean hasTakenDamage = new AtomicBoolean(false);
        battleManager = new BattleManager(
            new DudUI() {
                @Override
                public void onEachLandedAttack(boolean isPlayerTurn, int dmg) {
                    hasTakenDamage.set(true);
                }
            },
            player, enemy, Environment.FIELD
        );

        assertEquals(0, battleManager.getFeeds().size());

        while (!hasTakenDamage.get()) {
            battleManager.nextIteration();
        }
        assertNotEquals(0, battleManager.getFeeds().size());
    }

    /**
     * BattleManager <code>isEitherFallen</code> should:
     * <ul>
     * <li> Return true is either battling monster is fainted
     * <li> Return false is either is still standing
     * </ul>
     */
    @Test
    void isEitherFallen() {
        player.add(new Monster.Doger(6));
        enemy.add(new Monster.Tree(6));

        battleManager = new BattleManager(dud, player, enemy, Environment.FIELD);
        assertFalse(battleManager.isEitherFallen());

        final Monster mon0 = battleManager.getBattlingPlayerMonster();
        mon0.takeDamage(mon0.maxHp());
        assertTrue(battleManager.isEitherFallen());

        final Monster mon1 = battleManager.getBattlingEnemyMonster();
        mon0.healSelf(mon0.maxHp());
        mon1.takeDamage(mon1.maxHp());
        assertTrue(battleManager.isEitherFallen());
    }

    /**
     * BattleManager <code>isSettled</code> should:
     * <ul>
     * <li> Return false the battle is still on going </li>
     * <li> Return true if either trainers no longer have any active monster and <code>nextIteration</code> was called
     * <li> Return true if the player has no active monster to begin with
     * </ul>
     */
    @Test
    void isSettled() {
        battleManager = new BattleManager(dud, player, enemy, Environment.FIELD);
        assertTrue(battleManager.isSettled());

        final Monster mon = new Monster.Doger(1);
        player.add(mon);
        enemy.add(new Monster.Doger(1));
        battleManager = new BattleManager(dud, player, enemy, Environment.FIELD);
        assertFalse(battleManager.isSettled());

        mon.takeDamage(mon.maxHp());
        battleManager.nextIteration();
        assertTrue(battleManager.isSettled());
    }


    /**
     * BattleManager's:
     * <ul>
     * <li><code>goldReward</code> should return the sum of sell price for the losing player</li>
     * <li><code>scoreReward</code> should return the sum of level for the losing player</li>
     * </ul>
     */
    @Test
    void reward() {
        final Monster alive = new Monster.Doger(10);
        final Monster dead1 = new Monster.Doger(10);
        final Monster dead2 = new Monster.Doger(10);
        player.add(alive);
        enemy.add(dead1);
        enemy.add(dead2);

        battleManager = new BattleManager(dud, player, enemy, Environment.FIELD);

        dead1.takeDamage(dead1.maxHp());
        dead2.takeDamage(dead2.maxHp());
        battleManager.nextIteration();

        assertTrue(battleManager.isSettled());
        assertTrue(battleManager.hasPlayerWon());
        assertEquals(dead1.sellPrice() + dead2.sellPrice(), battleManager.goldReward());
        assertEquals(dead1.getLevel() + dead2.getLevel(), battleManager.scoreReward());
    }
}