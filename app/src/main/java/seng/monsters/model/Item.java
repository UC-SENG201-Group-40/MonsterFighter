//
//  Item.java
//  seng-practice
//
//  Created by Vincent on 3:27 PM.

//
package seng.monsters.model;

import java.util.List;
import java.util.Objects;

/**
 * A usable item that can be applied to a monster to produce a variety of result.
 */
public abstract class Item implements Purchasable {

    /**
     * Potion to heal monsters.
     */
    public static class Potion extends Item {

        @Override
        public void applyTo(Monster mon) throws NoEffectException {
            if (mon.isFainted())
                // Error if monster is fainted
                throw new NoEffectException("The monster is dead");
            if (mon.getCurrentHp() == mon.maxHp())
                // Error if monster as max Hp
                throw new NoEffectException("The monster hp is full");
            mon.healSelf(50);
        }

        @Override
        public int buyPrice() {
            return 25;
        }

        @Override
        public String description() {
            return "+50 HP (Heal only)";
        }

        @Override
        public int sellPrice() {
            return buyPrice() / 2;
        }
    }

    /**
     * Revive to heal fainted monsters.
     */
    public static class Revive extends Item {

        @Override
        public void applyTo(Monster mon) throws NoEffectException {
            if (!mon.isFainted())
                // Error if monster applying to is not fainted
                throw new NoEffectException("The monster is not dead");
            mon.healSelf(mon.maxHp() / 4);
        }

        @Override
        public String description() {
            return "+25% HP (Revive only)";
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

    /**
     * RareCandy to level up a monster.
     */
    public static class RareCandy extends Item {

        @Override
        public void applyTo(Monster mon) throws NoEffectException {
            mon.levelUp();
        }

        @Override
        public String description() {
            return "+1 Level";
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

    /**
     * FullRestore restores health to full regardless if monster is fainted or not.
     */
    public static class FullRestore extends Item {

        @Override
        public void applyTo(Monster mon) throws NoEffectException {
            if (mon.getCurrentHp() == mon.maxHp())
                // Error if monster's hp is full
                throw new NoEffectException("The monster hp is full");
            mon.healSelf(mon.maxHp());
        }

        @Override
        public String description() {
            return "+100% HP";
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
     * Signals that an item has been applied to a monster but produce no result.
     */
    public static final class NoEffectException extends IllegalStateException {
        /**
         * Creates an Exception that indicates that the item produces no result on the monster.
         * @param desc The reasoning why the item produces no result as a string.
         */
        public NoEffectException(String desc) {
            super(desc);
        }
    }

    /**
     * Apply this item to a monster.
     *
     * @param mon The target monster.
     * @throws NoEffectException if the effect gave no changes.
     */
    public abstract void applyTo(Monster mon) throws NoEffectException;

    /**
     * Returns the buy price of the item.
     *
     * @return The buy price as an int.
     */
    @Override
    public abstract int buyPrice();

    /**
     * Computes and returns the sell price of the item.
     *
     * @return The sell price of the item.
     */
    @Override
    public abstract int sellPrice();

    /**
     * Access the name of this item.
     *
     * @return The name of this item as a string.
     */
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Access the description of this item
     *
     * @return The description of the item as a string.
     */
    @Override
    public abstract String description();

    /**
     * Comparing the item with another
     *
     * @param o The item to be compared to
     * @return A boolean signaling whether the item is identical
     */
    public boolean equals(Object o) {
        if (o instanceof Item item)
            return this.hashCode() == item.hashCode();
        return false;
    }

    /**
     * The hashcode for the type of item
     *
     * @return The integer hashed from the type name
     */
    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }


    /**
     * All the items possible
     *
     * @return A list of all unique items
     */
    public static List<Item> all() {
        return List.of(new Item.Potion(), new Item.Revive(), new Item.RareCandy(), new Item.FullRestore());
    }
}