//
//  BattleManager.java
//  seng-monsters
//
//  Created by d-exclaimation on 8:35 PM.
//  Copyright © 2022 d-exclaimation. All rights reserved.
//
package seng.monsters.model;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Random;

/**
 * Battle manager to handle battle between Trainer.
 *
 * The match is handled with <code>javax.swing.Timer</code> to simulate monster attacking each other in a visible manner.
 */
public final class BattleManager {
    /**
     * The UI that provides callbacks at certain event during the match
     */
    public interface UI {
        int frameDelay();

        void beforeStart(boolean isMon1Turn);

        void afterStart();

        void onEachFrame(boolean isMon1Turn, int pos);

        void onEachDamage(boolean isMon1Turn, int dmg);

        void onEachNextMonster(boolean isMon1Turn);

        void onEnd(boolean isMon1Turn);
    }

    /**
     * Signals that the trainer no longer have any active (non-fainted) monster left
     */
    public static class WhitedOutException extends IllegalStateException {}

    /** The UI class to perform certain action on event */
    private final UI ui;

    /** The player trainer */
    private final Trainer player1;

    /** The opposing trainer */
    private final Trainer player2;

    /** The current monster for the player */
    private Monster mon1;

    /** The current monster for the opponent */
    private Monster mon2;

    private AtomicBoolean isSettled;

    /** The list of actions done */
    private final ConcurrentLinkedDeque<String> feeds = new ConcurrentLinkedDeque<>();

    /** The Timer to perform interval based action without blocking the main thread*/
    private Timer timer;

    /**
     * Battle manager to handle battle between Trainer.
     *
     * The match is handled with <code>javax.swing.Timer</code> to simulate monster attacking each other in a visible manner.
     */
    public BattleManager(UI ui, Trainer player, Trainer enemy) {
        this.player1 = player;
        this.player2 = enemy;
        this.ui = ui;
        try {
            this.mon1 = nextPlayerMon();
            this.mon2 = nextEnemyMon();
            this.isSettled = new AtomicBoolean(false);
        } catch (WhitedOutException ignored) {
            this.isSettled = new AtomicBoolean(true);
        }
    }

    /**
     * Wrapper higher-order function for <code>start()</code>
     * @return An action listener to start the match
     */
    public ActionListener onStart() {
        return e -> start();
    }


    /**
     * Start the battle until either side loses
     */
    public void start() {
        if (isSettled()) return;

        final var isMon1 = mon1.speed() >= mon2.speed();

        ui.beforeStart(isMon1);

        timer = new Timer(ui.frameDelay(), onEachTimer(isMon1));
        timer.start();

        ui.afterStart();
    }

    /**
     * Action listener for each tick by the Timer to proceed with the match
     * @param isMon1 A boolean signalling who start first
     * @return An action listener simulating a non-instant fight between both parties
     */
    private ActionListener onEachTimer(boolean isMon1) {
        return new ActionListener() {
            /** The randomizer used to simulate fluctuating damage output */
            private final Random rng = new Random();

            /** The boolean signalling whose turn is at this moment */
            private boolean isMon1Turn = isMon1;

            /** Pseudo position state to simulate delay in the attack before connecting*/
            private int pos = isMon1 ? 62 : 551;

            /** Pseudo speed (in distance) state the jump of movement in the attack before connecting */
            private int speed = isMon1 ? 20 : -20;

            /** Pseudo end goal state simulating the distance needed to reach before the attack lands */
            private int goal = isMon1 ? 551 : 62;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isEitherFallen()) {
                    try {
                        mon1 = mon1.isFainted() ? nextPlayerMon() : mon1;
                        mon2 = mon2.isFainted() ? nextEnemyMon() : mon2;
                        onChangeMonster();
                    } catch (WhitedOutException ignored) {
                        onEndGame();
                    }
                    return;
                }

                ui.onEachFrame(isMon1Turn, pos);
                pos += speed;

                if (hasAttackLands()) {
                    afterAttackLands();
                }
            }

            /**
             * Check if the pseudo attack has reach the target and lands
             * @return A boolean signalling the attacking landing or not
             */
            private boolean hasAttackLands() {
                return isMon1Turn ? pos >= goal : pos <= goal;
            }

            /**
             * Performed the damage, add new feed that the damage landed, and call damage event callback
             */
            private void damage() {
                final var atk = isMon1Turn ? mon1 : mon2;
                final var def = isMon1Turn ? mon2 : mon1;
                final var atkTrainer = isMon1Turn ? player1 : player2;
                final var defTrainer = isMon1Turn ? player2 : player1;

                final var maxDmg = atk.damage(Environment.FIELD);
                final var dmg = rng.nextInt(maxDmg - (maxDmg / 4)) + (maxDmg / 4);

                def.takeDamage(dmg);

                feeds.add(String.format(
                    "%s's %s attacked %s's %s dealing %d",
                    atkTrainer.getName(), atk.getName(),
                    defTrainer.getName(), def.getName(),
                    dmg
                ));

                if (def.isFainted()) {
                    feeds.add(String.format("%s's %s fainted", defTrainer.getName(),  def.getName()));
                }

                ui.onEachDamage(isMon1Turn, dmg);
            }

            /**
             * Switching monsters after either battling monster fainted, and resetting the pseudo states
             */
            private void onChangeMonster() {
                isMon1Turn = mon1.speed() >= mon2.speed();
                pos = isMon1Turn ? 62 : 551;
                speed = isMon1Turn ? 20 : -20;
                goal = isMon1Turn ? 551 : 62;
                ui.onEachNextMonster(isMon1Turn);
            }

            /**
             * Ending the match, add to the ending feed, call the end callback, and stopping the timer
             */
            private void onEndGame() {
                feeds.add(String.format(
                    "%s win with %d monster left and %s lost",
                    winner().getName(),
                    winner().getParty().stream().filter(m -> !m.isFainted()).toList().size(),
                    loser().getName()
                ));
                isSettled.set(true);
                ui.onEnd(isMon1Turn);

                if(timer != null)
                    timer.stop();
            }

            /**
             * Perform pseudo state reset  after attack lands
             */
            private void afterAttackLands() {
                damage();
                isMon1Turn = !isMon1Turn;
                pos = isMon1Turn ? 62 : 551;
                speed = -speed;
                goal = isMon1Turn ? 551 : 62;
            }
        };
    }


    /**
     * Get the next monster for the player that is not fainted
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
     * @return A concurrent linked deque containing the feeds
     */
    public Collection<String> getFeeds() {
        return feeds;
    }

    /**
     * Get the player's monster currently battling
     * @return A monster currently battling
     */
    public Monster getMon1() {
        return mon1;
    }

    /**
     * Get the enemy's monster currently battling
     * @return A monster currently battling
     */
    public Monster getMon2() {
        return mon2;
    }

    /**
     * Get the winning trainer
     * @return A trainer is the battle is settled otherwise null
     */
    private Trainer winner() {
        return mon2.isFainted() ? player1 : player2;
    }

    /**
     * Get the losing trainer
     * @return A trainer is the battle is settled otherwise null
     */
    private Trainer loser() {
        return mon1.isFainted() ? player1 : player2;
    }

    /**
     * Check if either battling monster has fainted
     * @return A boolean signalling if either fainted
     */
    public boolean isEitherFallen() {
        return mon1.isFainted() || mon2.isFainted();
    }
    /**
     * Check if the match has settled
     * @return A boolean signalling that result
     */
    public boolean isSettled() {
        return isSettled.get();
    }
}
