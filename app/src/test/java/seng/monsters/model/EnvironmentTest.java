package seng.monsters.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnvironmentTest {

    /**
     * Check if <code>Environment.all()</code> method:
     * <ul>
     * <li> Give a unique list of all environments </li>
     * <li> Give a list of all possible environments </li>
     * </ul>
     */
    @Test
    void all() {
        final var res = Environment.all();
        final var unique = new HashSet<>(res);

        // Check if all items are unique
        assertEquals(unique.size(), res.size());

        // Check if all item are included
        assertTrue(
            Arrays.stream(Environment.values())
                .map(unique::contains)
                .reduce(true, (acc, bool) -> acc && bool)
        );
    }

    /**
     * Check if <code>Environment.toString()</code> method:
     * <ul>
     * <li> Convert the enum into a string by its label in lowercase </li>
     * </ul>
     */
    @Test
    void testToString() {
        assertEquals("Urban", Environment.URBAN.toString());
        assertEquals("Beach", Environment.BEACH.toString());
        assertEquals("Desert", Environment.DESERT.toString());
        assertEquals("Field", Environment.FIELD.toString());
        assertEquals("Forest", Environment.FOREST.toString());
        assertEquals("Hill", Environment.HILL.toString());
    }
}