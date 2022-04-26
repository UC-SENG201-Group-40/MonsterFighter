package seng.monsters.ui.cli;

import seng.monsters.model.GameManager;
import seng.monsters.model.Inventory;
import seng.monsters.model.Item;
import seng.monsters.model.Monster;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public final class InventoryCLI {
    private final GameManager gameManager;
    private final Inventory inventory;
    private final List<Monster> party;
    private final Scanner input = new Scanner(System.in);

    ArrayList<Item> itemsReference = new ArrayList<>();

    public InventoryCLI(GameManager gameManager) {
        this.gameManager = gameManager;
        inventory = gameManager.getInventory();
        party = gameManager.getTrainer().getParty();

        itemsReference.add(new Item.Potion());
        itemsReference.add(new Item.Revive());
        itemsReference.add(new Item.RareCandy());
    }

    /**
     * Prints the player's inventory and takes the player's input to choose which item to use.
     *
     * @throws IllegalArgumentException if an invalid parameter is passed.
     */
    public void inventoryInterface() throws IllegalArgumentException {
        displayInventoryOptions();
        while (true) {
            try {
                selectItem(input.nextInt());
                return;
            }
            catch (InputMismatchException e) {
                input.next();
                System.out.println("Invalid input!"); }
        }
    }

    /**
     * Prints the player's party and takes the player's input to choose which monster to use the item on.
     *
     * @throws IllegalArgumentException if an invalid parameter is passed.
     */
    public void useItemInterface(Item item, boolean itemUsed) throws IllegalArgumentException {
        displayUseItemOptions(item, itemUsed);
        while (true) {
            try {
                useItemOnMonster(item, input.nextInt());
                return;
            }
            catch (InputMismatchException e) {
                input.next();
                System.out.println("Invalid input!"); }
        }
    }

    /**
     * Takes the player's input and chooses which item menu to navigate to or to return to the main menu
     *
     * @param scannerInput The player input.
     * @throws IllegalArgumentException if an invalid parameter is passed.
     */
    private void selectItem(int scannerInput) throws IllegalArgumentException {
        try {
            if ((scannerInput > 0) && (scannerInput < 4)) {
                var item = itemsReference.get(scannerInput - 1);
                useItemInterface(item, false);
                inventoryInterface();
            } else if (scannerInput != 0) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input!");
            selectItem(input.nextInt());
        }
    }

    /**
     * Takes the player's input and chooses which monster to use the item on, if possible.
     *
     * @param item         The item that is wanting to be used on a monster.
     * @param scannerInput The player's input.
     * @throws IllegalArgumentException if an invalid parameter is passed.
     */
    private void useItemOnMonster(Item item, int scannerInput) throws IllegalArgumentException {
        try {
            if ((scannerInput > 0) && (scannerInput < party.size() + 1)) {
                gameManager.useItemFromInventory(item, scannerInput - 1);
                useItemInterface(item, true);
            } else if (scannerInput != 0) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input!");
            useItemOnMonster(item, input.nextInt());
        } catch (Item.NoEffectException e) {
            System.out.println("That item has no effect on this monster!");
            useItemOnMonster(item, input.nextInt());
        } catch (Inventory.ItemNotExistException e) {
            System.out.println("You don't have any of that item!");
            useItemOnMonster(item, input.nextInt());
        }
    }

    /**
     * Prints the player's inventory to output.
     */
    private void displayInventoryOptions() {
        System.out.println("\n===========================\n");
        System.out.println("Here is your inventory. Select an item to use, or return to the main menu:");
        for (int i = 0; i < itemsReference.size(); i++) {
            var item = itemsReference.get(i);
            System.out.printf("%d - %s (Stock: %d)%n", i + 1, item.getName(), inventory.getItemNumber(item));
        }
        System.out.println("\n0 - Return to Main Menu");
    }

    /**
     * Prints the item and the monsters in the party that it could potentially be used on.
     *
     * @param item     The item that is desired to be used.
     * @param itemUsed A boolean for if this method is being used after an item has been used.
     */
    private void displayUseItemOptions(Item item, boolean itemUsed) {
        System.out.println("\n===========================\n");
        if (itemUsed) {
            System.out.printf("%s successfully used!\n", item.getName());
        }
        System.out.printf("You have %d %s(s). Select a monster to use one on, or return to the inventory menu:%n"
            , inventory.getItemNumber(item), item.getName());
        for (int i = 0; i < party.size(); i++) {
            var mon = party.get(i);
            System.out.printf("%d - %s (Level %d, %dHp/%dHp)\n", i + 1, mon.getName(), mon.getLevel(), mon.getCurrentHp(), mon.maxHp());
        }
        System.out.println("\n0 - Return to Inventory menu");
    }

    public static void make(GameManager gameManager) {
        try {
            final var inventoryCLI = new InventoryCLI(gameManager);
            inventoryCLI.inventoryInterface();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}