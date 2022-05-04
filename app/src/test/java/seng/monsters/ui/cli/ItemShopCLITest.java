package seng.monsters.ui.cli;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng.monsters.model.GameManager;
import seng.monsters.model.Inventory;
import seng.monsters.model.Item;
import seng.monsters.model.Shop;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemShopCLITest extends CLITestBase {

    private ItemShopCLI itemShopCLI;
    private GameManager gameManager;
    private Inventory inventory;
    private Shop shop;

    @BeforeEach
    void setUp() {
        gameManager = new GameManager(0, 1, 5, 1, "Idiot");
        inventory = gameManager.getInventory();
        shop = gameManager.getShop();
        itemShopCLI = new ItemShopCLI(gameManager);
        baseSetup();
    }

    @Override
    public TestableCLI cli() { return itemShopCLI; }

    @AfterEach
    void tearDown() {
        baseTeardown();
    }

    @Test
    void shopTypeInterface() {
        // Immediately exit with 0
        provideInput("0");
        itemShopCLI.shopTypeInterface("items");
        assertTrue(acquireOutput().contains("Would you like to buy or sell items?"));

        // Enter buy section, then exit
        provideMultipleInput(List.of("1", "0", "0"));
        itemShopCLI.shopTypeInterface("items");
        assertTrue(acquireOutput().contains("Select an item to buy:"));

        // Enter sell section, then exit
        provideMultipleInput(List.of("2", "0", "0"));
        itemShopCLI.shopTypeInterface("items");
        assertTrue(acquireOutput().contains("Select an item to sell:"));

        // Enter sell section, exit section, enter buy section, exit
        provideMultipleInput(List.of("2", "0", "1", "0", "0"));
        itemShopCLI.shopTypeInterface("items");
        assertTrue(acquireOutput().contains("Select an item to buy:"));

        // Enter an invalid input, then exit
        provideMultipleInput(List.of("69", "2", "0", "0"));
        itemShopCLI.shopTypeInterface("items");
        assertTrue(acquireOutput().contains("Select an item to sell:"));
    }

    @Test
    void buyPurchasableInterface() {
        gameManager.setGold(1050);

        // Immediately exit with 0
        provideInput("0");
        itemShopCLI.buyPurchasableInterface(null);

        // Buy 1 FullRestore
        final var fullRestore = new Item.FullRestore();
        shop.setItemStock(fullRestore, 2);
        provideMultipleInput(List.of("3", "0"));
        itemShopCLI.buyPurchasableInterface(null);
        assertEquals(1, shop.getItemStock(fullRestore));
        assertEquals(1, inventory.getItemNumber(fullRestore));
        assertEquals(50, gameManager.getGold());
        assertTrue(acquireOutput().contains("FullRestore bought!"));

        // Attempt to buy item with insufficient funds
        provideMultipleInput(List.of("3", "0"));
        itemShopCLI.buyPurchasableInterface(null);
        assertEquals(1, shop.getItemStock(fullRestore));
        assertEquals(1, inventory.getItemNumber(fullRestore));
        assertTrue(acquireOutput().contains("You're too poor! Come back when you're a little, mmmm... RICHER!"));

        // Invalid input, then regular flow
        final var potion = new Item.Potion();
        shop.setItemStock(potion, 5);
        provideMultipleInput(List.of("420", "2", "2", "0"));
        itemShopCLI.buyPurchasableInterface(null);
        assertEquals(3, shop.getItemStock(potion));
        assertEquals(2, inventory.getItemNumber(potion));
    }

    @Test
    void sellPurchasableInterface() {
        final var potion = new Item.Potion();
        final var rareCandy = new Item.RareCandy();
        inventory.add(potion);
        inventory.add(potion);
        inventory.add(rareCandy);

        // Immediately exit with 0
        provideInput("0");
        itemShopCLI.sellPurchasableInterface(null);

        // Sell an item
        provideMultipleInput(List.of("3", "0"));
        itemShopCLI.sellPurchasableInterface(null);
        assertEquals(0, inventory.getItemNumber(rareCandy));
        assertEquals(50, gameManager.getGold());
        assertTrue(acquireOutput().contains("RareCandy sold!"));

        // Attempt to sell an item that the player does not have
        provideMultipleInput(List.of("3", "0"));
        itemShopCLI.sellPurchasableInterface(null);
        assertEquals(0, inventory.getItemNumber(rareCandy));
        assertEquals(50, gameManager.getGold());
        assertTrue(acquireOutput().contains("You don't have any of that item!"));

        // Invalid input, then sell an item
        provideMultipleInput(List.of("bhjagsdf", "1", "0"));
        itemShopCLI.sellPurchasableInterface(null);
        assertEquals(1, inventory.getItemNumber(potion));
        assertEquals(62, gameManager.getGold());
        assertTrue(acquireOutput().contains("Potion sold!"));
    }


}
