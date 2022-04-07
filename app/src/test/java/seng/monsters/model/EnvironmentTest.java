package seng.monsters.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnvironmentTest {

    /**
     * Check if <code>Environment.all()</code> method:
     * - Give a unique list of all environments
     * - Give a list of all possible environments
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
     * - Convert the enum into a string by its label in lowercase
     */
    @Test
    void testToString() {
        assertEquals("urban", Environment.URBAN.toString());
        assertEquals("beach", Environment.BEACH.toString());
        assertEquals("desert", Environment.DESERT.toString());
        assertEquals("field", Environment.FIELD.toString());
        assertEquals("forest", Environment.FOREST.toString());
        assertEquals("hill", Environment.HILL.toString());
    }
}