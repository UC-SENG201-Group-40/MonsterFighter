package seng.monsters.ui.cli;

import seng.monsters.model.*;

import java.util.List;
import java.util.Map;

/**
 * A CLI to buy items from the shop
 */
public final class ItemShopCLI extends ShopCLI {

    private final Inventory inventory;

    /**
     * Creates a CLI to allow buying and selling items from the shop
     * @param gameManager The game manager / controller
     */
    public ItemShopCLI(GameManager gameManager) {
        super(gameManager);
        inventory = gameManager.getInventory();
    }

    /**
     * Takes player input and attempts to buy the selected item.
     *
     * @param scannerInput The player's input as an int.
     */
    public void buyPurchasable(int scannerInput) throws IllegalArgumentException {
        try {
            final List<Item> items = Item.all();
            if ((scannerInput > 0) && (scannerInput < items.size() + 1)) {
                final Item item = items.get(scannerInput - 1);
                gameManager.buy(item);
                buyPurchasableInterface(item);
            } else if (scannerInput != 0) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input!");
            buyPurchasable(input().nextInt());
        } catch (Shop.NotInStockException e) {
            System.out.println("There are no more of that item left!");
            buyPurchasable(input().nextInt());
        } catch (Shop.InsufficientFundsException e) {
            System.out.println("You're too poor! Come back when you're a little, mmmm... RICHER!");
            buyPurchasable(input().nextInt());
        }
    }

    /**
     * Takes player input and attempts to sell the selected item.
     *
     * @param scannerInput The player's input as an int.
     */
    public void sellPurchasable(int scannerInput) throws IllegalArgumentException {
        try {
            if ((scannerInput > 0) && (scannerInput < 5)) {
                final Item item = Item.all().get(scannerInput - 1);
                gameManager.sell(item);
                sellPurchasableInterface(item);
            } else if (scannerInput != 0) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input!");
            sellPurchasable(input().nextInt());
        } catch (Inventory.ItemNotExistException e) {
            System.out.println("You don't have any of that item!");
            sellPurchasable(input().nextInt());
        }
    }

    /**
     * Prints the item options to buy.
     *
     * @param boughtItem The last item bought, null otherwise.
     */
    public void displayBuyPurchasableOptions(Purchasable boughtItem) {
        System.out.println("\n===========================\n");
        if (boughtItem != null) {
            System.out.printf("%s bought!\n", boughtItem.getName());
        }
        System.out.printf("Gold: %d\n", gameManager.getGold());
        System.out.println("Select an item to buy:");
        final List<Map.Entry<Item, Integer>> items = shop.getItemsStock();
        for (int i = 0; i < items.size(); i++) {
            final Map.Entry<Item, Integer> item = items.get(i);
            System.out.printf("%d - %s (Stock: %d, Price: %d)%n",
                i + 1, item.getKey().getName(), item.getValue(), item.getKey().buyPrice());

            System.out.printf("    %s%n%n", item.getKey().description());
        }
        System.out.println("\n0 - Cancel");
    }

    /**
     * Prints the item options to sell.
     *
     * @param soldItem The last item sold, null otherwise.
     */
    public void displaySellPurchasableOptions(Purchasable soldItem) {
        System.out.println("\n===========================\n");
        if (soldItem != null) {
            System.out.printf("%s sold!\n", soldItem.getName());
        }
        System.out.printf("Gold: %d\n", gameManager.getGold());
        System.out.println("Select an item to sell:");
        final List<Item> items = Item.all();
        for (int i = 0; i < items.size(); i++) {
            final Item item = items.get(i);
            System.out.printf("%d - %s (Stock: %d, Sell Price: %d)%n",
                i + 1, item.getName(), inventory.getItemNumber(item), item.sellPrice());
        }
        System.out.println("\n0 - Cancel");
    }

    /**
     * Makes a ItemShopCli and run its interface
     * @param gameManager The game manager / controller
     */
    public static void make(GameManager gameManager) {
        ItemShopCLI itemShopCLI = new ItemShopCLI(gameManager);
        itemShopCLI.shopTypeInterface("items");
    }
}
