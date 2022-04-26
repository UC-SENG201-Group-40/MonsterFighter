package seng.monsters.ui.cli;

import seng.monsters.model.GameManager;
import seng.monsters.model.Monster;

import java.util.List;
import java.util.Scanner;

public class MainMenuCLI {

    private final GameManager gameManager;
    private final List<String> menuOptions = List.of("Manage Party", "Battle", "View Item Inventory", "Enter Shop", "Sleep");
    private final Scanner input = new Scanner(System.in);

    public MainMenuCLI(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void run() {
    }
    // TODO: Implement primary functionality of Main Menu








    public static Monster monsterJoinsPartyInterface(Monster mon) {
        monsterJoinsPartyMessage(mon);
        monsterJoinsParty(mon);
        return mon;
    }

    public static Monster monsterJoinsParty(Monster mon) throws IllegalArgumentException {
        Scanner input = new Scanner(System.in);
        try {
            String name = input.nextLine();
            if (((name.length() >= 3) && (name.length() <= 15) && (name.matches("[a-zA-Z]+")))) {
                mon.setName(name);
            }
            else if (!name.matches("")) { throw new IllegalArgumentException(); }
        }
        catch (IllegalArgumentException ignored) {
            System.out.println("Invalid name! (Must be between 3 and 15 letters inclusive, no symbols or numbers)");
            monsterJoinsParty(mon);
        }
        return mon;
    }

    public static void monsterJoinsPartyMessage(Monster mon) {
        System.out.printf("Congratulations! %s has joined your party!", mon.getName());
        System.out.println("\nWould you like to give it a name? \n(Must be between 3 and 15 letters inclusive, no symbols or numbers. Leave blank for default name):");
    }

    public static void make(GameManager gameManager) {
        MainMenuCLI mainMenuCLI = new MainMenuCLI(gameManager);
        mainMenuCLI.run();
    }


}
