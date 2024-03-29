package seng.monsters.ui.cli;

import seng.monsters.model.GameManager;
import seng.monsters.model.Monster;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * A CLI to the display the main menu and allow navigation to all other CLI's
 */
public final class MainMenuCLI extends TestableCLI {

    private final GameManager gameManager;
    private final List<String> menuOptions = List.of(
        "Manage Party", "Battle", "View Item Inventory", "Enter Shop", "Sleep");

    /**
     * Creates a CLI that display the main menu and allow navigation to all other CLI's
     *
     * @param gameManager The game manager / controller
     */
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
                    final boolean hasEnded = gameManager.nextDay();
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
        final Optional<Monster> leftParty = gameManager.partyMonstersLeave();
        final List<Monster> levelledUp = gameManager.partyMonstersLevelUp();
        final Optional<Monster> monsterJoining = gameManager.monsterJoinsParty();
        System.out.printf("%nThe environment has been changed to a(n) %s environment!%n",
            gameManager.getEnvironment());
        leftParty.ifPresent(monster -> System.out.printf("%s has expired...%n", monster.getName()));
        monsterJoining.ifPresent(monster -> monsterJoinsPartyInterface(input(), monster));
        for (final Monster mon : levelledUp) {
            System.out.printf("%s has levelled up from %d to %d!%n", mon.getName(), mon.getLevel() - 1, mon.getLevel());
        }
        System.out.println("Your monsters have healed!");
        System.out.println("The shop has restocked and there are new battles available!");
    }

    private void displayEndScreen() {
        System.out.printf("%s...%n", gameManager.getPlayer().getName());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return;
        }
        final int day = gameManager.getCurrentDay();
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
        for (Monster mon : gameManager.getPlayer().getParty()) {
            System.out.printf("%s - Level %d %s%n",
                mon.getName(), mon.getLevel(), mon.monsterType());
        }
        System.out.println("\nThanks for playing!");
    }

    /**
     * Creates a CLI display for monster that have joined a party
     *
     * @param input The Scanner to get the input from the user
     * @param mon   The monster that have joined the party
     * @return The monster itself
     */
    public static Monster monsterJoinsPartyInterface(Scanner input, Monster mon) {
        monsterJoinsPartyMessage(mon);
        return monsterJoinsParty(input, mon);
    }

    /**
     * The method to handle naming of a monster
     *
     * @param input The new name input
     * @param mon   The monster to be renamed
     * @return The monster itself
     * @throws IllegalArgumentException if the input contains non-alphabetical characters or not within 3-15 characters
     */
    public static Monster monsterJoinsParty(Scanner input, Monster mon) throws IllegalArgumentException {
        try {
            final String name = input.next();
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

    /**
     * Prints out a message for the monster joining the party
     *
     * @param mon The monster that has joined
     */
    public static void monsterJoinsPartyMessage(Monster mon) {
        System.out.printf("Congratulations! %s has joined your party!", mon.getName());
        System.out.println("""

            Would you like to give it a name?\s
            (Must be between 3 and 15 letters inclusive, no symbols or numbers. Leave blank for default name):""");
    }

    /**
     * Makes a MainMenuCLI and run its interface
     *
     * @param gameManager The game manager / controller
     */
    public static void make(GameManager gameManager) {
        MainMenuCLI mainMenuCLI = new MainMenuCLI(gameManager);
        mainMenuCLI.mainMenuInterface();
    }

}
