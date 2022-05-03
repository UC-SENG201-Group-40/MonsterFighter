package seng.monsters.ui.cli;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng.monsters.model.GameManager;
import seng.monsters.model.Monster;
import seng.monsters.model.Shop;
import seng.monsters.model.Trainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MonsterShopCLITest extends CLITestBase {

    private MonsterShopCLI monsterShopCLI;
    private GameManager gameManager;
    private Trainer trainer;
    private Shop shop;

    @BeforeEach
    void setUp() {
        gameManager = new GameManager(0, 1, 5, 1, "Idiot");
        shop = gameManager.getShop();
        trainer = gameManager.getTrainer();
        monsterShopCLI = new MonsterShopCLI(gameManager);
        baseSetup();
    }

    @Override
    public TestableCLI cli() { return monsterShopCLI; }

    @AfterEach
    void tearDown() {
        baseTeardown();
    }

    // TODO: Needs comments
    @Test
    void shopTypeInterface() {
        // Immediately exit with 0
        provideInput("0");
        monsterShopCLI.shopTypeInterface("monsters");
        assertTrue(acquireOutput().contains("Would you like to buy or sell monsters?"));

        // Enter buy section, then exit
        provideMultipleInput(List.of("1", "0", "0"));
        monsterShopCLI.shopTypeInterface("monsters");
        assertTrue(acquireOutput().contains("Select a monster to buy:"));

        // Enter sell section with empty party
        provideMultipleInput(List.of("2", "0"));
        monsterShopCLI.shopTypeInterface("monsters");
        assertTrue(acquireOutput().contains("You have no monsters to sell!"));

        // Enter sell section, then exit
        trainer.add(new Monster.Quacker("The bastardisation of man", 70));
        provideMultipleInput(List.of("2", "0", "0"));
        monsterShopCLI.shopTypeInterface("monsters");
        assertTrue(acquireOutput().contains("Select a monster to sell:"));

        // Enter buy section, exit section, enter sell section, exit
        provideMultipleInput(List.of("1", "0", "2", "0", "0"));
        monsterShopCLI.shopTypeInterface("monsters");
        assertTrue(acquireOutput().contains("Select a monster to sell:"));

        // Enter an invalid input
        provideMultipleInput(List.of("69", "1", "0", "0"));
        monsterShopCLI.shopTypeInterface("monsters");
        assertTrue(acquireOutput().contains("Select a monster to buy:"));
    }

    @Test
    void buyPurchasableInterface() {
        trainer.add(new Monster.Quacker("The bastardisation of man", 70));
        trainer.add(new Monster.Eel("Homunculus", 1));

        // Immediately exit with 0
        provideInput("0");
        monsterShopCLI.buyPurchasableInterface(null);

        // Attempt to buy a monster with no funds
        final var previousMonsterStockNumber = shop.getMonsterStock().size();
        provideMultipleInput(List.of("1", "0"));
        monsterShopCLI.buyPurchasableInterface(null);
        assertEquals(2, trainer.getParty().size());
        assertEquals(shop.getMonsterStock().size(), previousMonsterStockNumber);
        assertTrue(acquireOutput().contains("You're too poor! Come back when you're a little, mmmm... RICHER!"));

        // Buy a monster
        gameManager.setGold(99999);
        provideMultipleInput(List.of("1", "noog", "0"));
        monsterShopCLI.buyPurchasableInterface(null);
        assertEquals(3, trainer.getParty().size());
        assertNotEquals(shop.getMonsterStock().size(), previousMonsterStockNumber);
        assertNotEquals(99999, gameManager.getGold());
        assertTrue(acquireOutput().contains("noog bought!"));

        // Invalid input, then regular flow
        provideMultipleInput(List.of("(ノ｀Д)ノ", "1", "blarg", "0"));
        monsterShopCLI.buyPurchasableInterface(null);
        assertEquals(4, trainer.getParty().size());
        assertTrue(acquireOutput().contains("blarg bought!"));

        // Attempt to buy a monster with a full party
        provideMultipleInput(List.of("1", "0"));
        monsterShopCLI.buyPurchasableInterface(null);
        assertEquals(4, trainer.getParty().size());
        assertTrue(acquireOutput().contains("Your party is full!"));
    }

    @Test
    void sellPurchasableInterface() {
        trainer.add(new Monster.Eel("Homunculus", 1));
        trainer.add(new Monster.Quacker("The bastardisation of man", 70));

        // Immediately exit with 0
        provideInput("0");
        monsterShopCLI.sellPurchasableInterface(null);

        // Sell a monster
        provideMultipleInput(List.of("2", "0"));
        monsterShopCLI.sellPurchasableInterface(null);
        final var currentGold = gameManager.getGold();
        assertEquals(1, trainer.getParty().size());
        assertNotEquals(0, currentGold);
        assertTrue(acquireOutput().contains("The bastardisation of man sold!"));

        // Invalid input that was previously a monster position, then sell
        provideMultipleInput(List.of("2", "1", "0"));
        monsterShopCLI.sellPurchasableInterface(null);
        assertEquals(0, trainer.getParty().size());
        assertNotEquals(currentGold, gameManager.getGold());
        assertTrue(acquireOutput().contains("Homunculus sold!"));
    }
}
