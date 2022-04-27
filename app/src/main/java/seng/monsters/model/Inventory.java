//
//  Inventory.java
//  seng-practice
//
//  Created by Vincent on 4:56 PM.

//
package seng.monsters.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A collection for item used by trainer
 */
public final class Inventory {
    /**
     * Signals that the caller intended to use an item that does not exist in the inventory
     */
    public static final class ItemNotExistException extends IndexOutOfBoundsException {
    }

    /**
     * The item collection
     */
    private final Map<Item, Integer> items = new HashMap<>();

    /**
     * Adding a new item to the inventory
     *
     * @param item The newly added item
     */
    public void add(Item item) {
        items.put(item, items.getOrDefault(item, 0) + 1);
    }

    /**
     * Use a specific item in the inventory
     *
     * @param item The item
     * @param mon  The monster to apply the item to
     * @throws ItemNotExistException  If the index reference a non-existing item
     * @throws Item.NoEffectException If the item produce no effect
     */
    public void use(Item item, Monster mon) throws ItemNotExistException, Item.NoEffectException {
        final var count = items.getOrDefault(item, 0);
        if (count <= 0)
            throw new ItemNotExistException();
        item.applyTo(mon);
        remove(item);
    }


    /**
     * Remove 1 count of Item
     *
     * @param item The item to be removed
     * @throws ItemNotExistException if the inventory holds 0 of the item
     */
    public void remove(Item item) throws ItemNotExistException {
        final var count = items.getOrDefault(item, 0);
        if (count <= 0)
            throw new ItemNotExistException();
        items.put(item, count - 1);
    }

    /**
     * Returns the amount of a specified item in the player's inventory
     * @param item The specified item to retrieve the count of
     * @return An int representing the number of the item in the player's inventory
     */
    public int getItemNumber(Item item) {
        return items.getOrDefault(item, 0);
    }

    /**
     * Get all the item in the inventory
     *
     * @return The items as an arraylist
     */
    public List<Item> getItems() {
        return items
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() > 0)
            .map(Map.Entry::getKey)
            .toList();
    }

    /**
     * Get all item in the inventory as an entry
     * @return The items and its count as entries in a ArrayList
     */
    public List<Map.Entry<Item, Integer>> getItemEntries() {
        return items
            .entrySet()
            .stream()
            .toList();
    }
}

