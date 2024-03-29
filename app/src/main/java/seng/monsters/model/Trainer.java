//
//  Trainer.java
//  seng-practice
//
//  Created by Vincent on 3:32 PM.

//
package seng.monsters.model;

import java.util.*;

/**
 * AN entity with a party of monsters.
 */
public final class Trainer {
    /**
     * Signals that add is called when the party reach its maximum size.
     */
    public static final class PartyFullException extends IllegalStateException {
        /**
         * Creates a party full exception with a description.
         * @param desc The description of why the exception was thrown.
         */
        public PartyFullException(String desc) {
            super(desc);
        }
    }

    /**
     * Signals that the caller has attempted to index a monster that does not exist.
     */
    public static final class MonsterDoesNotExistException extends IndexOutOfBoundsException {
    }

    /**
     * The name of the trainer.
     */
    private String name;

    /**
     * The party of monsters.
     */
    private final ArrayList<Monster> party = new ArrayList<>(4);

    /**
     * Create a new instance of Trainer.
     *
     * @param name Name of the trainer.
     */
    public Trainer(String name) {
        this.name = name;
    }

    /**
     * Add a new monster into a party if possible.
     *
     * @param mon The monster added.
     * @throws PartyFullException Monsters cannot be added if party has already reached 4 monsters.
     */
    public void add(Monster mon) throws PartyFullException {
        if (party.size() >= 4)
            // Error if party is full
            throw new PartyFullException("Cannot add more than 4 monster");
        if (party.contains(mon))
            return;
        party.add(mon);
    }

    /**
     * Remove a monster from a party.
     *
     * @param index The index of the monster.
     */
    public void remove(int index) {
        // Does nothing if index is invalid
        if (index < 0 || index >= party.size()) return;
        party.remove(index);
    }

    /**
     * Remove a monster from a party.
     *
     * @param monster The monster to be removed.
     */
    public void remove(Monster monster) {
        final int i = party.indexOf(monster);
        remove(i);
    }

    /**
     * Remove a monster from the party and return it.
     *
     * @param index The index of the monster.
     * @return The monster removed.
     * @throws IndexOutOfBoundsException If the index is invalid.
     */
    public Monster pop(int index) throws IndexOutOfBoundsException {
        final Monster monster = party.get(index);
        party.remove(index);
        return monster;
    }

    /**
     * Switch the ordering of two monster.
     *
     * @param monster The monster being moved.
     * @param to      The index to removed to.
     * @throws IndexOutOfBoundsException If either the index is out of bound.
     */
    public void moveMonster(Monster monster, int to) throws IndexOutOfBoundsException {
        final int from = party.indexOf(monster);
        switchMonster(from, to);
    }

    /**
     * Switch the ordering of two monster.
     *
     * @param from One of the index of the monster being swapped.
     * @param to   The other index.
     * @throws IndexOutOfBoundsException If either the index is out of bound.
     */
    public void switchMonster(int from, int to) throws IndexOutOfBoundsException {
        // Does nothing if the monster is "moved to itself"
        if (from != to) {
            final Monster temp = party.get(from);
            party.set(from, party.get(to));
            party.set(to, temp);
        }
    }

    /**
     * Get the party as it is with all the nulls.
     *
     * @return All nullable monster in the party.
     */
    public List<Monster> getParty() {
        return party;
    }

    /**
     * Get the name of the trainer
     *
     * @return The trainer's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Change the name of the trainer.
     *
     * @param name The new name.
     */
    public void setName(String name) throws IllegalArgumentException {
        if ((name.length() < 3) || (name.length() > 15) || (!name.matches("[a-zA-Z]+")))
            // Error if name is invalid
            throw new IllegalArgumentException();
        this.name = name;
    }

    /**
     * Check if the trainer has any monster active.
     *
     * @return true if all monster is fainted.
     */
    public boolean isWhitedOut() {
        return party.stream().allMatch(Monster::isFainted);
    }
}
