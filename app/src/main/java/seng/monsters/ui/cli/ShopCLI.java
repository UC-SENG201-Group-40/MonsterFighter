package seng.monsters.ui.cli;

import seng.monsters.model.GameManager;

//TODO: Still buggy, needs refactoring and testing


public class ShopCLI extends TestableCLI {
    private final GameManager gameManager;

    public ShopCLI(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    private void selectShopInterface() {
        displayShopChoices();
        selectShop(input().nextInt());
    }

    private void selectShop(int scannerInput) throws IllegalArgumentException {
        try {
            switch (scannerInput) {
                case 1:
                    ItemShopCLI.make(gameManager);
                    break;
                case 2:
                    MonsterShopCLI.make(gameManager);
                    break;
                case 0:
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input!");
            selectShop(input().nextInt());
        }
    }

    private void displayShopChoices() {
        System.out.println("\n===========================\n");
        System.out.printf("Gold: %d\n", gameManager.getGold());
        System.out.println("Select a shop to enter:");
        System.out.println("1 - Item Shop");
        System.out.println("2 - Monster Shop");
        System.out.println("\n0 - Return to Main Menu");
    }

    public static void make(GameManager gameManager) {
        ShopCLI shopCLI = new ShopCLI(gameManager);
        shopCLI.selectShopInterface();
    }
}
























