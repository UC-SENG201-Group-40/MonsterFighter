//
//  BattleCLI.java
//  seng-monsters
//
//  Created by Vincent on 7:14 PM.
//
package seng.monsters.ui.cli;

import seng.monsters.model.BattleManager;
import seng.monsters.model.Environment;
import seng.monsters.model.GameManager;
import seng.monsters.model.Trainer;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO: This is still a testing CLI, it works but need checks and testing to use it in the final application
 */
public final class BattleCLI extends TestableCLI implements BattleManager.UI {
    private final BattleManager battler;
    private final Set<String> loggedFeeds = new HashSet<>();
    private final GameManager gameManager;

    public BattleCLI(Trainer lhs, Trainer rhs, GameManager gameManager) {
        battler = new BattleManager(this, lhs, rhs, Environment.FIELD);
        this.gameManager = gameManager;
    }

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
        final var playerMon = battler.getBattlingPlayerMonster();
        final var enemyMon = battler.getBattlingEnemyMonster();
        return String.format(
            "%s (HP: %d) vs %s (HP: %d)",
            playerMon.getName(), playerMon.getCurrentHp(),
            enemyMon.getName(), enemyMon.getCurrentHp()
        );
    }

    private String partyFeed() {
        final var playerMon = battler.getBattlingPlayerMonster();
        final var enemyMon = battler.getBattlingEnemyMonster();

        final var party1 = battler.getPlayer()
            .getParty()
            .stream()
            .map(mon -> mon.isFainted() ? "(_)" : "(x)")
            .reduce((acc, x) -> acc + "-" + x)
            .orElse("");

        final var party2 = battler.getEnemy()
            .getParty()
            .stream()
            .map(mon -> mon.isFainted() ? "(_)" : "(x)")
            .reduce((acc, x) -> acc + "-" + x)
            .orElse("");

        return String.format(
            "Player 1: %s (HP: %d) %s" + "\n" + "Player 2: %s (HP: %d) %s",
            playerMon.getName(), playerMon.getCurrentHp(), party1,
            enemyMon.getName(), enemyMon.getCurrentHp(), party2
        );
    }

    @Override
    public void onEachFrame(int percentage) {
    }

    @Override
    public void onEachDamage(boolean isPlayerTurn, int dmg) {
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
        final var feeds = battler
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
        if (!gameManager.getTrainer().isWhitedOut()) {
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

    public static void make(Trainer trainer1, Trainer trainer2, GameManager gameManager) {
        try {
            final var cli = new BattleCLI(trainer1, trainer2, gameManager);
            cli.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
