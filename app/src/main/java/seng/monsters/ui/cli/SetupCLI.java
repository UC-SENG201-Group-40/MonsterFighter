package seng.monsters.ui.cli;

import seng.monsters.model.GameManager;
import seng.monsters.model.Monster;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

// TODO: Needs more testing

public class SetupCLI {

    private final List<Monster> starterMonsters = List.of(
        new Monster.Quacker(1),
        new Monster.Raver(1),
        new Monster.Tree(1)
    );
    private final List<String> difficulties = List.of("Normal", "Hard", "Utterly Impossible");
    private final Scanner input = new Scanner(System.in);


    public void setup() {
        final var name = chooseNameInterface();
        final var maxDays = chooseMaxDaysInterface();
        final var difficulty = selectDifficultyInterface();
        final var gameManager = new GameManager(0, 1, maxDays, difficulty, name);
        final var mon = selectStartingMonsterInterface();
        System.out.printf("\n%s, Your adventure has begun!", name);
        gameManager.getTrainer().add(mon);
        MainMenuCLI.make(gameManager);
    }

    public String chooseNameInterface() throws IllegalArgumentException {
        welcomeMessage();
        while (true) {
            try {
                return chooseName(input.nextLine());
            } catch (InputMismatchException e) {
                input.next();
                System.out.println("Invalid name! (Must be between 3 and 15 letters inclusive, no symbols or numbers)");
            }
        }
    }

    public int chooseMaxDaysInterface() throws IllegalArgumentException {
        dayNumberMessage();
        while (true) {
            try {
                return chooseMaxDays(input.nextInt());
            } catch (InputMismatchException e) {
                input.next();
                System.out.println("Invalid input! (Must be a number between 5 and 15 inclusive)");
            }
        }
    }

    public int selectDifficultyInterface() throws IllegalArgumentException {
        displayDifficulties();
        while (true) {
            try {
                return selectDifficulty(input.nextInt());
            } catch (InputMismatchException e) {
                input.next();
                System.out.println("Invalid input!");
            }
        }
    }

    public Monster selectStartingMonsterInterface() throws IllegalArgumentException {
        displayStartingMonsters();
        while (true) {
            try {
                return selectStartingMonster(input.nextInt());
            } catch (InputMismatchException e) {
                input.next();
                System.out.println("Invalid input!");
            }
        }
    }


    public String chooseName(String scannerInput) throws IllegalArgumentException {
        try {
            if ((scannerInput.length() >= 3) && (scannerInput.length() <= 15) && (scannerInput.matches("[a-zA-Z]+"))) {
                System.out.printf("\nName \"%s\" chosen.", scannerInput);
                return scannerInput;
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid name! (Must be between 3 and 15 letters inclusive, no symbols or numbers)");
            return chooseName(input.nextLine());
        }
    }

    public int chooseMaxDays(int scannerInput) throws IllegalArgumentException {
        try {
            if ((scannerInput >= 5) && (scannerInput <= 15)) {
                System.out.printf("\n%d days chosen.", scannerInput);
                return scannerInput;
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input! (Must be a number between 5 and 15 inclusive)");
            return chooseMaxDays(input.nextInt());
        }
    }

    public int selectDifficulty(int scannerInput) throws IllegalArgumentException {
        try {
            if ((scannerInput > 0) && (scannerInput < 4)) {
                System.out.printf("\n%s chosen.", difficulties.get(scannerInput - 1));
                return scannerInput;
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input!");
            return selectDifficulty(input.nextInt());
        }
    }


    public Monster selectStartingMonster(int scannerInput) {
        try {
            if ((scannerInput > 0) && (scannerInput < 4)) {
                return MainMenuCLI.monsterJoinsPartyInterface(starterMonsters.get(scannerInput - 1));
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input!");
            return selectStartingMonster(input.nextInt());
        }
    }

    public void welcomeMessage() {
        System.out.println("\n===========================\n");
        System.out.println("Welcome to Monster Fighter!");
        System.out.println("Choose a name (Must be between 3 and 15 letters inclusive, no symbols or numbers):");
    }

    public void dayNumberMessage() {
        System.out.println("\n===========================\n");
        System.out.println("Choose a number of days to play (Must be between 5 and 15 inclusive):");
    }

    public void displayDifficulties() {
        System.out.println("\n===========================\n");
        System.out.println("Select a difficulty (1-3):");
        for (int i = 0; i < difficulties.size(); i++) {
            System.out.printf("%d - %s\n", i + 1, difficulties.get(i));
        }
    }

    public void displayStartingMonsters() {
        System.out.println("\n===========================\n");
        System.out.println("Select a starting monster (1-3):");
        for (int i = 0; i < starterMonsters.size(); i++) {
            final var mon = starterMonsters.get(i);
            System.out.printf("\n%d - %s (Level %d)\n", i + 1, mon.getName(), mon.getLevel());
            System.out.printf("Max Hp: %d HP\n", mon.maxHp());
            System.out.printf("Overnight Heal Rate: %d\n", mon.healRate());
            System.out.printf("Attack Damage: %d\n", mon.scaledDamage());
            System.out.printf("Speed: %d\n", mon.speed());
            System.out.printf("Ideal Environment: %s\n", mon.idealEnvironment());
        }
    }

    public static void make() {
        try {
            final var setupCLI = new SetupCLI();
            setupCLI.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}