package seng.monsters.ui.cli;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng.monsters.model.GameManager;
import seng.monsters.model.Inventory;
import seng.monsters.model.Item;
import seng.monsters.model.Monster;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryCLITest extends CLITestBase {

    private Inventory inventory;
    private Monster labMonster;
    private InventoryCLI inventoryCLI;

    @BeforeEach
    void setUp() {
        final GameManager gameManager = new GameManager(1000, 1, 5, 1, "Test");
        labMonster = new Monster.Doger("Good boi", 10);
        inventoryCLI = new InventoryCLI(gameManager);
        inventory = gameManager.getInventory();
        gameManager.getPlayer().add(labMonster);
        baseSetup();
    }

    @Override
    public TestableCLI cli() {
        return inventoryCLI;
    }

    @AfterEach
    void tearDown() {
        baseTeardown();
    }

    /**
     * InventoryCLI's `<code>inventoryInterface</code>:
     * <ul>
     * <li>Wait until given input</li>
     * <li>Exit if given <code>0</code> as input</li>
     * <li>Not use item that doesn't exist in the inventory</li>
     * <li>Properly process all exception thrown by model classes</li>
     * <li>Apply item from inventory to the selected monster, if given valid indices for item and monster</li>
     * <li>Loop if given an invalid input, prompt user <code>Invalid input!</code>, and continue as normal</li>
     * </ul>
     */
    @Test
    void inventoryInterface() {
        // Immediately exit with 0
        provideInput("0");
        inventoryCLI.inventoryInterface();

        // Invalid item index and exit with 0
        provideMultipleInput(List.of("1", "1", "0", "0"));
        inventoryCLI.inventoryInterface();
        assertTrue(acquireOutput().contains("You don't have any of that item!"));

        // Valid index and item, but item produce no effect, and exit with 0
        inventory.add(new Item.Potion());
        provideMultipleInput(List.of("1", "1", "0", "0"));
        inventoryCLI.inventoryInterface();
        assertTrue(acquireOutput().contains("That item has no effect on this monster!"));

        // Valid index and item
        labMonster.takeDamage(labMonster.maxHp() / 2);
        final int currHp = labMonster.getCurrentHp();
        provideMultipleInput(List.of("1", "1", "0", "0"));
        inventoryCLI.inventoryInterface();
        assertNotEquals(currHp, labMonster.getCurrentHp());

        // Invalid input and exit with 0
        final int currHp2 = labMonster.getCurrentHp();
        provideMultipleInput(List.of("100", "0", "0"));
        inventoryCLI.inventoryInterface();
        assertEquals(currHp2, labMonster.getCurrentHp());

        // Invalid input, normal flow, exit with 0
        labMonster.takeDamage(10);
        inventory.add(new Item.Potion());
        final int currHp3 = labMonster.getCurrentHp();
        provideMultipleInput(List.of("100", "1", "1", "0", "0"));
        inventoryCLI.inventoryInterface();
        assertTrue(acquireOutput().contains("Invalid input!"));
        assertNotEquals(currHp3, labMonster.getCurrentHp());
    }

    /**
     * InventoryCLI's `<code>useItemInterface</code>:
     * <ul>
     * <li>Wait until given input</li>
     * <li>Exit if given <code>0</code> as input</li>
     * <li>Not use item that doesn't exist in the inventory</li>
     * <li>Properly process all exception thrown by model classes</li>
     * <li>Apply item from inventory to the selected monster, if given valid index for monster</li>
     * <li>Loop if given an invalid input, prompt user <code>Invalid input!</code>, and continue as normal</li>
     * </ul>
     */
    @Test
    void useItemInterface() {
        // Immediately exit with 0
        provideInput("0");
        inventoryCLI.useItemInterface(new Item.Potion(), false);

        // Valid index but non-existing item, and exit with 0
        provideMultipleInput(List.of("1", "0"));
        inventoryCLI.useItemInterface(new Item.Potion(), false);
        assertTrue(acquireOutput().contains("You don't have any of that item!"));

        // Valid index and item, but item produce no effect, and exit with 0
        inventory.add(new Item.Potion());
        provideMultipleInput(List.of("1", "0"));
        inventoryCLI.useItemInterface(new Item.Potion(), false);
        assertTrue(acquireOutput().contains("That item has no effect on this monster!"));

        // Valid index and item
        labMonster.takeDamage(labMonster.maxHp() / 2);
        final int currHp = labMonster.getCurrentHp();
        provideMultipleInput(List.of("1", "0"));
        inventoryCLI.useItemInterface(new Item.Potion(), false);
        assertNotEquals(currHp, labMonster.getCurrentHp());

        // Invalid input and exit with 0
        final int currHp2 = labMonster.getCurrentHp();
        provideMultipleInput(List.of("100", "0"));
        inventoryCLI.useItemInterface(new Item.Potion(), false);
        assertEquals(currHp2, labMonster.getCurrentHp());

        // Invalid input, normal flow, exit with 0
        labMonster.takeDamage(10);
        inventory.add(new Item.Potion());
        final int currHp3 = labMonster.getCurrentHp();
        provideMultipleInput(List.of("100", "1", "0"));
        inventoryCLI.useItemInterface(new Item.Potion(), false);
        assertTrue(acquireOutput().contains("Invalid input!"));
        assertNotEquals(currHp3, labMonster.getCurrentHp());
    }
}