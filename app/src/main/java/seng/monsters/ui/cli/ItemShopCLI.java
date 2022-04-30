package seng.monsters.ui.cli;

import seng.monsters.model.GameManager;
import seng.monsters.model.Shop;
import seng.monsters.model.Inventory;
import seng.monsters.model.Item;

import java.util.InputMismatchException;

public class ItemShopCLI extends TestableCLI {

    private final GameManager gameManager;
    private final Shop shop;
    private final Inventory inventory;

    public ItemShopCLI(GameManager gameManager) {
        this.gameManager = gameManager;
        shop = gameManager.getShop();
        inventory = gameManager.getInventory();
    }

    private void shopTypeInterface() {
        displayShopTypes();
        selectShopType(input().nextInt());
    }

    private void buyItemInterface(Item item, boolean wasItemBought) {
        displayItemBuy(item, wasItemBought);
        while (true) {
            try {
                buyItem(input().nextInt());
                return;
            } catch (InputMismatchException e) {
                input().next();
                System.out.println("Invalid input!");
            }
        }
    }

    private void sellItemInterface(Item item, boolean wasItemSold) {
        displayItemSell(item, wasItemSold);
        while (true) {
            try {
                sellItem(input().nextInt());
                return;
            } catch (InputMismatchException e) {
                input().next();
                System.out.println("Invalid input!");
            }
        }
    }

    private void selectShopType(int scannerInput) throws IllegalArgumentException {
        try {
            switch (scannerInput) {
                case 1:
                    buyItemInterface(null, false);
                    displayShopTypes();
                case 2:
                    sellItemInterface(null, false);
                    displayShopTypes();
                case 0:
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input!");
            selectShopType(input().nextInt());
        }
    }

    private void buyItem(int scannerInput) throws IllegalArgumentException {
        try {
            if ((scannerInput > 0) && (scannerInput < 4)) {
                final var item = Item.all().get(scannerInput - 1);
                gameManager.buy(item);
                buyItemInterface(item, true);
            } else if (scannerInput != 0) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input!");
            buyItem(input().nextInt());
        } catch (Shop.NotInStockException e) {
            System.out.println("There are no more of that item left!");
            buyItem(input().nextInt());
        } catch (Shop.InsufficientFundsException e) {
            System.out.println("You're too poor! Come back when you're a little, mmmm... RICHER!");
            buyItem(input().nextInt());
        }
    }

    private void sellItem(int scannerInput) throws IllegalArgumentException {
        try {
            if ((scannerInput > 0) && (scannerInput < 4)) {
                final var item = Item.all().get(scannerInput - 1);
                gameManager.sell(item);
                sellItemInterface(item, true);
            } else if (scannerInput != 0) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input!");
            sellItem(input().nextInt());
        } catch (Inventory.ItemNotExistException e) {
            System.out.println("You don't have any of that item!");
            sellItem(input().nextInt());
        }
    }

    private void displayShopTypes() {
        System.out.println("\n===========================\n");
        System.out.printf("Gold: %d\n", gameManager.getGold());
        System.out.println("Would you like to buy or sell items?");
        System.out.println("1 - Buy Items");
        System.out.println("2 - Sell Items");
        System.out.println("\n0 - Return to shop menu");
    }

    private void displayItemBuy(Item boughtItem, boolean wasItemBought) {
        System.out.println("\n===========================\n");
        if (wasItemBought) {
            System.out.printf("%n%s bought!", boughtItem.getName());
        }
        System.out.printf("Gold: %d\n", gameManager.getGold());
        System.out.println("Select an item to buy:");
        final var items = shop.getItemStock();
        for (var i = 0; i < items.size(); i++) {
            final var item = items.get(i);
            System.out.printf("%d - %s (Stock: %d)%n", i + 1, item.getKey(), item.getValue());
        }
        System.out.println("\n0 - Cancel");
    }

    private void displayItemSell(Item soldItem, boolean wasItemSold) {
        System.out.println("\n===========================\n");
        if (wasItemSold) {
            System.out.printf("%n%s sold!", soldItem.getName());
        }
        System.out.printf("Gold: %d\n", gameManager.getGold());
        System.out.println("Select an item to sell:");
        final var items = Item.all();
        for (var i = 0; i < items.size(); i++) {
            final var item = items.get(i);
            System.out.printf("%d - %s (Stock: %d, Sell Price: %d)%n", i + 1, item.getName(), inventory.getItemNumber(item), item.sellPrice());
        }
        System.out.println("\n0 - Cancel");
    }

    public static void make(GameManager gameManager) {
        ItemShopCLI itemShopCLI = new ItemShopCLI(gameManager);
        itemShopCLI.shopTypeInterface();
    }
}
