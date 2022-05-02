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

    public void shopTypeInterface(String shopType) {
        displayShopTypes(shopType);
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

    public void selectShopType(int scannerInput, String shopType) throws IllegalArgumentException {
        try {
            switch (scannerInput) {
                case 1:
                    buyPurchasableInterface(null, false);
                    displayShopTypes(shopType);
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
                    displayShopTypes(shopType);
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

    public void displayShopTypes(String shopType) {
        System.out.println("\n===========================\n");
        System.out.printf("Gold: %d\n", gameManager.getGold());
        System.out.printf("Would you like to buy or sell %s?%n", shopType);
        System.out.printf("1 - Buy %s%n", shopType);
        System.out.printf("2 - Sell %s%n", shopType);
        System.out.println("\n0 - Return to shop menu");
    }

    public abstract void buyPurchasable(int scannerInput);

    public abstract void sellPurchasable(int scannerInput);

    public abstract void displayBuyPurchasableOptions(Purchasable boughtItem, boolean wasPurchasableBought);

    public abstract void displaySellPurchasableOptions(Purchasable purchasable, boolean wasItemSold);
}
