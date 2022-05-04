//
//  BattleManager.java
//  seng-monsters
//
//  Created by Vincent on 8:35 PM.

//
package seng.monsters.model;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Battle manager to handle battle between Trainer.
 * <p>
 * The match is handled using the <code>nextIteration()</code> method which will need to be and
 */
public final class BattleManager {
    /**
     * The UI that provides callbacks at certain event during the match
     */
    public interface UI {
        void onEachFrame(boolean isMon1Turn, int pos);

        void onEachDamage(boolean isMon1Turn, int dmg);

        void onEachNextMonster(boolean isMon1Turn);

        void onEnd(boolean isMon1Turn);
    }

    /**
     * Signals that the trainer no longer have any active (non-fainted) monster left
     */
    public static class WhitedOutException extends IllegalStateException {
    }

    /**
     * The UI class to perform certain action on event
     */
    private final UI ui;

    /**
     * The player trainer
     */
    private final Trainer player1;

    /**
     * The opposing trainer
     */
    private final Trainer player2;

    /**
     * The environment where the battle is held
     */
    private final Environment environment;

    /**
     * The current monster for the player
     */
    private Monster mon1;

    /**
     * The current monster for the opponent
     */
    private Monster mon2;

    /**
     * A state to determine whether the manager can still continue
     */
    private AtomicBoolean isSettled;

    /**
     * The list of actions done
     */
    private final ConcurrentLinkedDeque<String> feeds = new ConcurrentLinkedDeque<>();

    /**
     * The randomizer used to simulate fluctuating damage output
     */
    private final Random rng = new Random();

    /**
     * The boolean signalling whose turn is at this moment
     */
    private boolean isMon1Turn;

    /**
     * Pseudo position state to simulate delay in the attack before connecting
     */
    private int pos;

    /**
     * Pseudo speed (in distance) state the jump of movement in the attack before connecting
     */
    private int speed;

    /**
     * Pseudo end goal state simulating the distance needed to reach before the attack lands
     */
    private int goal;


    public BattleManager(UI ui, Trainer player, Trainer enemy, Environment environment) {
        this.player1 = player;
        this.player2 = enemy;
        this.ui = ui;
        this.environment = environment;
        try {
            this.mon1 = nextPlayerMon();
            this.mon2 = nextEnemyMon();
            this.isMon1Turn = mon1.speed() >= mon2.speed();
            this.pos = isMon1Turn ? 62 : 551;
            this.speed = isMon1Turn ? 20 : -20;
            this.goal = isMon1Turn ? 551 : 62;
            this.isSettled = new AtomicBoolean(false);
        } catch (WhitedOutException ignored) {
            this.isSettled = new AtomicBoolean(true);
        }
    }

    /**
     * A method to proceed to the next iteration of the battle
     */
    public void nextIteration() {
        if (isSettled())
            return;

        if (isEitherFallen()) {
            try {
                mon1 = mon1.isFainted() ? nextPlayerMon() : mon1;
                mon2 = mon2.isFainted() ? nextEnemyMon() : mon2;
                changeMonster();
            } catch (WhitedOutException ignored) {
                endGame();
            }
            return;
        }

        ui.onEachFrame(isMon1Turn, pos);
        pos += speed;

        if (hasAttackLands()) {
            performDamage();
            isMon1Turn = !isMon1Turn;
            pos = isMon1Turn ? 62 : 551;
            speed = -speed;
            goal = isMon1Turn ? 551 : 62;
        }
    }

    /**
     * Check if the pseudo attack has reach the target and lands
     *
     * @return A boolean signalling the attacking landing or not
     */
    private boolean hasAttackLands() {
        return isMon1Turn ? pos >= goal : pos <= goal;
    }

    /**
     * Performed the damage, add new feed that the damage landed, and call damage event callback
     */
    private void performDamage() {
        final var atk = isMon1Turn ? mon1 : mon2;
        final var def = isMon1Turn ? mon2 : mon1;
        final var atkTrainer = isMon1Turn ? player1 : player2;
        final var defTrainer = isMon1Turn ? player2 : player1;

        final var maxDmg = atk.damage(environment);
        final var dmg = rng.nextInt(maxDmg - (maxDmg / 4)) + (maxDmg / 4);

        def.takeDamage(dmg);

        feeds.add(String.format(
            "%s's %s attacked %s's %s dealing %d",
            atkTrainer.getName(), atk.getName(),
            defTrainer.getName(), def.getName(),
            dmg
        ));

        if (def.isFainted()) {
            feeds.add(String.format("%s's %s fainted", defTrainer.getName(), def.getName()));
        }

        ui.onEachDamage(isMon1Turn, dmg);
    }

