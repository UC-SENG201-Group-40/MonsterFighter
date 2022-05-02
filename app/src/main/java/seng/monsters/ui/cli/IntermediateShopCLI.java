package seng.monsters.ui.cli;

import seng.monsters.model.GameManager;

public class IntermediateShopCLI extends TestableCLI {
    private final GameManager gameManager;

    public IntermediateShopCLI(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    private void selectShopInterface() {
        displayIntermediateShopChoices();
        selectShop(input().nextInt());
    }

    private void selectShop(int scannerInput) throws IllegalArgumentException {
        try {
            switch (scannerInput) {
                case 1:
                    ItemShopCLI.make(gameManager);
                    selectShopInterface();
                    break;
                case 2:
                    MonsterShopCLI.make(gameManager);
                    selectShopInterface();
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

    private void displayIntermediateShopChoices() {
        System.out.println("\n===========================\n");
        System.out.printf("Gold: %d\n", gameManager.getGold());
        System.out.println("Select a shop to enter:");
        System.out.println("1 - Item Shop");
        System.out.println("2 - Monster Shop");
        System.out.println("\n0 - Return to Main Menu");
    }

    public static void make(GameManager gameManager) {
        IntermediateShopCLI shopCLI = new IntermediateShopCLI(gameManager);
        shopCLI.selectShopInterface();
    }
}
























