//
//  BattleCLI.java
//  seng-monsters
//
//  Created by Vincent on 7:14 PM.
//
package seng.monsters.ui.cli;

import seng.monsters.model.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TODO: This is still a testing CLI, it works but need checks and testing to use it in the final application
 */
public final class BattleCLI extends TestableCLI implements BattleManager.UI {
    private final BattleManager battler;
    private final Set<String> loggedFeeds = new HashSet<>();
    private final GameManager gameManager;

    /**
     * Create a CLi for battling between two trainer
     *
     * @param gameManager The game manager for the logic
     * @param index       The index of the enemy
     */
    public BattleCLI(GameManager gameManager, int index) {
        this.battler = gameManager.prepareBattle(this, index);
        this.gameManager = gameManager;
    }

    /**
     * Execute the battle until it is settled
     */
    public void run() {
        System.out.println("===========================");
        System.out.println(partyFeed());
        System.out.println("===========================");

        while (!battler.isSettled()) {
            battler.nextIteration();
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private String hpFeed() {
        final Monster playerMon = battler.getBattlingPlayerMonster();
        final Monster enemyMon = battler.getBattlingEnemyMonster();
        return String.format(
            "%s (HP: %d) vs %s (HP: %d)",
            playerMon.getName(), playerMon.getCurrentHp(),
            enemyMon.getName(), enemyMon.getCurrentHp()
        );
    }

    private String partyFeed() {
        final Monster playerMon = battler.getBattlingPlayerMonster();
        final Monster enemyMon = battler.getBattlingEnemyMonster();

        final String party1 = battler.getPlayer()
            .getParty()
            .stream()
            .map(mon -> mon.isFainted() ? "(_)" : "(x)")
            .reduce((acc, x) -> acc + "-" + x)
            .orElse("");

        final String party2 = battler.getEnemy()
            .getParty()
            .stream()
            .map(mon -> mon.isFainted() ? "(_)" : "(x)")
            .reduce((acc, x) -> acc + "-" + x)
            .orElse("");

        return String.format(
            "Trainer 1: %s (HP: %d) %s" + "\n" + "Trainer 2: %s (HP: %d) %s",
            playerMon.getName(), playerMon.getCurrentHp(), party1,
            enemyMon.getName(), enemyMon.getCurrentHp(), party2
        );
    }

    @Override
    public void onEachAttackProgress(int percentage) {
    }

    @Override
    public void onEachLandedAttack(boolean isPlayerTurn, int dmg) {
        battler
            .getFeeds()
            .stream()
            .filter(each -> !loggedFeeds.contains(each))
            .forEachOrdered(feed -> {
                System.out.println(feed + " [" + hpFeed() + "]");
                loggedFeeds.add(feed);
            });
    }

    @Override
    public void onEachNextMonster(boolean isPlayerTurn) {
        System.out.println("===========================");
        System.out.println(partyFeed());
        System.out.println("===========================");
    }

    @Override
    public void onEnd() {
        final List<String> feeds = battler
            .getFeeds()
            .stream()
            .filter(each -> !loggedFeeds.contains(each))
            .toList();
        feeds.subList(0, feeds.size() - 1)
            .forEach(feed -> {
                System.out.println(feed + " [" + hpFeed() + "]");
                loggedFeeds.add(feed);
            });
        System.out.println("===========================");
        System.out.println(feeds.get(feeds.size() - 1));
        System.out.println(partyFeed());
        if (!gameManager.getPlayer().isWhitedOut()) {
            displayBattleRewards();
            battleRewards();
        }
        System.out.println("===========================");
    }

    private void displayBattleRewards() {
        System.out.printf("The enemy payed out %d gold!%n",
            battler.goldReward());
        System.out.printf("You gained %d score!%n",
            battler.scoreReward());
    }

    private void battleRewards() {
        gameManager.setGold(gameManager.getGold() + battler.goldReward());
        gameManager.setScore(gameManager.getScore() + battler.scoreReward());
    }
}
