package seng.monsters.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
    }

    @AfterEach
    void tearDown() {
        inventory.getItemEntries()
            .forEach(entry -> {
                for (int i = 0; i < entry.getValue(); i++) {
                    inventory.remove(entry.getKey());
                }
            });
    }

    /**
     * Inventory <code>add()</code> should:
     * <ul>
     * <li>Add a new entry into the Map and a count of 1 if item doesn't exist yet</li>
     * <li>Increase the count of an item by 1</li>
     * <li>Not conflict the count of different item</li>
     * </ul>
     */
    @Test
    void add() {
        assertEquals(0, inventory.getItemEntries().stream().mapToInt(Map.Entry::getValue).sum());

        inventory.add(new Item.Potion());
        assertEquals(1, inventory.getItemEntries().stream().mapToInt(Map.Entry::getValue).sum());

        inventory.add(new Item.Potion());
        assertEquals(2, inventory.getItemEntries().stream().mapToInt(Map.Entry::getValue).sum());

        inventory.add(new Item.Revive());
        assertEquals(1,
            inventory.getItemEntries()
                .stream()
                .filter(entry -> entry.getKey().equals(new Item.Revive()))
                .mapToInt(Map.Entry::getValue)
                .sum()
        );
        assertEquals(2, inventory.getItems().size());
    }

    /**
     * Inventory <code>use()</code> should:
     * <ul>
     * <li>Use an item to a target and reduce count by 1 if successful</li>
     * <li>Throw an exception if item does not exist</li>
     * <li>Throw an exception if item throws an exception when used</li>
     * </ul>
     */
    @Test
    void use() {
        assertThrows(Inventory.ItemNotExistException.class,
            () -> inventory.use(new Item.RareCandy(), new Monster.Raver(1))
        );

        inventory.add(new Item.Revive());
        assertThrows(Item.NoEffectException.class,
            () -> inventory.use(new Item.Revive(), new Monster.Raver(1))
        );

        final Monster monster = new Monster.Raver(1000);
        monster.takeDamage(monster.maxHp());
        inventory.use(new Item.Revive(), monster);
        assertNotEquals(0, monster.getCurrentHp());
    }

    /**
     * Inventory <code>remove()</code> should:
     * <ul>
     * <li>Reduce the count of that item by 1</li>
     * <li>Not change anything and throw an exception if item doesn't exist</li>
     * </ul>
     */
    @Test
    void remove() {
        assertThrows(Inventory.ItemNotExistException.class, () -> inventory.remove(new Item.Potion()));

        inventory.add(new Item.Potion());
        inventory.remove(new Item.Potion());
        assertEquals(0, inventory.getItems().size());
    }

    /**
     * Inventory <code>getItems()</code> should:
     * <ul>
     * <li>Return all unique items as a list if the item exist in the inventory (count 1 or more)</li>
     * </ul>
     */
    @Test
    void getItems() {
        assertEquals(0, inventory.getItems().size());

        inventory.add(new Item.Potion());
        inventory.add(new Item.Revive());
        inventory.add(new Item.RareCandy());
        inventory.add(new Item.RareCandy());
        assertEquals(3, inventory.getItems().size());
    }
}