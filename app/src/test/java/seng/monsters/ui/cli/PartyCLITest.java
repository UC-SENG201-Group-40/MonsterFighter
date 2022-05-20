package seng.monsters.ui.cli;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng.monsters.model.GameManager;
import seng.monsters.model.Monster;
import seng.monsters.model.Trainer;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PartyCLITest extends CLITestBase {

    private GameManager gameManager;
    private PartyCLI partyCLI;

    @BeforeEach
    void setUp() {
        gameManager = new GameManager(1000, 1, 5, 1, "Test");
        partyCLI = new PartyCLI(gameManager);
        baseSetup();
    }

    @Override
    public TestableCLI cli() {
        return partyCLI;
    }

    @AfterEach
    void tearDown() {
        baseTeardown();
    }

    /**
     * PartyCLi's <code>partyStatsInterface</code> should:
     * <ul>
     * <li>Perform nothing and exit if given <code>"0"</code> user input</li>
     * <li>Swap monsters if given 2 valid indices user inputs</li>
     * <li>Not swap monsters if given only 1 valid index user input</li>
     * <li>Output <code>"Invalid input!</code> if given 1 invalid index</li>
     * <li>Be able to loop back and perform proper even after given 1 invalid index</li>
     * </ul>
     */
    @Test
    void partyStatsInterface() {
        final Trainer trainer = gameManager.getTrainer();
        final List<Monster> monsters = List.of(new Monster.Quacker("1", 1), new Monster.Eel("2", 1));
        monsters.forEach(trainer::add);

        // Immediately exit with 0
        provideInput("0");
        partyCLI.partyStatsInterface(false);

        // Move 1 with 2, and then exit with 0
        provideMultipleInput(List.of("1", "2", "0"));
        partyCLI.partyStatsInterface(false);
        assertNotEquals(monsters.get(0), trainer.getParty().get(0));
        assertEquals(monsters.get(1), trainer.getParty().get(0));
        assertEquals("1", trainer.getParty().get(1).getName());
        assertEquals("2", trainer.getParty().get(0).getName());

        // Move 1, but immediately exit with 0
        provideMultipleInput(List.of("1", "0", "0"));
        partyCLI.partyStatsInterface(false);
        assertNotEquals(monsters.get(0), trainer.getParty().get(0));
        assertEquals(monsters.get(1), trainer.getParty().get(0));
        assertEquals("1", trainer.getParty().get(1).getName());
        assertEquals("2", trainer.getParty().get(0).getName());

        // Give invalid initial input and exit with 0
        provideMultipleInput(List.of("100", "0"));
        partyCLI.partyStatsInterface(false);
        assertTrue(acquireOutput().contains("Invalid input!"));

        // Give invalid initial input, move 1 to 2, and exit with 0
        provideMultipleInput(List.of("100", "1", "2", "0"));
        partyCLI.partyStatsInterface(false);
        assertTrue(acquireOutput().contains("Invalid input!"));
        assertEquals(monsters.get(0), trainer.getParty().get(0));
        assertNotEquals(monsters.get(1), trainer.getParty().get(0));
        assertEquals("1", trainer.getParty().get(0).getName());
        assertEquals("2", trainer.getParty().get(1).getName());
    }

    /**
     * PartyCLi's <code>moveMonsterInterface</code> should:
     * <ul>
     * <li>Perform nothing and exit if given <code>"0"</code> user input</li>
     * <li>Swap monsters if given an existing monster and a valid index user inputs</li>
     * <li>Output <code>"Invalid input!</code> if given 1 invalid index or non-existing monster</li>
     * <li>Be able to loop back and perform proper even after given 1 invalid index</li>
     * </ul>
     */
    @Test
    void moveMonsterInterface() {
        final Trainer trainer = gameManager.getTrainer();
        final List<Monster> monsters = List.of(new Monster.Quacker("1", 1), new Monster.Eel("2", 1));
        monsters.forEach(trainer::add);

        // Immediately exit with 0
        provideInput("0");
        assertFalse(partyCLI.moveMonsterInterface(monsters.get(0)));

        // Move existing monster with 2
        provideInput("2");
        assertTrue(partyCLI.moveMonsterInterface(monsters.get(0)));
        assertNotEquals(monsters.get(0), trainer.getParty().get(0));
        assertEquals(monsters.get(1), trainer.getParty().get(0));
        assertEquals("1", trainer.getParty().get(1).getName());
        assertEquals("2", trainer.getParty().get(0).getName());

        // Move with invalid input, and then exit with 0
        provideMultipleInput(List.of("100", "0"));
        assertFalse(partyCLI.moveMonsterInterface(monsters.get(0)));
        assertTrue(acquireOutput().contains("Invalid input!"));

        // Move with invalid input, and then properly swap with 1
        provideMultipleInput(List.of("100", "1"));
        assertTrue(partyCLI.selectMonsterToSwap(monsters.get(0), 100));
        assertTrue(acquireOutput().contains("Invalid input!"));
        assertEquals(monsters.get(0), trainer.getParty().get(0));
        assertNotEquals(monsters.get(1), trainer.getParty().get(0));
        assertEquals("1", trainer.getParty().get(0).getName());
        assertEquals("2", trainer.getParty().get(1).getName());
    }

    /**
     * PartyCLi's <code>selectMonsterToMove</code> should:
     * <ul>
     * <li>Perform nothing and exit if given <code>"0"</code> user input</li>
     * <li>Swap monsters if given 2 valid indices user inputs</li>
     * <li>Not swap monsters if given only 1 valid index user input</li>
     * <li>Output <code>"Invalid input!</code> if given 1 invalid index</li>
     * <li>Be able to loop back and perform proper even after given 1 invalid index</li>
     * </ul>
     */
    @Test
    void selectMonsterToMove() {
        final Trainer trainer = gameManager.getTrainer();
        final List<Monster> monsters = List.of(new Monster.Quacker("1", 1), new Monster.Eel("2", 1));
        monsters.forEach(trainer::add);

        partyCLI.selectMonsterToMove(0);

        // Move 1 with 2, and then exit with 0
        provideMultipleInput(List.of("2", "0"));
        partyCLI.selectMonsterToMove(1);
        assertNotEquals(monsters.get(0), trainer.getParty().get(0));
        assertEquals(monsters.get(1), trainer.getParty().get(0));
        assertEquals("1", trainer.getParty().get(1).getName());
        assertEquals("2", trainer.getParty().get(0).getName());

        // Move 1, but immediately exit with 0
        provideMultipleInput(List.of("0", "0"));
        partyCLI.selectMonsterToMove(1);
        assertNotEquals(monsters.get(0), trainer.getParty().get(0));
        assertEquals(monsters.get(1), trainer.getParty().get(0));
        assertEquals("1", trainer.getParty().get(1).getName());
        assertEquals("2", trainer.getParty().get(0).getName());

        // Give invalid initial input and exit with 0
        provideInput("0");
        partyCLI.selectMonsterToMove(100);
        assertTrue(acquireOutput().contains("Invalid input!"));

        // Give invalid initial input, move 1 to 2, and exit with 0
        provideMultipleInput(List.of("1", "2", "0"));
        partyCLI.selectMonsterToMove(100);
        assertTrue(acquireOutput().contains("Invalid input!"));
        assertEquals(monsters.get(0), trainer.getParty().get(0));
        assertNotEquals(monsters.get(1), trainer.getParty().get(0));
        assertEquals("1", trainer.getParty().get(0).getName());
        assertEquals("2", trainer.getParty().get(1).getName());
    }

    /**
     * PartyCLi's <code>selectMonsterToSwap</code> should:
     * <ul>
     * <li>Perform nothing and exit if given <code>"0"</code> user input</li>
     * <li>Swap monsters if given an existing monster and a valid index user inputs</li>
     * <li>Output <code>"Invalid input!</code> if given 1 invalid index or non-existing monster</li>
     * <li>Be able to loop back and perform proper even after given 1 invalid index</li>
     * </ul>
     */
    @Test
    void selectMonsterToSwap() {
        final Trainer trainer = gameManager.getTrainer();
        final List<Monster> monsters = List.of(new Monster.Quacker("1", 1), new Monster.Eel("2", 1));
        monsters.forEach(trainer::add);

        assertFalse(partyCLI.selectMonsterToSwap(new Monster.Tree(1), 0));

        // Move existing monster with 2
        assertTrue(partyCLI.selectMonsterToSwap(monsters.get(0), 2));
        assertNotEquals(monsters.get(0), trainer.getParty().get(0));
        assertEquals(monsters.get(1), trainer.getParty().get(0));
        assertEquals("1", trainer.getParty().get(1).getName());
        assertEquals("2", trainer.getParty().get(0).getName());

        // Move with invalid input, and then exit with 0
        provideInput("0");
        assertFalse(partyCLI.selectMonsterToSwap(monsters.get(0), 100));
        assertTrue(acquireOutput().contains("Invalid input!"));

        // Move with invalid input, and then properly swap with 1
        provideInput("1");
        assertTrue(partyCLI.selectMonsterToSwap(monsters.get(0), 100));
        assertTrue(acquireOutput().contains("Invalid input!"));
        assertEquals(monsters.get(0), trainer.getParty().get(0));
        assertNotEquals(monsters.get(1), trainer.getParty().get(0));
        assertEquals("1", trainer.getParty().get(0).getName());
        assertEquals("2", trainer.getParty().get(1).getName());
    }

    /**
     * PartyCLi's <code>displayPartyStats</code> should:
     * <ul>
     * <li>Display all monsters in the player's party full statistics</li>
     * </ul>
     */
    @Test
    void displayPartyStats() {
        partyCLI.displayPartyStats(false);
        final String res0 = acquireOutput();
        assertFalse(Stream.of(1, 2, 3, 4).map(Object::toString).anyMatch(s -> res0.contains(s + " - ")));

        final Trainer trainer = gameManager.getTrainer();
        final List<Monster> monsters = List.of(new Monster.Quacker("1", 1), new Monster.Eel("2", 1));
        monsters.forEach(trainer::add);
        partyCLI.displayPartyStats(false);
        final String res1 = acquireOutput();
        assertTrue(monsters.stream()
            .map(mon ->
                String.format("\n%s - %s (Level %d, %d/%d HP)\n", mon.getName(), mon.getName(), mon.getLevel(), mon.getCurrentHp(), mon.maxHp())
                    + String.format("Monster Type: %s\n", mon.monsterType())
                    + String.format("Sell Price: %d Gold\n", mon.sellPrice())
                    + String.format("Attack Damage: %d\n", mon.scaledDamage())
                    + String.format("Speed: %d\n", mon.speed())
                    + String.format("Overnight Heal Rate: %d\n", mon.healRate())
                    + String.format("Ideal Environment: %s\n", mon.idealEnvironment())
            )
            .allMatch(res1::contains)
        );
    }

    /**
     * PartyCLi's <code>displayPartyStats</code> should:
     * <ul>
     * <li>Display all monsters in the player's party, only their name</li>
     * </ul>
     */
    @Test
    void displayMoveMonsters() {
        partyCLI.displayMoveMonsters(new Monster.Tree("A", 1));
        final String res0 = acquireOutput();
        assertTrue(res0.contains("Which monster would you like to swap A with?"));
        assertFalse(Stream.of(1, 2, 3, 4).map(Object::toString).anyMatch(s -> res0.contains(s + " - ")));

        final Trainer trainer = gameManager.getTrainer();
        List.of(new Monster.Quacker("1", 1), new Monster.Eel("2", 1)).forEach(trainer::add);
        partyCLI.displayMoveMonsters(new Monster.Tree("A", 1));
        final String res1 = acquireOutput();
        assertTrue(Stream.of(1, 2).map(Object::toString).allMatch(s -> res1.contains(s + " - " + s)));
    }
}