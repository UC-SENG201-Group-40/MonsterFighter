package seng.monsters.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShopTest {
    private GameManager gameManager;
    private Shop shop;

    @BeforeEach
    void setUp() {
        gameManager = new GameManager();
        shop = new Shop(gameManager);
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * Shop <code>buyPurchasable()</code> should:
     * <ul>
     * <li>Validate if the manager have enough funds otherwise throw an Exception</li>
     * <li>Validate if item or monster is in stock</li>
     * <li>Remove monster from stock or reduce item's count by 1</li>
     * <li>Subtract the cost</li>
     * <li>Not accept any other type purchasable and throw an Exception</li>
     * </ul>
     */
    @Test
    void buyPurchasable() {
        // Shop has not restocked (empty) and player has no money
        gameManager.setGold(0);
        assertThrows(Shop.InsufficientFundsException.class,
            () -> shop.buyPurchasable(new Item.Potion())
        );

        // Player has sufficient funds but stock is still empty (1)
        gameManager.setGold(new Item.Potion().buyPrice() * 2);
        assertThrows(Shop.NotInStockException.class,
            () -> shop.buyPurchasable(new Item.Potion())
        );

        // Player has sufficient funds but stock is still empty (2)
        final var monster = new Monster.Eel(100);
        gameManager.setGold(monster.buyPrice());
        assertThrows(Shop.NotInStockException.class,
            () -> shop.buyPurchasable(monster)
        );

        // Blue sky scenario, enough funds, has proper item in stock
        gameManager.setMaxDays(10);
        gameManager.setCurrentDay(9);
        shop.restock();


        var maybeItem = shop.getItemStock().stream().findAny();
        while (maybeItem.isEmpty()) {
            shop.restock();
            maybeItem = shop.getItemStock().stream().findAny();
        }
        final var item = maybeItem.get().getKey();
        gameManager.setGold(item.buyPrice());
        shop.buyPurchasable(item);
        assertEquals(0, gameManager.getGold());

        var maybeMonster = shop.getMonsterStock().stream().findAny();
        while (maybeMonster.isEmpty()) {
            shop.restock();
            maybeMonster = shop.getMonsterStock().stream().findAny();
        }

        final var monster1 = maybeMonster.get();
        gameManager.setGold(monster1.buyPrice());
        shop.buyPurchasable(monster1);
        assertEquals(0, gameManager.getGold());


        // Buying non-item or non-monster purchasable
        assertThrows(Shop.NotInStockException.class,
            () -> shop.buyPurchasable(new Purchasable() {
                @Override
                public int buyPrice() {
                    return 0;
                }

                @Override
                public int sellPrice() {
                    return 0;
                }

                @Override
                public String getName() { return "purchasable";}

                @Override
                public String description() { return "Something that can be purchased."; }
            })
        );
    }


    /**
     * Shop <code>sellPurchasable()</code> should:
     * <ul>
     * <li>Sell the purchasable and add the sold cost to the player gold total</li>
     * </ul>
     */
    @Test
    void sellPurchasable() {
        final var definitelyInPartyMonster = new Monster.Eel(69);
        shop.sellPurchasable(definitelyInPartyMonster);
        assertEquals(definitelyInPartyMonster.sellPrice(), gameManager.getGold());

        final var definitelyInInventoryItem = new Item.RareCandy();
        gameManager.setGold(0);
        shop.sellPurchasable(definitelyInInventoryItem);
        assertEquals(definitelyInInventoryItem.sellPrice(), gameManager.getGold());
    }

    /**
     * Shop <code>randomMonster()</code> should:
     * <ul>
     * <li>Create a new monster with a level range of 3 times the difficulty added by the current day</li>
     * <li>The level formula is <code>level = <b>currentDay</b> ± (3 * <b>difficulty</b>) </code></li>
     * </ul>
     */
    @Test
    void randomMonster() {
        gameManager.setDifficulty(1);
        gameManager.setCurrentDay(1);
        gameManager.setMaxDays(2);

        final var day0 = gameManager.getCurrentDay();
        final var mon0 = shop.randomMonster();
        assertTrue(mon0.getLevel() <= (3 + day0) && mon0.getLevel() >= (-3 + day0));

        gameManager.setDifficulty(2);
        gameManager.setCurrentDay(1);
        gameManager.setMaxDays(2);

        final var day1 = gameManager.getCurrentDay();
        final var mon1 = shop.randomMonster();
        assertTrue(mon1.getLevel() <= (6 + day1) && mon1.getLevel() >= (-6 + day1));
    }

    /**
     * Shop <code>randomItemStock()</code> should:
     * <ul>
     * <li>Create a new map for all item and its stock count</li>
     * <li>The count formula is <code>count = 1 + <b>currentDay</b> * <b>difficulty</b> </code></li>
     * </ul>
     */
    @Test
    void randomItemStock() {
        gameManager.setDifficulty(1);
        gameManager.setCurrentDay(1);
        gameManager.setMaxDays(2);

        final var day0 = gameManager.getCurrentDay();
        final var stocks0 = shop.randomItemStock();
        for (final var entry: stocks0.entrySet()) {
            assertTrue(entry.getValue() >= 1 && entry.getValue() <= (day0 + 1));
        }

        gameManager.setDifficulty(2);
        gameManager.setCurrentDay(1);
        gameManager.setMaxDays(2);

        final var day1 = gameManager.getCurrentDay();
        final var stocks1 = shop.randomItemStock();
        for (final var entry: stocks1.entrySet()) {
            assertTrue(entry.getValue() >= 1 && entry.getValue() <= (day1 * 2 + 1));
        }

    }

    /**
     * Shop <code>restock()</code> should:
     * <ul>
     * <li>Fill in all the item stock and the monster stock</li>
     * <li>Fill in monster stock with 3 times difficulty unique monsters</li>
     * </ul>
     */
    @Test
    void restock() {
        // Shop is empty (after construction), restock should have new items
        gameManager.setDifficulty(1);
        gameManager.setCurrentDay(1);
        gameManager.setMaxDays(2);
        shop.restock();

        assertFalse(shop.getItemStock().isEmpty());
        assertFalse(shop.getMonsterStock().isEmpty());

        assertEquals(3, shop.getMonsterStock().size());

        gameManager.setDifficulty(2);
        gameManager.setCurrentDay(1);
        gameManager.setMaxDays(2);
        shop.restock();

        assertFalse(shop.getItemStock().isEmpty());
        assertFalse(shop.getMonsterStock().isEmpty());

        assertEquals(6, shop.getMonsterStock().size());
    }
}