package seng.monsters.ui.cli;

import seng.monsters.model.*;

import java.util.List;

/**
 * A CLi for displaying monster from the shop and allowing the player to buy monsters
 */
public final class MonsterShopCLI extends ShopCLI {

    /**
     * A CLi for displaying monster from the shop and allowing the player to buy monsters
     *
     * @param gameManager The game manager / controller
     */
    public MonsterShopCLI(GameManager gameManager) {
        super(gameManager);
    }

    /**
     * Takes player input and attempts to buy the selected monster.
     *
     * @param scannerInput The player's input as an int.
     */
    public void buyPurchasable(int scannerInput) throws IllegalArgumentException, Trainer.PartyFullException {
        try {
            if ((scannerInput > 0) && (scannerInput < shop.getMonsterStock().size() + 1)) {
                final Monster mon = shop.getMonsterStock().get(scannerInput - 1);
                if (party.size() == 4) {
                    throw new Trainer.PartyFullException("Cannot add more than 4 monster");
                }
                gameManager.buy(mon);
                MainMenuCLI.monsterJoinsPartyInterface(input(), mon);
                buyPurchasableInterface(mon);
            } else if (scannerInput != 0) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input!");
            buyPurchasable(input().nextInt());
        } catch (Shop.InsufficientFundsException e) {
            System.out.println("You're too poor! Come back when you're a little, mmmm... RICHER!");
            buyPurchasable(input().nextInt());
        } catch (Trainer.PartyFullException e) {
            System.out.println("Your party is full!");
            buyPurchasable(input().nextInt());
        }
    }

    /**
     * Takes player input and attempts to sell the selected monster.
     *
     * @param scannerInput The player's input as an int.
     */
    public void sellPurchasable(int scannerInput) throws IllegalArgumentException {
        try {
            if ((scannerInput > 0) && (scannerInput < party.size() + 1)) {
                final Monster mon = party.get(scannerInput - 1);
                gameManager.sell(mon);
                sellPurchasableInterface(mon);
            } else if (scannerInput != 0) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input!");
            sellPurchasable(input().nextInt());
        }
    }

    /**
     * Prints the monster options to buy.
     *
     * @param boughtMon The last monster bought, null otherwise.
     */
    public void displayBuyPurchasableOptions(Purchasable boughtMon) {
        System.out.println("\n===========================\n");
        if (boughtMon != null) {
            System.out.printf("%s bought!\n", boughtMon.getName());
        }
        System.out.printf("Gold: %d\n", gameManager.getGold());
        System.out.println("Select a monster to buy:");
        final List<Monster> shopMonsters = shop.getMonsterStock();
        for (int i = 0; i < shopMonsters.size(); i++) {
            final Monster mon = shopMonsters.get(i);
            System.out.printf("\n%d - %s (Level %d)\n", i + 1, mon.getName(), mon.getLevel());
            System.out.printf("Price: %d Gold\n", mon.buyPrice());
            System.out.printf("Max Hp: %d\n", mon.maxHp());
            System.out.printf("Attack Damage: %d\n", mon.scaledDamage());
            System.out.printf("Speed: %d\n", mon.speed());
            System.out.printf("Overnight Heal Rate: %d\n", mon.healRate());
            System.out.printf("Ideal Environment: %s\n", mon.idealEnvironment());
        }
        System.out.println("\n0 - Cancel");
    }

    /**
     * Prints the monster options to sell.
     *
     * @param soldMon The last monster sold, null otherwise.
     */
    public void displaySellPurchasableOptions(Purchasable soldMon) {
        System.out.println("\n===========================\n");
        if (soldMon != null) {
            System.out.printf("%s sold!\n", soldMon.getName());
        }
        System.out.printf("Gold: %d\n", gameManager.getGold());
        System.out.println("Select a monster to sell:");
        for (int i = 0; i < party.size(); i++) {
            final Monster mon = party.get(i);
            System.out.printf("%d - %s (Sell Price: %d)\n", i + 1, mon.getName(), mon.sellPrice());
        }
        System.out.println("\n0 - Cancel");
    }

    /**
     * Makes a MonsterShopCLI and run its interface
     * @param gameManager The game manager / controller
     */
    public static void make(GameManager gameManager) {
        MonsterShopCLI monsterShopCLI = new MonsterShopCLI(gameManager);
        monsterShopCLI.shopTypeInterface("monsters");
    }
}
