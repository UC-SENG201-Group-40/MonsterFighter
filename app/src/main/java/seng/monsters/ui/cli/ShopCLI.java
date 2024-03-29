package seng.monsters.ui.cli;

import seng.monsters.model.GameManager;
import seng.monsters.model.Monster;
import seng.monsters.model.Purchasable;
import seng.monsters.model.Shop;

import java.util.InputMismatchException;
import java.util.List;

/**
 * A base CLI for a shop
 */
public abstract class ShopCLI extends TestableCLI {

    /**
     * The game manager / controller used by this CLI
     */
    public final GameManager gameManager;
    /**
     * The shop itself
     */
    public final Shop shop;

    /**
     * The list of monsters in the players party
     */
    public final List<Monster> party;

    /**
     * Creates a base CLI for handling all shop operation
     * @param gameManager The game manager / controller
     */
    public ShopCLI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.shop = gameManager.getShop();
        this.party = gameManager.getPlayer().getParty();
    }

    /**
     * Prints buy and sell shop options to output and selects one based on player input.
     * Type of purchasables depends on shop type.
     *
     * @param shopType The string "items" or "monsters" depending on the type of shop.
     */
    public void shopTypeInterface(String shopType) {
        displayShopOptions(shopType);
        while (true) {
            try {
                selectShopType(input().nextInt(), shopType);
                return;
            } catch (InputMismatchException e) {
                input().next();
                System.out.println("Invalid input!");
            }
        }
    }

    /**
     * Prints the purchasable options to buy and attempts to buy purchasables based on player input.
     *
     * @param purchasable The last purchasable bought, null otherwise.
     */
    public void buyPurchasableInterface(Purchasable purchasable) {
        displayBuyPurchasableOptions(purchasable);
        while (true) {
            try {
                buyPurchasable(input().nextInt());
                return;
            } catch (InputMismatchException e) {
                input().next();
                System.out.println("Invalid input!");
            }
        }
    }

    /**
     * Prints the purchasable options to sell and attempts to sell purchasables based on player input.
     *
     * @param purchasable The last purchasable sold, null otherwise.
     */
    public void sellPurchasableInterface(Purchasable purchasable) {
        displaySellPurchasableOptions(purchasable);
        while (true) {
            try {
                sellPurchasable(input().nextInt());
                return;
            } catch (InputMismatchException e) {
                input().next();
                System.out.println("Invalid input!");
            }
        }
    }

    /**
     * Takes player input and selects to either buy or sell purchasables.
     * Type of purchasables depends on the shop type.
     *
     * @param scannerInput The player's input as an int.
     * @param shopType     The string "items" or "monsters" depending on the type of shop.
     * @throws IllegalArgumentException if an invalid input is passed.
     */
    public void selectShopType(int scannerInput, String shopType) throws IllegalArgumentException {
        try {
            switch (scannerInput) {
                case 1:
                    buyPurchasableInterface(null);
                    displayShopOptions(shopType);
                    selectShopType(input().nextInt(), shopType);
                    break;
                case 2:
                    if (shopType.equals("items")) {
                        sellPurchasableInterface(null);
                        displayShopOptions(shopType);
                    } else {
                        if (!party.isEmpty()) {
                            sellPurchasableInterface(null);
                            displayShopOptions(shopType);
                        } else {
                            System.out.println("You have no monsters to sell!");
                        }
                    }
                    selectShopType(input().nextInt(), shopType);
                    break;
                case 0:
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input!");
            selectShopType(input().nextInt(), shopType);
        }
    }

    /**
     * Prints buy and sell shop options to output.
     *
     * @param shopType The string "items" or "monsters" depending on the type of shop.
     */
    public void displayShopOptions(String shopType) {
        System.out.println("\n===========================\n");
        System.out.printf("Gold: %d\n", gameManager.getGold());
        System.out.printf("Would you like to buy or sell %s?%n", shopType);
        System.out.printf("1 - Buy %s%n", shopType);
        System.out.printf("2 - Sell %s%n", shopType);
        System.out.println("\n0 - Return to shop menu");
    }

    /**
     * Takes player input and attempts to buy the selected purchasable.
     *
     * @param scannerInput The player's input as an int.
     */
    public abstract void buyPurchasable(int scannerInput);

    /**
     * Takes player input and attempts to sell the selected purchasable.
     *
     * @param scannerInput The player's input as an int.
     */
    public abstract void sellPurchasable(int scannerInput);

    /**
     * Prints the purchasable options to buy.
     *
     * @param boughtPurchasable The last purchasable bought, null otherwise.
     */
    public abstract void displayBuyPurchasableOptions(Purchasable boughtPurchasable);

    /**
     * Prints the purchasable options to sell.
     *
     * @param soldPurchasable The last purchasable sold, null otherwise.
     */
    public abstract void displaySellPurchasableOptions(Purchasable soldPurchasable);
}
