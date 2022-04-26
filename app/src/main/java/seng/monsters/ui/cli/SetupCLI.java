package seng.monsters.ui.cli;

import seng.monsters.model.GameManager;
import seng.monsters.model.Monster;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

//TODO: Needs more testing

public class SetupCLI {

    private final ArrayList<Monster> starterMonsters = new ArrayList<>();
    private final List<String> difficulties = List.of("Normal", "Hard", "Utterly Impossible");
    private final Scanner input = new Scanner(System.in);

    public SetupCLI() {
        starterMonsters.add(new Monster.Quacker(1));
        starterMonsters.add(new Monster.Raver(1));
        starterMonsters.add(new Monster.Tree(1));
    }

    public void setup() {
        String name = chooseNameInterface();
        int maxDays = chooseMaxDaysInterface();
        int difficulty = selectDifficultyInterface();
        GameManager gameManager = new GameManager(0, 1, maxDays, difficulty, name);
        Monster mon = selectStartingMonsterInterface();
        System.out.printf("\n%s, Your adventure has begun!", name);
        gameManager.getTrainer().add(mon);
        MainMenuCLI.make(gameManager);
    }

    public String chooseNameInterface() throws IllegalArgumentException {
        welcomeMessage();
        while (true) {
            try { return chooseName(input.nextLine()); }
            catch (InputMismatchException e) {
                input.next();
                System.out.println("Invalid name! (Must be between 3 and 15 letters inclusive, no symbols or numbers)"); }
        }
    }

    public int chooseMaxDaysInterface() throws IllegalArgumentException {
        dayNumberMessage();
        while (true) {
            try { return chooseMaxDays(input.nextInt()); }
            catch (InputMismatchException e) {
                input.next();
                System.out.println("Invalid input! (Must be a number between 5 and 15 inclusive)");}
        }
    }

    public int selectDifficultyInterface() throws IllegalArgumentException {
        displayDifficulties();
        while (true) {
            try { return selectDifficulty(input.nextInt()); }
            catch (InputMismatchException e) {
                input.next();
                System.out.println("Invalid input!"); }
        }
    }

    public Monster selectStartingMonsterInterface() throws IllegalArgumentException {
        displayStartingMonsters();
        while (true) {
            try { return selectStartingMonster(input.nextInt()); }
            catch (InputMismatchException e) {
                input.next();
                System.out.println("Invalid input!"); }
        }
    }


    public String chooseName(String scannerInput) throws IllegalArgumentException {
        String name = "";
        try {
            if ((scannerInput.length() >= 3) && (scannerInput.length() <= 15) && (scannerInput.matches("[a-zA-Z]+"))) {
                name = scannerInput;
                System.out.printf("\nName \"%s\" chosen.", name);
            }
            else { throw new IllegalArgumentException(); }
        }
        catch (IllegalArgumentException e) {
            System.out.println("Invalid name! (Must be between 3 and 15 letters inclusive, no symbols or numbers)");
            chooseName(input.nextLine());
        }
        return name;
    }

    public int chooseMaxDays(int scannerInput) throws IllegalArgumentException {
        int dayNumber = 0;
        try {
            if ((scannerInput >= 5) && (scannerInput <= 15)) {
                dayNumber = scannerInput;
                System.out.printf("\n%d days chosen.", dayNumber);
            }
            else { throw new IllegalArgumentException(); }
        }
        catch (IllegalArgumentException e) {
            System.out.println("Invalid input! (Must be a number between 5 and 15 inclusive)");
            chooseMaxDays(input.nextInt());
        }
        return dayNumber;
    }

    public int selectDifficulty(int scannerInput) throws IllegalArgumentException {
        int difficulty = 0;
        try {
            if ((scannerInput > 0) && (scannerInput < 4)) {
                difficulty = scannerInput;
                System.out.printf("\n%s chosen.", difficulties.get(scannerInput-1));
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input!");
            selectDifficulty(input.nextInt());
        }
        return difficulty;
    }


    public Monster selectStartingMonster(int scannerInput) {
        Monster mon = null;
        try {
            if ((scannerInput > 0) && (scannerInput < 4)) {
                mon = MainMenuCLI.monsterJoinsPartyInterface(starterMonsters.get(scannerInput-1));
            }
            else { throw new IllegalArgumentException(); }
        }
        catch (IllegalArgumentException e) {
            System.out.println("Invalid input!");
            selectStartingMonster(input.nextInt());
        }
        return mon;
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
            var mon = starterMonsters.get(i);
            System.out.printf("\n%d - %s (Level %d)\n", i + 1, mon.getName(), mon.getLevel());
            System.out.printf("Max Hp: %dHp\n", mon.maxHp());
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