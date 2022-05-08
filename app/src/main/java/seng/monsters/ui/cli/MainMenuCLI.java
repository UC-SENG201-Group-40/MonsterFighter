package seng.monsters.ui.cli;

import seng.monsters.model.GameManager;
import seng.monsters.model.Monster;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public final class MainMenuCLI extends TestableCLI {

    private final GameManager gameManager;
    private final List<String> menuOptions = List.of(
        "Manage Party", "Battle", "View Item Inventory", "Enter Shop", "Sleep");

    public MainMenuCLI(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    private void mainMenuInterface() throws IllegalArgumentException {
        displayMainMenu();
        while (true) {
            try {
                selectMainMenuOption(input().nextInt());
                return;
            } catch (InputMismatchException e) {
                input().next();
                System.out.println("Invalid input!");
            }
        }
    }

    private void selectMainMenuOption(int scannerInput) throws IllegalArgumentException {
        try {
            switch (scannerInput) {
                case 1 -> {
                    PartyCLI.make(gameManager);
                    mainMenuInterface();
                }
                case 2 -> {
                    if (gameManager.hasNotBattleOnce()) {
                        ViewBattlesCLI.make(gameManager);
                    } else {
                        System.out.println("\nYou've already battled today!");
                    }
                    mainMenuInterface();
                }
                case 3 -> {
                    InventoryCLI.make(gameManager);
                    mainMenuInterface();
                }
                case 4 -> {
                    IntermediateShopCLI.make(gameManager);
                    mainMenuInterface();
                }
                case 5 -> {
                    final var hasEnded = gameManager.nextDay();
                    if (hasEnded) {
                        displayEndScreen();
                    } else {
                        displayNightEvents();
                        mainMenuInterface();
                    }
                }
                default -> throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input!");
            selectMainMenuOption(input().nextInt());
        }
    }

    private void displayMainMenu() {
        System.out.println("\n===========================\n");
        System.out.printf("Gold: %d%n",
            gameManager.getGold());
        System.out.printf("Current day: %d/%d%n",
            gameManager.getCurrentDay(), gameManager.getMaxDays());
        System.out.printf("Current environment: %s%n%n",
            gameManager.getEnvironment());
        System.out.println("Select an option:");
        for (int i = 0; i < menuOptions.size(); i++) {
            System.out.printf("%d - %s%n", i + 1, menuOptions.get(i));
        }
    }

    private void displayNightEvents() {
        final var leftParty = gameManager.partyMonstersLeave();
        final var levelledUp = gameManager.partyMonstersLevelUp();
        final var monsterJoining = gameManager.monsterJoinsParty();
        System.out.printf("%nThe environment has been changed to a(n) %s environment!%n",
            gameManager.getEnvironment());
        if (leftParty != null) {
            System.out.printf("%s has expired...%n", leftParty.getName());
        }
        if (monsterJoining != null) {
            monsterJoinsPartyInterface(input(), monsterJoining);
        }
        for (final var mon : levelledUp) {
            System.out.printf("%s has levelled up from %d to %d!%n", mon.getName(), mon.getLevel() - 1, mon.getLevel());
        }
        System.out.println("Your monsters have healed!");
        System.out.println("The shop has restocked and there are new battles available!");
    }

    private void displayEndScreen() {
        System.out.printf("%s...%n", gameManager.getTrainer().getName());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return;
        }
        final var day = gameManager.getCurrentDay();
        if (day == gameManager.getMaxDays() + 1) {
            if (gameManager.getScore() == 0) {
                System.out.println("YOU CHEESED THE GAME%n");
            } else {
                System.out.printf("YOU SUCCEEDED IN %d DAYS%n%n", day - 1);
            }
        } else {
            System.out.printf("YOU FAILED IN %d DAYS%n", day);
            System.out.println("Try harder next time\n!");
        }
        System.out.printf("Max days: %d%n", gameManager.getMaxDays());
        System.out.printf("Final Gold: %d%n", gameManager.getGold());
        System.out.printf("Final score: %d%n", gameManager.getScore());
        System.out.println("\nFinal party:");
        for (Monster mon : gameManager.getTrainer().getParty()) {
            System.out.printf("%s - Level %d %s%n",
                mon.getName(), mon.getLevel(), mon.monsterType());
        }
        System.out.println("\nThanks for playing!");
    }

    public static Monster monsterJoinsPartyInterface(Scanner input, Monster mon) {
        monsterJoinsPartyMessage(mon);
        return monsterJoinsParty(input, mon);
    }

    public static Monster monsterJoinsParty(Scanner input, Monster mon) throws IllegalArgumentException {
        try {
            final var name = input.next();
            if (((name.length() >= 3) && (name.length() <= 15) && (name.matches("[a-zA-Z]+")))) {
                mon.setName(name);
            } else if (!name.matches("")) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException ignored) {
            System.out.println("Invalid name! (Must be between 3 and 15 letters inclusive, no symbols or numbers)");
            return monsterJoinsParty(input, mon);
        }
        return mon;
    }

    public static void monsterJoinsPartyMessage(Monster mon) {
        System.out.printf("Congratulations! %s has joined your party!", mon.getName());
        System.out.println("""

            Would you like to give it a name?\s
            (Must be between 3 and 15 letters inclusive, no symbols or numbers. Leave blank for default name):""");
    }

    public static void make(GameManager gameManager) {
        MainMenuCLI mainMenuCLI = new MainMenuCLI(gameManager);
        mainMenuCLI.mainMenuInterface();
    }


}
