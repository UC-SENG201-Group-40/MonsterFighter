package seng.monsters.ui.cli;

import seng.monsters.model.GameManager;
import seng.monsters.model.Monster;

import java.util.InputMismatchException;
import java.util.List;

/**
 * A CLI to prompt user with the setting for their game
 */
public final class SetupCLI extends TestableCLI {

    private final List<Monster> starterMonsters = List.of(
        new Monster.Quacker(3),
        new Monster.Raver(3),
        new Monster.Tree(3)
    );
    private final List<String> difficulties = List.of("Normal", "Hard", "Utterly Impossible");

    /**
     * Runs each setup interface method and makes the main menu CLI
     * out of the returned values.
     */
    public void setup() {
        final String name = chooseNameInterface();
        final int maxDays = chooseMaxDaysInterface();
        final int difficulty = selectDifficultyInterface();
        final GameManager gameManager = new GameManager(0, 1, maxDays, difficulty, name);
        final Monster mon = selectStartingMonsterInterface();
        System.out.printf("\n%s, Your adventure has begun!", name);
        gameManager.getTrainer().add(mon);
        MainMenuCLI.make(gameManager);
    }

    /**
     * Prints a message introducing the player to the game and prompts them for their name
     * and takes the player's input as their name.
     *
     * @return The player's name as a string.
     */
    public String chooseNameInterface() {
        welcomeMessage();
        while (true) {
            try {
                return chooseName(input().next());
            } catch (InputMismatchException e) {
                input().next();
                System.out.println("Invalid name! (Must be between 3 and 15 letters inclusive, no symbols or numbers)");
            }
        }
    }

    /**
     * Prints a message prompting the player to enter the number of days they want to play
     * and takes the player input as that.
     *
     * @return The maximum number of days to play as an integer.
     */
    public int chooseMaxDaysInterface() {
        dayNumberMessage();
        while (true) {
            try {
                return chooseMaxDays(input().nextInt());
            } catch (InputMismatchException e) {
                input().next();
                System.out.println("Invalid input! (Must be a number between 5 and 15 inclusive)");
            }
        }
    }

    /**
     * Prints difficulty options and takes player input to choose a difficulty.
     *
     * @return The difficulty as an integer multiplier.
     */
    public int selectDifficultyInterface() {
        displayDifficulties();
        while (true) {
            try {
                return selectDifficulty(input().nextInt());
            } catch (InputMismatchException e) {
                input().next();
                System.out.println("Invalid input!");
            }
        }
    }

    /**
     * Prints the starting monster choices and takes player input to pick a starting monster.
     *
     * @return The player's starting monster.
     */
    public Monster selectStartingMonsterInterface() {
        displayStartingMonsters();
        while (true) {
            try {
                return selectStartingMonster(input().nextInt());
            } catch (InputMismatchException e) {
                input().next();
                System.out.println("Invalid input!");
            }
        }
    }

    /**
     * Takes the player's input as their name.
     *
     * @param scannerInput The player's input as a string.
     * @return The player's name as a string.
     * @throws IllegalArgumentException if an invalid input is passed.
     */
    public String chooseName(String scannerInput) throws IllegalArgumentException {
        try {
            if ((scannerInput.length() >= 3) && (scannerInput.length() <= 15)
                && (scannerInput.matches("[a-zA-Z]+"))) {
                System.out.printf("\nName \"%s\" chosen.", scannerInput);
                return scannerInput;
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid name! (Must be between 3 and 15 letters inclusive, no symbols or numbers)");
            return chooseName(input().next());
        }
    }

    /**
     * Takes the player's input as the number of maximum days to play.
     *
     * @param scannerInput The player's input as an int.
     * @return The maximum number of days to play as an int.
     * @throws IllegalArgumentException if an invalid input is passed.
     */
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
            return chooseMaxDays(input().nextInt());
        }
    }

    /**
     * Takes the player's input and chooses the difficulty accordingly.
     *
     * @param scannerInput The player's input as an int.
     * @return The difficulty as an integer multiplier.
     * @throws IllegalArgumentException if an invalid input is passed.
     */
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
            return selectDifficulty(input().nextInt());
        }
    }

    /**
     * Takes player input and chooses a starting monster accordingly.
     *
     * @param scannerInput The player's input as an integer.
     * @return The selected starting monster.
     * @throws IllegalArgumentException if an invalid input is passed.
     */
    public Monster selectStartingMonster(int scannerInput) throws IllegalArgumentException {
        try {
            if ((scannerInput > 0) && (scannerInput < 4)) {
                return MainMenuCLI.monsterJoinsPartyInterface(input(), starterMonsters.get(scannerInput - 1));
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input!");
            return selectStartingMonster(input().nextInt());
        }
    }

    /**
     * Prints out a message introducing the game and prompting the player to choose a name.
     */
    public void welcomeMessage() {
        System.out.println("\n===========================\n");
        System.out.println("Welcome to Monster Fighter!");
        System.out.println("Choose a name (Must be between 3 and 15 letters inclusive, no symbols or numbers):");
    }

    /**
     * Prints out a message prompting the player to choose a number of days to play.
     */
    public void dayNumberMessage() {
        System.out.println("\n===========================\n");
        System.out.println("Choose a number of days to play (Must be between 5 and 15 inclusive):");
    }

    /**
     * Prints out the difficulty choices.
     */
    public void displayDifficulties() {
        System.out.println("\n===========================\n");
        System.out.println("Select a difficulty (1-3):");
        for (int i = 0; i < difficulties.size(); i++) {
            System.out.printf("%d - %s\n", i + 1, difficulties.get(i));
        }
    }

    /**
     * Prints out the starting monster choices.
     */
    public void displayStartingMonsters() {
        System.out.println("\n===========================\n");
        System.out.println("Select a starting monster (1-3):");
        for (int i = 0; i < starterMonsters.size(); i++) {
            final Monster mon = starterMonsters.get(i);
            System.out.printf("\n%d - %s (Level %d)\n", i + 1, mon.getName(), mon.getLevel());
            System.out.printf("Max Hp: %d HP\n", mon.maxHp());
            System.out.printf("Overnight Heal Rate: %d\n", mon.healRate());
            System.out.printf("Attack Damage: %d\n", mon.scaledDamage());
            System.out.printf("Speed: %d\n", mon.speed());
            System.out.printf("Ideal Environment: %s\n", mon.idealEnvironment());
        }
    }

    /**
     * Makes a SetupCLI and run its interface
     */
    public static void make() {
        try {
            final SetupCLI setupCLI = new SetupCLI();
            setupCLI.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}