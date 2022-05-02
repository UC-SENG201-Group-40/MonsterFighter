package seng.monsters.ui.cli;

import seng.monsters.model.*;

import java.util.InputMismatchException;
import java.util.List;

public abstract class ShopCLI extends TestableCLI {

    public final GameManager gameManager;
    public final Shop shop;
    public final List<Monster> party;

    public ShopCLI(GameManager gameManager) {
        this.gameManager = gameManager;
        shop = gameManager.getShop();
        party = gameManager.getTrainer().getParty();
    }

    /**
     * Prints buy and sell shop options to output and selects one based on player input.
     * Type of purchasables depends on shop type.
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
     * @param purchasable The last purchasable bought, null otherwise.
     * @param wasPurchasableBought a boolean flagging if a purchasable had been bought previously.
     */
    public void buyPurchasableInterface(Purchasable purchasable, boolean wasPurchasableBought) {
        displayBuyPurchasableOptions(purchasable, wasPurchasableBought);
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
     * @param purchasable The last purchasable sold, null otherwise.
     * @param wasPurchasableSold a boolean flagging if a purchasable had been sold previously.
     */
    public void sellPurchasableInterface(Purchasable purchasable, boolean wasPurchasableSold) {
        displaySellPurchasableOptions(purchasable, wasPurchasableSold);
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
     * @param scannerInput The player's input as an int.
     * @param shopType The string "items" or "monsters" depending on the type of shop.
     * @throws IllegalArgumentException if an invalid input is passed.
     */
    public void selectShopType(int scannerInput, String shopType) throws IllegalArgumentException {
        try {
            switch (scannerInput) {
                case 1:
                    buyPurchasableInterface(null, false);
                    displayShopOptions(shopType);
                    selectShopType(input().nextInt(), shopType);
                    break;
                case 2:
                    if (shopType.equals("items")) {
                        sellPurchasableInterface(null, false);
                    } else {
                        if (!party.isEmpty()) {
                            sellPurchasableInterface(null, false);
                        } else {
                            System.out.println("You have no monsters to sell!");
                        }
                    }
                    displayShopOptions(shopType);
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
     * @param scannerInput The player's input as an int.
     */
    public abstract void buyPurchasable(int scannerInput);

    /**
     * Takes player input and attempts to sell the selected purchasable.
     * @param scannerInput The player's input as an int.
     */
    public abstract void sellPurchasable(int scannerInput);

    /**
     * Prints the purchasable options to buy.
     * @param boughtPurchasable The last purchasable bought, null otherwise.
     * @param wasPurchasableBought a boolean flagging if a purchasable had been bought previously.
     */
    public abstract void displayBuyPurchasableOptions(Purchasable boughtPurchasable, boolean wasPurchasableBought);

    /**
     * Prints the purchasable options to sell.
     * @param soldPurchasable The last purchasable sold, null otherwise.
     * @param wasPurchasableSold a boolean flagging if a purchasable had been sold previously.
     */
    public abstract void displaySellPurchasableOptions(Purchasable soldPurchasable, boolean wasPurchasableSold);
}
