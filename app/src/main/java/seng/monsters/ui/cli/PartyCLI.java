package seng.monsters.ui.cli;

import seng.monsters.model.GameManager;
import seng.monsters.model.Monster;

import java.util.InputMismatchException;
import java.util.List;

/**
 * A CLI to display the player's party and allow switching monster
 */
public final class PartyCLI extends TestableCLI {
    private final GameManager gameManager;
    private final List<Monster> party;

    /**
     * Creates a CLI to display the monster party
     * @param gameManager The game manager / controller
     */
    public PartyCLI(GameManager gameManager) {
        super();
        this.gameManager = gameManager;
        this.party = gameManager.getPlayer().getParty();
    }

    /**
     * Prints the player's party and monster stats and takes the player's input to choose which monster to move.
     *
     * @param monsterMoved Flagged if a monster had been moved in the previously used method.
     * @throws IllegalArgumentException if an invalid parameter is passed.
     */
    public void partyStatsInterface(boolean monsterMoved) throws IllegalArgumentException {
        displayPartyStats(monsterMoved);
        while (true) {
            try {
                selectMonsterToMove(input().nextInt());
                return;
            } catch (InputMismatchException e) {
                input().next();
                System.out.println("Invalid input!");
            }
        }
    }

    /**
     * Prints the player's party and takes the player's input to choose which monster to swap with.
     *
     * @param mon The monster that is to be swapped.
     * @return A boolean that flags if a monster had been swapped.
     */
    public boolean moveMonsterInterface(Monster mon) {
        displayMoveMonsters(mon);
        while (true) {
            try {
                return selectMonsterToSwap(mon, input().nextInt());
            } catch (InputMismatchException e) {
                input().next();
                System.out.println("Invalid input!");
            }
        }
    }

    /**
     * Takes the player's input and chooses the first monster to swap.
     *
     * @param scannerInput The player's input as an int.
     * @throws IllegalArgumentException  if an invalid parameter is passed.
     * @throws IndexOutOfBoundsException if a valid parameter is passed, but there is no monster in that party position.
     */
    public void selectMonsterToMove(int scannerInput) throws IllegalArgumentException, IndexOutOfBoundsException {
        try {
            if ((scannerInput > 0) && (scannerInput < 5)) {
                final Monster mon = party.get(scannerInput - 1);
                final boolean monsterMoved = moveMonsterInterface(mon);
                partyStatsInterface(monsterMoved);
            } else if (scannerInput != 0) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException | InputMismatchException e) {
            System.out.println("Invalid input!");
            selectMonsterToMove(input().nextInt());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No monster in that position!");
            selectMonsterToMove(input().nextInt());
        }
    }

    /**
     * Takes the player's input and attempts to swap the first monster with the second monster (chosen via input).
     *
     * @param mon          The monster that is to be swapped.
     * @param scannerInput The player's input as an int.
     * @return A boolean that flags if a monster had been swapped.
     * @throws IllegalArgumentException  if an invalid parameter is passed.
     * @throws IndexOutOfBoundsException if a valid parameter is passed, but there is no monster in that party position.
     */
    public boolean selectMonsterToSwap(Monster mon, int scannerInput)
        throws IllegalArgumentException, IndexOutOfBoundsException {
        try {
            if ((scannerInput > 0) && (scannerInput < 5)) {
                gameManager.switchMonsterOnParty(mon, scannerInput - 1);
                return true;
            } else if (scannerInput != 0) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException | InputMismatchException e) {
            System.out.println("Invalid input!");
            return selectMonsterToSwap(mon, input().nextInt());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No monster in that position!");
            return selectMonsterToSwap(mon, input().nextInt());
        }
        return false;
    }

    /**
     * Prints the party of monsters and their stats.
     *
     * @param monsterMoved Flagged if a monster was moved in the previous method.
     */
    public void displayPartyStats(boolean monsterMoved) {
        System.out.println("\n===========================\n");
        if (monsterMoved) {
            System.out.println("Monsters successfully swapped!");
        }
        System.out.println("Here is your party. Select a monster to move, or return to the main menu:");
        for (int i = 0; i < party.size(); i++) {
            final Monster mon = party.get(i);
            System.out.printf("\n%d - %s (Level %d, %d/%d HP)\n",
                i + 1, mon.getName(), mon.getLevel(), mon.getCurrentHp(), mon.maxHp());
            System.out.printf("Monster Type: %s\n", mon.monsterType());
            System.out.printf("Sell Price: %d Gold\n", mon.sellPrice());
            System.out.printf("Attack Damage: %d\n", mon.scaledDamage());
            System.out.printf("Speed: %d\n", mon.speed());
            System.out.printf("Overnight Heal Rate: %d\n", mon.healRate());
            System.out.printf("Ideal Environment: %s\n", mon.idealEnvironment());
        }
        System.out.println("\n0 - Return to main menu");
    }

    /**
     * Prints the party of monsters without stats for readability.
     *
     * @param mon The monster to be swapped.
     */
    public void displayMoveMonsters(Monster mon) {
        System.out.println("\n===========================\n");
        System.out.printf("Which monster would you like to swap %s with?\n", mon.getName());
        for (int i = 0; i < party.size(); i++) {
            final Monster swapMon = party.get(i);
            System.out.printf("%d - %s\n", i + 1, swapMon.getName());
        }
        System.out.println("\n0 - Cancel");
    }

    /**
     * Makes a PartyCLI and run its interface
     * @param gameManager The game manager / controller
     */
    public static void make(GameManager gameManager) {
        try {
            final PartyCLI partyCLI = new PartyCLI(gameManager);
            partyCLI.partyStatsInterface(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}