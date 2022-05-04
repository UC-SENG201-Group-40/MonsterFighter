package seng.monsters.ui.cli;

import seng.monsters.model.GameManager;
import seng.monsters.model.Monster;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public final class MainMenuCLI extends TestableCLI {

    private final GameManager gameManager;
    private final List<String> menuOptions = List.of(
            "Manage Party", "Battle", "View Item Inventory", "Enter Shop", "Sleep");

    public MainMenuCLI(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void run() {
    }
    // TODO: Implement primary functionality of Main Menu


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
        mainMenuCLI.run();
    }


}
