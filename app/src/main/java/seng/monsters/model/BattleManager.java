//
//  BattleManager.java
//  seng-monsters
//
//  Created by Vincent on 8:35 PM.

//
package seng.monsters.model;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
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
        /**
         * The action done on each frame of the moving attack
         *
         * @param percentage The progress percentage of the attack
         */
        void onEachAttackProgress(int percentage);

        /**
         * The action done when the pseudo attack lands and dealt damage
         *
         * @param isPlayerTurn True if the player is attacking, otherwise false
         * @param dmg          The damage dealt
         */
        void onEachLandedAttack(boolean isPlayerTurn, int dmg);

        /**
         * The action done when either battling monster needed to be switched out
         *
         * @param isPlayerTurn True if the player is attacking, otherwise false
         */
        void onEachNextMonster(boolean isPlayerTurn);

        /**
         * The action done after the battle has concluded
         */
        void onEnd();
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
    private final Trainer player;

    /**
     * The opposing trainer
     */
    private final Trainer enemy;

    /**
     * The environment where the battle is held
     */
    private final Environment environment;

    /**
     * The current monster for the player
     */
    private Monster battlingPlayerMonster;

    /**
     * The current monster for the opponent
     */
    private Monster battlingEnemyMonster;

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
    private boolean isPlayerTurn;

    /**
     * Pseudo position state to simulate delay in the attack before connecting
     */
    private int pseudoAttackPosition;

    /**
     * Pseudo speed (in distance) state the jump of movement in the attack before connecting
     */
    private int pseudoSpeed;

    /**
     * Pseudo end goal state simulating the distance needed to reach before the attack lands
     */
    private int pseudoGoal;


    /**
     * Creates a battle manager that provide functions to simulate the battle process
     * @param ui The UI that can perform actions on certain events
     * @param player The player trainer
     * @param enemy The enemy trainer to fight
     * @param environment The environment for the battle
     */
    public BattleManager(UI ui, Trainer player, Trainer enemy, Environment environment) {
        this.player = player;
        this.enemy = enemy;
        this.ui = ui;
        this.environment = environment;
        try {
            this.battlingPlayerMonster = nextPlayerMon();
            this.battlingEnemyMonster = nextEnemyMon();
            this.isPlayerTurn = battlingPlayerMonster.speed() >= battlingEnemyMonster.speed();
            this.pseudoAttackPosition = isPlayerTurn ? PSEUDO_MIN_POSITION : PSEUDO_MAX_POSITION;
            this.pseudoSpeed = isPlayerTurn ? PSEUDO_ATTACK_SPEED : -PSEUDO_ATTACK_SPEED;
            this.pseudoGoal = isPlayerTurn ? PSEUDO_MAX_POSITION : PSEUDO_MIN_POSITION;
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
                battlingPlayerMonster = battlingPlayerMonster.isFainted() ? nextPlayerMon() : battlingPlayerMonster;
                battlingEnemyMonster = battlingEnemyMonster.isFainted() ? nextEnemyMon() : battlingEnemyMonster;
                changeMonster();
            } catch (WhitedOutException ignored) {
                endGame();
            }
            return;
        }

        final int progress = pseudoAttackPosition * 100 / PSEUDO_MAX_POSITION;
        ui.onEachAttackProgress(progress);
        pseudoAttackPosition += pseudoSpeed;

        // If attack has landed, we perform the damage calculation,
        // and reset / flip the pseudo attack position, goal, and speed
        if (hasAttackLanded()) {
            performDamage();
            isPlayerTurn = !isPlayerTurn;
            pseudoAttackPosition = isPlayerTurn ? PSEUDO_MIN_POSITION : PSEUDO_MAX_POSITION;
            pseudoSpeed = -pseudoSpeed;
            pseudoGoal = isPlayerTurn ? PSEUDO_MAX_POSITION : PSEUDO_MIN_POSITION;
        }
    }

    /**
     * Check if the pseudo attack has reach the target and lands
     *
     * @return A boolean signalling the attacking landing or not
     */
    private boolean hasAttackLanded() {
        return isPlayerTurn ? pseudoAttackPosition >= pseudoGoal : pseudoAttackPosition <= pseudoGoal;
    }

    /**
     * Performed the damage, add new feed that the damage landed, and call damage event callback
     */
    private void performDamage() {
        final Monster atk = isPlayerTurn ? battlingPlayerMonster : battlingEnemyMonster;
        final Monster def = isPlayerTurn ? battlingEnemyMonster : battlingPlayerMonster;
        final Trainer atkTrainer = isPlayerTurn ? player : enemy;
        final Trainer defTrainer = isPlayerTurn ? enemy : player;

        final int maxDmg = atk.damage(environment);
        final int dmg = rng.nextInt(maxDmg - (maxDmg / 4)) + (maxDmg / 4);

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

        ui.onEachLandedAttack(isPlayerTurn, dmg);
    }

    /**
     * Switching monsters after either battling monster fainted, and resetting the pseudo states
     */
    private void changeMonster() {
        isPlayerTurn = battlingPlayerMonster.speed() >= battlingEnemyMonster.speed();
        pseudoAttackPosition = isPlayerTurn ? PSEUDO_MIN_POSITION : PSEUDO_MAX_POSITION;
        pseudoSpeed = isPlayerTurn ? PSEUDO_ATTACK_SPEED : -PSEUDO_ATTACK_SPEED;
        pseudoGoal = isPlayerTurn ? PSEUDO_MAX_POSITION : PSEUDO_MIN_POSITION;
        ui.onEachNextMonster(isPlayerTurn);
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
        ui.onEnd();
    }

    /**
     * Get the next monster for the player that is not fainted
     *
     * @return A next non-fainted monster if there is none return null
     */
    private Monster nextPlayerMon() throws WhitedOutException {
        final Optional<Monster> nextMon = player.getParty().stream().filter(mon -> !mon.isFainted()).findFirst();
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
        final Optional<Monster> nextMon = enemy.getParty().stream().filter(mon -> !mon.isFainted()).findFirst();
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
    public Monster getBattlingPlayerMonster() {
        return battlingPlayerMonster;
    }

    /**
     * Get the enemy's monster currently battling
     *
     * @return A monster currently battling
     */
    public Monster getBattlingEnemyMonster() {
        return battlingEnemyMonster;
    }

    /**
     * Get the player trainer
     *
     * @return A trainer representation for the player
     */
    public Trainer getPlayer() {
        return player;
    }

    /**
     * Get the opposing trainer
     *
     * @return A trainer representation for the enemy
     */
    public Trainer getEnemy() {
        return enemy;
    }

    /**
     * Get the winning trainer
     *
     * @return A trainer is the battle is settled otherwise null
     */
    private Trainer winner() {
        final long remainingPlayerMon = player.getParty().stream().filter(mon -> !mon.isFainted()).count();
        final long remainingEnemyMon = enemy.getParty().stream().filter(mon -> !mon.isFainted()).count();
        return remainingPlayerMon >= remainingEnemyMon ? player : enemy;
    }

    /**
     * Get the losing trainer
     *
     * @return A trainer is the battle is settled otherwise null
     */
    private Trainer loser() {
        final long remainingPlayerMon = player.getParty().stream().filter(mon -> !mon.isFainted()).count();
        final long remainingEnemyMon = enemy.getParty().stream().filter(mon -> !mon.isFainted()).count();
        return remainingPlayerMon >= remainingEnemyMon ? enemy : player;
    }

    /**
     * Check if either battling monster has fainted
     *
     * @return A boolean signalling if either fainted
     */
    public boolean isEitherFallen() {
        return isSettled() || battlingPlayerMonster.isFainted() || battlingEnemyMonster.isFainted();
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
     * Check if the player1 has won
     *
     * @return True if all player 2 monster is fainted, otherwise false
     */
    public boolean hasPlayerWon() {
        return enemy.getParty().stream().allMatch(Monster::isFainted);
    }

    /**
     * The gold total rewarded for this battle
     *
     * @return The amount gold should be received by the winner
     */
    public int goldReward() {
        return loser().getParty()
            .stream()
            .mapToInt(Monster::sellPrice)
            .sum();
    }

    /**
     * The score total rewarded for this battle
     *
     * @return The amount score should be received by the winner
     */
    public int scoreReward() {
        return loser().getParty()
            .stream()
            .mapToInt(Monster::getLevel)
            .sum();
    }

    /**
     * The upmost right pseudo position of the attack / The required steps before attack lands
     */
    public static final int PSEUDO_MAX_POSITION = 25;

    /**
     * The upmost left pseudo position of the attack
     */
    public static final int PSEUDO_MIN_POSITION = 0;

    /**
     * The amount of jump of position on each iteration of the attack
     */
    public static final int PSEUDO_ATTACK_SPEED = 1;
}