    /**
     * Switching monsters after either battling monster fainted, and resetting the pseudo states
     */
    private void changeMonster() {
        isMon1Turn = mon1.speed() >= mon2.speed();
        pos = isMon1Turn ? 62 : 551;
        speed = isMon1Turn ? 20 : -20;
        goal = isMon1Turn ? 551 : 62;
        ui.onEachNextMonster(isMon1Turn);
    }

    /**
     * Ending the match, add to the ending feed, and call the end callback
     */
    private void endGame() {
        feeds.add(String.format(
            "%s win with %d monster left and %s lost",
            winner().getName(),
            winner().getParty().stream().filter(m -> !m.isFainted()).toList().size(),
            loser().getName()
        ));
        isSettled.set(true);
        ui.onEnd(isMon1Turn);
    }

    /**
     * Get the next monster for the player that is not fainted
     *
     * @return A next non-fainted monster if there is none return null
     */
    private Monster nextPlayerMon() throws WhitedOutException {
        final var nextMon = player1.getParty().stream().filter(mon -> !mon.isFainted()).findFirst();
        try {
            return nextMon.orElseThrow();
        } catch (NoSuchElementException ignored) {
            throw new WhitedOutException();
        }
    }

    /**
     * Get the next monster for the enemy that is not fainted
     *
     * @return A next non-fainted monster if there is none return null
     */
    private Monster nextEnemyMon() throws WhitedOutException {
        final var nextMon = player2.getParty().stream().filter(mon -> !mon.isFainted()).findFirst();
        try {
            return nextMon.orElseThrow();
        } catch (NoSuchElementException ignored) {
            throw new WhitedOutException();
        }
    }

    /**
     * Get the feeds broadcast during the battle
     *
     * @return A concurrent linked deque containing the feeds
     */
    public Collection<String> getFeeds() {
        return feeds;
    }

    /**
     * Get the player's monster currently battling
     *
     * @return A monster currently battling
     */
    public Monster getMon1() {
        return mon1;
    }

    /**
     * Get the enemy's monster currently battling
     *
     * @return A monster currently battling
     */
    public Monster getMon2() {
        return mon2;
    }

    /**
     * Get the player trainer
     *
     * @return A trainer representation for the player
     */
    public Trainer getPlayer1() {
        return player1;
    }

    /**
     * Get the opposing trainer
     *
     * @return A trainer representation for the enemy
     */
    public Trainer getPlayer2() {
        return player2;
    }

    /**
     * Get the winning trainer
     *
     * @return A trainer is the battle is settled otherwise null
     */
    private Trainer winner() {
        final var remainingPlayerMon = player1.getParty().stream().filter(mon -> !mon.isFainted()).count();
        final var remainingEnemyMon = player2.getParty().stream().filter(mon -> !mon.isFainted()).count();
        return remainingPlayerMon >= remainingEnemyMon ? player1 : player2;
    }

    /**
     * Get the losing trainer
     *
     * @return A trainer is the battle is settled otherwise null
     */
    private Trainer loser() {
        final var remainingPlayerMon = player1.getParty().stream().filter(mon -> !mon.isFainted()).count();
        final var remainingEnemyMon = player2.getParty().stream().filter(mon -> !mon.isFainted()).count();
        return remainingPlayerMon >= remainingEnemyMon ? player2 : player1;
    }

    /**
     * Check if either battling monster has fainted
     *
     * @return A boolean signalling if either fainted
     */
    public boolean isEitherFallen() {
        return isSettled() || mon1.isFainted() || mon2.isFainted();
    }

    /**
     * Check if the match has settled
     *
     * @return A boolean signalling that result
     */
    public boolean isSettled() {
        return isSettled.get();
    }

    /**
     * The gold total rewarded for this battle
     * @return The amount gold should be received by the winner
     */
    public static int goldReward(Trainer loser) {
        return loser.getParty()
            .stream()
            .mapToInt(Monster::sellPrice)
            .sum();
    }

    /**
     * The score total rewarded for this battle
     * @return The amount score should be received by the winner
     */
    public static int scoreReward(Trainer loser) {
        return loser.getParty()
            .stream()
            .mapToInt(Monster::getLevel)
            .sum();
    }
}