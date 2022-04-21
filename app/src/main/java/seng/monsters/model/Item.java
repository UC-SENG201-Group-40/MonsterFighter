//
//  Item.java
//  seng-practice
//
//  Created by d-exclaimation on 3:27 PM.
//  Copyright © 2022 d-exclaimation. All rights reserved.
//
package seng.monsters.model;

import java.util.List;
import java.util.Objects;

/**
 * A usable item that can be applied to a monster to produce a variety of result
 */
public abstract class Item implements Purchasable {

    /** Potion to heal monsters */
    public static class Potion extends Item {
        /**
         * Create a new Item with a name
         */
        public Potion() {
            super("Potion");
        }

        @Override
        public void applyTo(Monster mon) throws NoEffectException {
            if (mon.isFainted())
                throw new NoEffectException("The monster is dead");
            if (mon.getCurrentHp() == mon.maxHp())
                throw new NoEffectException("The monster hp is full");
            mon.healSelf(50);
        }

        @Override
        public int buyPrice() {
            return 25;
        }

        @Override
        public int sellPrice() {
            return buyPrice() / 2;
        }
    }

    /** Revive to heal fainted monsters */
    public static class Revive extends Item {

        /**
         * Create a new Item with a name
         */
        public Revive() {
            super("Revive");
        }

        @Override
        public void applyTo(Monster mon) throws NoEffectException {
            if (!mon.isFainted())
                throw new NoEffectException("The monster is not dead");
            mon.healSelf(mon.maxHp() / 4);
        }

        @Override
        public int buyPrice() {
            return 30;
        }

        @Override
        public int sellPrice() {
            return buyPrice() / 2;
        }
    }

    /** RareCandy to level up a monster */
    public static class RareCandy extends Item {
        /**
         * Create a new Item with a name
         */
        public RareCandy() {
            super("RareCandy");
        }

        @Override
        public void applyTo( Monster mon) throws NoEffectException {
            mon.levelUp();
        }

        @Override
        public int buyPrice() {
            return 100;
        }

        @Override
        public int sellPrice() {
            return buyPrice() / 2;
        }
    }

    /** FullRestore restores health to full regardless if monster is fainted or not */
    public static class FullRestore extends Item {
        /**
         * Create a new Item with a name
         */
        public FullRestore() {
            super("FullRestore");
        }

        @Override
        public void applyTo(Monster mon) throws NoEffectException {
            if (mon.getCurrentHp() == mon.maxHp())
                throw new NoEffectException("The monster hp is full");
            mon.healSelf(mon.maxHp());
        }

        @Override
        public int buyPrice() {
            return 1000;
        }

        @Override
        public int sellPrice() {
            return buyPrice() / 2;
        }
    }
    /**
     * Signals that aan item has been applied to a monster but produce no result
     */
    public static final class NoEffectException extends IllegalStateException {
        public NoEffectException(String desc) {
            super(desc);
        }
    }

    /**
     * The name of the item
     */
    private final String name;

    /**
     * Create a new Item with a name
     *
     * @param name The name of the item
     */
    public Item(String name) {
        this.name = name;
    }

    /**
     * Apply this item to a monster
     *
     * @param mon The target monster
     * @throws NoEffectException if the effect gave no changes
     */
    public abstract void applyTo(Monster mon) throws NoEffectException;

    @Override
    public abstract int buyPrice();

    @Override
    public abstract int sellPrice();

    /**
     * Access the name of this item
     *
     * @return The name of this item
     */
    public String getName() {
        return name;
    }

    /**
     * Comparing the item with another
     * @param o The item to be compared to
     * @return A boolean signalling whether the item is identical
     */
    public boolean equals(Object o) {
        if (o instanceof Item item)
            return this.hashCode() == item.hashCode();
        return false;
    }

    /**
     * The hashcode for the type of item
     * @return The integer hashed from the type name
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }


    /**
     * All the items possible
     * @return A list of all unique items
     */
    public static List<Item> all() {
        return List.of(new Item.Potion(), new Item.Revive(), new Item.RareCandy(), new Item.FullRestore());
    }
}
