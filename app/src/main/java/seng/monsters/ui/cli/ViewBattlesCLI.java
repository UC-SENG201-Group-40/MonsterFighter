package seng.monsters.ui.cli;

import seng.monsters.model.*;

import java.util.InputMismatchException;
import java.util.List;

public final class ViewBattlesCLI extends TestableCLI {

    private final GameManager gameManager;
    private final List<Trainer> availableBattles;
    private final Trainer player;

    public ViewBattlesCLI(GameManager gameManager) {
        this.gameManager = gameManager;
        availableBattles = gameManager.getAvailableBattles();
        player = gameManager.getTrainer();
    }

    private void viewBattlesInterface() {
        displayBattleOptions();
        while (true) {
            try {
                selectBattle(input().nextInt());
                return;
            } catch (InputMismatchException e) {
                input().next();
                System.out.println("Invalid input!");
            }
        }
    }

    private void selectBattle(int scannerInput) {
        try {
            if ((scannerInput > 0) && (scannerInput < availableBattles.size()+1)) {
                final var battleTrainer = availableBattles.get(scannerInput-1);
                BattleCLI.make(player, battleTrainer);
                if (!player.isWhitedOut()) {
                    battleRewards(battleTrainer);
                    displayBattleRewards(battleTrainer);
                } else {
                    System.out.println("You whited out!");
                }
            } else if (scannerInput != 0) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input!");
            selectBattle(input().nextInt());
        }
    }

    private void battleRewards(Trainer loser) {
        gameManager.setGold(gameManager.getGold() + BattleManager.goldReward(loser));
        gameManager.setScore(gameManager.getScore() + BattleManager.scoreReward(loser));
    }

    private void displayBattleOptions() {
        System.out.println("\n===========================\n");
        System.out.println("The following trainers would like to battle!");
        for (int i=0; i < availableBattles.size(); i++) {
            final var battleParty = availableBattles.get(i).getParty();
            System.out.printf("%d - Trainer %d:%n",
                    i+1, i+1);
            for (Monster mon : battleParty) {
                System.out.printf("%s (Level %d)%n",
                        mon.getName(), mon.getLevel());
            }
            System.out.print("\n");
        }
        System.out.println("0 - Return to Main Menu");
    }

    private void displayBattleRewards(Trainer loser) {
        System.out.printf("The enemy payed out %d gold!%n",
                BattleManager.goldReward(loser));
        System.out.printf("You gained %d score!%n",
                BattleManager.scoreReward(loser));
    }

    public static void make(GameManager gameManager) {
        ViewBattlesCLI viewBattlesCLI = new ViewBattlesCLI(gameManager);
        viewBattlesCLI.viewBattlesInterface();
    }
}
