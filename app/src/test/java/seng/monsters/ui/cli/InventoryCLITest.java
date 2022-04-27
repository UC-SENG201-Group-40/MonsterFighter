package seng.monsters.ui.cli;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng.monsters.model.GameManager;
import seng.monsters.model.Item;
import seng.monsters.model.Monster;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryCLITest extends CLITestBase {

    private Monster labMonster;
    private GameManager gameManager;
    private InventoryCLI inventoryCLI;

    @BeforeEach
    void setUp() {
        labMonster = new Monster.Doger("Good boi", 10);
        gameManager = new GameManager(1000, 1, 5, 1, "Test");
        inventoryCLI = new InventoryCLI(gameManager);

        gameManager.getTrainer().add(labMonster);
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

    @Test
    void inventoryInterface() {
    }

    @Test
    void useItemInterface() {
        final var inventory = gameManager.getInventory();

        // Immediately exit with 0
        provideInput("0");
        inventoryCLI.useItemInterface(new Item.Potion(), false);
    }
}