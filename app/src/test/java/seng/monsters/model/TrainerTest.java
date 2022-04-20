package seng.monsters.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrainerTest {

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer("Trainer");
    }

    @AfterEach
    void tearDown() {
        trainer.getParty().clear();
    }

    /**
     * Trainer <code>add()</code> should:
     * <ul>
     * <li>Append newly added monster into the ArrayList</li>
     * <li>Append monsters up to 4</li>
     * <li>Not dd new monsters if party is at size 4</li>
     * </ul>
     */
    @Test
    void add() {
        trainer.add(new Monster.Eel(100));
        assertEquals(1, trainer.getParty().size());

        trainer.add(new Monster.Quacker(100));
        trainer.add(new Monster.Raver(100));
        trainer.add(new Monster.Tree(100));
        assertEquals(4, trainer.getParty().size());

        assertThrows(Trainer.PartyFullException.class, () -> trainer.add(new Monster.Eel(1)));
    }

    /**
     * Trainer <code>remove()</code> should:
     * <ul>
     * <li>Remove monster by their index</li>
     * <li>Not remove any monster if the index is invalid</li>
     * </ul>
     */
    @Test
    void remove() {
        trainer.add(new Monster.Quacker(1));
        trainer.remove(0);
        assertEquals(0, trainer.getParty().size());

        trainer.remove(0);
        assertEquals(0, trainer.getParty().size());

        trainer.add(new Monster.Quacker(2));
        trainer.remove(2);
        assertEquals(1, trainer.getParty().size());
    }

    /**
     * Trainer <code>remove()</code> should:
     * <ul>
     * <li>Remove monster using the monster itself</li>
     * <li>Not remove any monster if monster doesn't exist in trainer's party</li>
     * </ul>
     */
    @Test
    void testRemove() {
        final var mon = new Monster.Eel(1);
        final var notMon = new Monster.Eel(1);
        trainer.add(mon);
        trainer.remove(notMon);
        assertEquals(1, trainer.getParty().size());

        trainer.remove(mon);
        assertEquals(0, trainer.getParty().size());

        trainer.remove(mon);
        assertEquals(0, trainer.getParty().size());
    }

    /**
     * Trainer <code>pop()</code> should:
     * <ul>
     * <li>Remove monster by index and return it</li>
     * <li>Not remove any monster nad throw an Exception if given invalid index</li>
     * </ul>
     */
    @Test
    void pop() {
        final var mon = new Monster.Eel("Bob", 10);
        trainer.add(mon);

        assertThrows(IndexOutOfBoundsException.class, () -> trainer.pop(1));

        final var popped = trainer.pop(0);
        assertEquals(mon, popped);
        assertEquals("Bob", popped.getName());

        assertThrows(IndexOutOfBoundsException.class, () -> trainer.pop(0));
    }

    /**
     * Trainer <code>moveMonster()</code> should:
     * <ul>
     * <li>Move / Switch a monster order</li>
     * <li>Not move anything and throw an Exception if the monster given doesn't exist</li>
     * </ul>
     */
    @Test
    void moveMonster() {
        final var mon0 = new Monster.Eel("Bob", 10);
        final var mon1 = new Monster.Quacker("Paul", 10);
        final var mon3 = new Monster.Raver("Not In", 10);
        trainer.add(mon0);
        trainer.add(mon1);

        trainer.moveMonster(mon0, 1);
        assertEquals(mon0, trainer.getParty().get(1));

        trainer.moveMonster(mon1, 0);
        assertEquals(mon1, trainer.getParty().get(0));

        assertThrows(IndexOutOfBoundsException.class, () -> trainer.moveMonster(mon3, 0));
    }

    /**
     * Trainer <code>switchMonster()</code> should:
     * <ul>
     * <li>Switch two monster by their indices</li>
     * <li>Not move anything and throw an Exception if any of the index is invalid</li>
     * </ul>
     */
    @Test
    void switchMonster() {
        final var mon0 = new Monster.Eel("Bob", 10);
        final var mon1 = new Monster.Quacker("Paul", 10);
        trainer.add(mon0);
        trainer.add(mon1);

        trainer.switchMonster(0, 1);
        assertEquals(mon0, trainer.getParty().get(1));

        trainer.switchMonster(0, 0);
        assertEquals(mon1, trainer.getParty().get(0));

        assertThrows(IndexOutOfBoundsException.class, () -> trainer.switchMonster(2, 69));
    }

    /**
     * Trainer <code>setName()</code> should:
     * <ul>
     * <li>Change the name of the trainer</li>
     * </ul>
     */
    @Test
    void setName() {
        trainer.setName("Amongus");
        assertEquals("Amongus", trainer.getName());
    }

    /**
     * Trainer <code>isWhiteOut()</code> should:
     * <ul>
     * <li>Return true if player has no monsters</li>
     * <li>Return true if player has no active monster</li>
     * <li>Return false otherwise </li>
     * </ul>
     */
    @Test
    void isWhiteOut() {
        assertTrue(trainer.isWhitedOut());

        trainer.add(new Monster.Doger(10));

        assertFalse(trainer.isWhitedOut());

        trainer.getParty().forEach(mon -> mon.takeDamage(mon.maxHp()));

        assertTrue(trainer.isWhitedOut());
    }
}