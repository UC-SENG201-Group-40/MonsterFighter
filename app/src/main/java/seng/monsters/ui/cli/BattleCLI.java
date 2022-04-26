//
//  BattleCLI.java
//  seng-monsters
//
//  Created by Vincent on 7:14 PM.
//
package seng.monsters.ui.cli;

import seng.monsters.model.BattleManager;
import seng.monsters.model.Environment;
import seng.monsters.model.Trainer;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * TODO: This is still a testing CLI, it works but need checks and testing to use it in the final application
 */
public final class BattleCLI implements BattleManager.UI {
    private final BattleManager battler;
    private final Set<String> loggedFeeds = new HashSet<>();

    public BattleCLI(Trainer lhs, Trainer rhs) {
        battler = new BattleManager(this, lhs, rhs, Environment.FIELD);
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
        final var mon1 = battler.getMon1();
        final var mon2 = battler.getMon2();
        return String.format(
            "%s (HP: %d) vs %s (HP: %d)",
            mon1.getName(), mon1.getCurrentHp(),
            mon2.getName(), mon2.getCurrentHp()
        );
    }

    private String partyFeed() {
        final var mon1 = battler.getMon1();
        final var mon2 = battler.getMon2();

        final var party1 = battler.getPlayer1()
            .getParty()
            .stream()
            .map(mon -> mon.isFainted() ? "(_)" : "(x)")
            .reduce((acc, x) -> acc + "-" + x)
            .orElse("");

        final var party2 = battler.getPlayer2()
            .getParty()
            .stream()
            .map(mon -> mon.isFainted() ? "(_)" : "(x)")
            .reduce((acc, x) -> acc + "-" + x)
            .orElse("");

        return String.format(
            "Player 1: %s (HP: %d) %s" + "\n" + "Player 2: %s (HP: %d) %s",
            mon1.getName(), mon1.getCurrentHp(), party1,
            mon2.getName(), mon2.getCurrentHp(), party2
        );
    }

    @Override
    public void onEachFrame(boolean isMon1Turn, int pos) {
    }

    @Override
    public void onEachDamage(boolean isMon1Turn, int dmg) {
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
    public void onEachNextMonster(boolean isMon1Turn) {
        System.out.println("===========================");
        System.out.println(partyFeed());
        System.out.println("===========================");
    }

    @Override
    public void onEnd(boolean isMon1Turn) {
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
        System.out.println("===========================");
    }

    public static void make(Trainer trainer1, Trainer trainer2) {
        try {
            final var cli = new BattleCLI(trainer1, trainer2);
            cli.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
