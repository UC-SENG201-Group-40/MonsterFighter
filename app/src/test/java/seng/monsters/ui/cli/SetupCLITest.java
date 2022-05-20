package seng.monsters.ui.cli;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng.monsters.model.Monster;

import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SetupCLITest extends CLITestBase {
    private SetupCLI setupCLI;

    @BeforeEach
    void setUp() {
        setupCLI = new SetupCLI();
        baseSetup();
    }


    @Override
    public TestableCLI cli() {
        return setupCLI;
    }

    @AfterEach
    void tearDown() {
        baseTeardown();
    }

    /**
     * SetupCLI's <code>chooseNameInterface</code>:
     * <ul>
     * <li>Return a valid string if given a valid username (3-15 characters, just letters)</li>
     * <li>Return only the valid string if given multiple inputs (last 1 valid and the remaining are invalid)</li>
     * </ul>
     */
    @Test
    void chooseNameInterface() {
        // Valid input
        provideInput("ThisIsValid");
        final String name1 = setupCLI.chooseNameInterface();
        assertEquals("ThisIsValid", name1);

        // Invalid (just symbols, mixed w/ numbers, mixed all, 2 characters, over 15 characters, then valid
        provideMultipleInput(List.of("#*(#))*", "Invalid1", "Invalid#$2", "aa", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "ThisIsValid"));
        final String name2 = setupCLI.chooseNameInterface();
        assertEquals("ThisIsValid", name2);
    }

    /**
     * SetupCLI's <code>chooseMaxDaysInterface</code>:
     * <ul>
     * <li>Return a valid max days if given a valid integer (5-15 inclusive)</li>
     * <li>Return only the valid max days if given multiple inputs (last 1 valid and the remaining are invalid)</li>
     * </ul>
     */
    @Test
    void chooseMaxDaysInterface() {
        // Valid input
        provideInput("5");
        final int maxDays1 = setupCLI.chooseMaxDaysInterface();
        assertTrue(acquireOutput().contains("5 days chosen."));
        assertEquals(5, maxDays1);

        // Invalid then valid
        provideMultipleInput(List.of("1000", "5"));
        final int maxDays2 = setupCLI.chooseMaxDaysInterface();
        assertEquals(5, maxDays2);
        assertTrue(acquireOutput().contains("Invalid input! (Must be a number between 5 and 15 inclusive)"));
    }

    /**
     * SetupCLI's <code>selectDifficultyInterface</code>:
     * <ul>
     * <li>Return a valid difficulty scale if given a valid integer (1-3 inclusive)</li>
     * <li>Return only the valid difficulty if given multiple inputs (last 1 valid and the remaining are invalid)</li>
     * </ul>
     */
    @Test
    void selectDifficultyInterface() {
        // Valid difficulty
        provideInput("1");
        final int difficulty1 = setupCLI.selectDifficultyInterface();
        assertEquals(1, difficulty1);


        // Invalid once and then valid
        provideMultipleInput(List.of("100", "3"));
        final int difficulty2 = setupCLI.selectDifficultyInterface();
        assertEquals(3, difficulty2);
    }

    /**
     * SetupCLI's <code>selectStartingMonsterInterface</code>:
     * <ul>
     * <li>Return a monster picked from the starter selection if given a valid integer (1-3 inclusive)</li>
     * <li>Rename the monster if given a name as the second input,</li>
     * <li>Skip and loop again if given invalid index</li>
     * </ul>
     */
    @Test
    void selectStartingMonsterInterface() {
        // Valid monster index
        provideMultipleInput(List.of("1", "Honkers"));
        final Monster startingMonster1 = setupCLI.selectStartingMonsterInterface();
        assertEquals("Quacker", startingMonster1.monsterType());
        assertEquals(3, startingMonster1.getLevel());
        assertEquals("Honkers", startingMonster1.getName());


        // Invalid once, and then valid index
        provideMultipleInput(List.of("100", "2", "Crab"));
        final Monster startingMonster2 = setupCLI.selectStartingMonsterInterface();
        assertTrue(acquireOutput().contains("Invalid input!"));
        assertEquals("Raver", startingMonster2.monsterType());
        assertEquals(3, startingMonster2.getLevel());
        assertEquals("Crab", startingMonster2.getName());
    }

    /**
     * SetupCLI's <code>chooseName</code>:
     * <ul>
     * <li>Return a valid string if given a valid username (3-15 characters, just letters)</li>
     * <li>Return only the valid string if given multiple inputs (last 1 valid and the remaining are invalid)</li>
     * </ul>
     */
    @Test
    void chooseName() {
        // Valid input
        final String name1 = setupCLI.chooseName("ThisIsValid");
        assertEquals("ThisIsValid", name1);

        // Invalid (just symbols, mixed w/ numbers, mixed all, 2 characters, over 15 characters, then valid
        provideMultipleInput(List.of("Invalid1", "Invalid#$2", "aa", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "ThisIsValid"));
        final String name2 = setupCLI.chooseName("#*(#))*");
        assertEquals("ThisIsValid", name2);
    }

    /**
     * SetupCLI's <code>chooseMaxDays</code>:
     * <ul>
     * <li>Return a valid max days if given a valid integer (5-15 inclusive)</li>
     * <li>Return only the valid max days if given multiple inputs (last 1 valid and the remaining are invalid)</li>
     * </ul>
     */
    @Test
    void chooseMaxDays() {
        // Valid input
        final int maxDays1 = setupCLI.chooseMaxDays(5);
        assertTrue(acquireOutput().contains("5 days chosen."));
        assertEquals(5, maxDays1);

        // Invalid then valid
        provideInput("5");
        final int maxDays2 = setupCLI.chooseMaxDays(1000);
        assertEquals(5, maxDays2);
        assertTrue(acquireOutput().contains("Invalid input! (Must be a number between 5 and 15 inclusive)"));
    }

    /**
     * SetupCLI's <code>selectDifficulty</code>:
     * <ul>
     * <li>Return a valid difficulty scale if given a valid integer (1-3 inclusive)</li>
     * <li>Return only the valid difficulty if given multiple inputs (last 1 valid and the remaining are invalid)</li>
     * </ul>
     */
    @Test
    void selectDifficulty() {
        // Valid difficulty
        final int difficulty1 = setupCLI.selectDifficulty(1);
        assertEquals(1, difficulty1);


        // Invalid once and then valid
        provideInput("3");
        final int difficulty2 = setupCLI.selectDifficulty(100);
        assertEquals(3, difficulty2);
    }

    /**
     * SetupCLI's <code>selectStartingMonster</code>:
     * <ul>
     * <li>Return a monster picked from the starter selection if given a valid integer (1-3 inclusive)</li>
     * <li>Rename the monster if given a name as the second input,</li>
     * <li>Skip and loop again if given invalid index</li>
     * </ul>
     */
    @Test
    void selectStartingMonster() {
        // Valid monster index
        provideInput("Honkers");
        final Monster startingMonster1 = setupCLI.selectStartingMonster(1);
        assertEquals("Quacker", startingMonster1.monsterType());
        assertEquals(3, startingMonster1.getLevel());
        assertEquals("Honkers", startingMonster1.getName());


        // Invalid once, and then valid index
        provideMultipleInput(List.of("2", "Crab"));
        final Monster startingMonster2 = setupCLI.selectStartingMonster(100);
        assertTrue(acquireOutput().contains("Invalid input!"));
        assertEquals("Raver", startingMonster2.monsterType());
        assertEquals(3, startingMonster2.getLevel());
        assertEquals("Crab", startingMonster2.getName());
    }
}