package seng.monsters.ui.cli;

import seng.monsters.model.*;

import java.util.InputMismatchException;
import java.util.List;

public class MonsterShopCLI extends TestableCLI {

    private final GameManager gameManager;
    private final Shop shop;
    private final List<Monster> party;

    public MonsterShopCLI(GameManager gameManager) {
        this.gameManager = gameManager;
        shop = gameManager.getShop();
        party = gameManager.getTrainer().getParty();
    }

    private void shopTypeInterface() {
        displayShopTypes();
        selectShopType(input().nextInt());
    }

    private void buyMonsterInterface() {
        while (true) {
            try {
                displayMonsterBuy();
                buyMonster(input().nextInt());
                return;
            } catch (InputMismatchException e) {
                input().next();
                System.out.println("Invalid input!");
            }
        }
    }

    private void sellMonsterInterface(Monster mon, boolean wasMonSold) {
        if (!party.isEmpty()) {
            while (true) {
                try {
                    displayMonstersSell(mon, wasMonSold);
                    sellMonster(input().nextInt());
                    return;
                } catch (InputMismatchException e) {
                    input().next();
                    System.out.println("Invalid input!");
                }
            }
        } else {
            System.out.println("You have no monsters to sell!");
        }
    }

    private void selectShopType(int scannerInput) throws IllegalArgumentException {
        try {
            switch (scannerInput) {
                case 1:
                    buyMonsterInterface();
                    displayShopTypes();
                case 2:
                    sellMonsterInterface(null, false);
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

    private void buyMonster(int scannerInput) throws IllegalArgumentException{
        try {
            if ((scannerInput > 0) && (scannerInput < shop.getMonsterStock().size())) {
                final var mon = shop.getMonsterStock().get(scannerInput - 1);
                gameManager.buy(mon);
                MainMenuCLI.monsterJoinsPartyInterface(input(), mon);
                buyMonsterInterface();
            } else if (scannerInput != 0) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input!");
            buyMonster(input().nextInt());
        } catch (Shop.InsufficientFundsException e) {
            System.out.println("You're too poor! Come back when you're a little, mmmm... RICHER!");
            buyMonster(input().nextInt());
        }
    }

    private void sellMonster(int scannerInput) throws IllegalArgumentException {
        try {
            if ((scannerInput > 0) && (scannerInput < party.size())) {
                final var mon = party.get(scannerInput - 1);
                gameManager.sell(mon);
                sellMonsterInterface(mon, true);
            } else if (scannerInput != 0) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input!");
            sellMonster(input().nextInt());
        } catch (Inventory.ItemNotExistException e) {
            System.out.println("You don't have any of that item!");
            sellMonster(input().nextInt());
        }
    }

    private void displayShopTypes() {
        System.out.println("\n===========================\n");
        System.out.printf("Gold: %d\n", gameManager.getGold());
        System.out.println("Would you like to buy or sell monsters?");
        System.out.println("1 - Buy Monsters");
        System.out.println("2 - Sell Monsters");
        System.out.println("\n0 - Return to Shop Menu");
    }

    private void displayMonsterBuy() {
        System.out.println("\n===========================\n");
        System.out.printf("Gold: %d\n", gameManager.getGold());
        System.out.println("Select a monster to buy:");
        final var shopMonsters = shop.getMonsterStock();
        for (int i = 0; i < shopMonsters.size(); i++) {
            final var mon = shopMonsters.get(i);
            System.out.printf("\n%d - %s (Level %d)\n", i + 1, mon.getName(), mon.getLevel());
            System.out.printf("Price: %d Gold\n", mon.buyPrice());
            System.out.printf("Max Hp: %d", mon.maxHp());
            System.out.printf("Attack Damage: %d\n", mon.scaledDamage());
            System.out.printf("Speed: %d\n", mon.speed());
            System.out.printf("Overnight Heal Rate: %d\n", mon.healRate());
            System.out.printf("Ideal Environment: %s\n", mon.idealEnvironment());
        }
        System.out.println("\n0 - Cancel");
    }

    private void displayMonstersSell(Monster soldMonster, boolean wasMonSold) {
        System.out.println("\n===========================\n");
        if (wasMonSold) {
            System.out.printf("%n%s bought!", soldMonster.getName());
        }
        System.out.printf("Gold: %d\n", gameManager.getGold());
        System.out.println("Select a monster to sell:");
        for (int i = 0; i < party.size(); i++) {
            final var mon = party.get(i);
            System.out.printf("%d - %s (Sell Price: %d)\n", i + 1, mon.getName(), mon.sellPrice());
        }
        System.out.println("\n0 - Cancel");
    }

    public static void make(GameManager gameManager) {
        MonsterShopCLI monsterShopCLI = new MonsterShopCLI(gameManager);
        monsterShopCLI.shopTypeInterface();
    }
}







