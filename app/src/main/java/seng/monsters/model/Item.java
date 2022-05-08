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
 * A usable item that can be applied to a monster to produce a variety of result
 */
public abstract class Item implements Purchasable {

    /** Potion to heal monsters */
    public static class Potion extends Item {

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
		public String description() {
			return "+50 HP (Heal only)";
		}

        @Override
        public int sellPrice() {
            return buyPrice() / 2;
        }
    }

    /** Revive to heal fainted monsters */
    public static class Revive extends Item {

        @Override
        public void applyTo(Monster mon) throws NoEffectException {
            if (!mon.isFainted())
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

        @Override
        public void applyTo(Monster mon) throws NoEffectException {
            if (mon.getCurrentHp() == mon.maxHp())
                throw new NoEffectException("The monster hp is full");
            mon.healSelf(mon.maxHp());
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

        @Override
        public void applyTo( Monster mon) throws NoEffectException {
            mon.levelUp();
        }
        
		@Override
		public String description() {
			return "+100% HP";
		}

        @Override
        public int buyPrice() {
            return 300;
        }

        @Override
        public int sellPrice() {
            return buyPrice() / 2;
        }
    }

    /**
     * Signals that an item has been applied to a monster but produce no result
     */
    public static final class NoEffectException extends IllegalStateException {
        public NoEffectException(String desc) {
            super(desc);
        }
    }

     * Apply this item to a monster
     *
     * @param mon The target monster
     * @throws NoEffectException if the effect gave no changes
     */
    public abstract void applyTo(Monster mon) throws NoEffectException;
    
    /**
     * A quick description of what this item does
     * @return
     */
    public abstract String description();

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
        return this.getClass().getSimpleName();
    }

    /**
     * Get the description of this item
     *
     * @return The description of this item
     */
    public String getDesc() { return desc; }

    /**
     * Comparing the item with another
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
     * @return The integer hashed from the type name
     */
    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }


    /**
     * All the items possible
     * @return A list of all unique items
     */
    public static List<Item> all() {
        return List.of(new Item.Potion(), new Item.Revive(), new Item.RareCandy(), new Item.FullRestore());
    }
}
