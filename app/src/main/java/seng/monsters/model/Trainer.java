//
//  Trainer.java
//  seng-practice
//
//  Created by d-exclaimation on 3:32 PM.
//  Copyright © 2022 d-exclaimation. All rights reserved.
//
package seng.monsters.model;

import java.util.ArrayList;
import java.util.List;

/**
 * AN entity with a party of monsters
 */
public final class Trainer {
    /**
     * Signals that add is called when the party reach its maximum size
     */
    static final class PartyFullException extends IllegalStateException {
        public PartyFullException(String desc) {
            super(desc);
        }
    }

    /**
     * Signals that the caller has attempted to index a monster that does not exist
     */
    static final class MonsterDoesNotExistException extends IndexOutOfBoundsException {
    }

    /**
     * The name of the trainer
     */
    private String name;

    /**
     * The party of monsters
     */
    private final ArrayList<Monster> party = new ArrayList<>(4);

    /**
     * Create a new instance of Trainer
     *
     * @param name Name of the trainer
     */
    public Trainer(String name) {
        this.name = name;
    }

    /**
     * Add a new monster into a party if possible
     *
     * @param mon The monster added
     * @throws PartyFullException Monsters cannot be added if party has already reached 4 monsters
     */
    public void add(Monster mon) throws PartyFullException {
        if (party.size() >= 4)
            throw new PartyFullException("Cannot add more than 4 monster");
        party.add(mon);
    }

    /**
     * Remove a monster from a party
     *
     * @param index The index of the monster
     */
    public void remove(int index) {
        if (index < 0 || index >= party.size()) return;
        party.remove(index);
    }

    /**
     * Remove a monster from a party
     *
     * @param monster The index of the monster
     */
    public void remove(Monster monster) {
        final var i = party.indexOf(monster);
        remove(i);
    }

    /**
     * Remove a monster from the party and return it
     *
     * @param index The index of the monster
     * @return The monster removed
     * @throws IndexOutOfBoundsException If the index is invalid
     */
    public Monster pop(int index) throws IndexOutOfBoundsException {
        final var monster = party.get(index);
        party.remove(index);
        return monster;
    }

    /**
     * Switch the ordering of two monster
     *
     * @param monster The monster being moved
     * @param to      The index to removed to
     * @throws IndexOutOfBoundsException If either the index is out of bound
     */
    public void moveMonster(Monster monster, int to) throws IndexOutOfBoundsException {
        final var from = party.indexOf(monster);
        switchMonster(from, to);
    }

    /**
     * Switch the ordering of two monster
     *
     * @param from One of the index of the monster being swapped
     * @param to   The other index
     * @throws IndexOutOfBoundsException If either the index is out of bound
     */
    public void switchMonster(int from, int to) throws IndexOutOfBoundsException {
        if (from != to) {
            final var temp = party.get(from);
            party.set(from, party.get(to));
            party.set(to, temp);
        }
    }

    /**
     * Get the party as it is with all the nulls
     *
     * @return All nullable monster in the party
     */
    public List<Monster> getParty() {
        return party;
    }


    /**
     * Get the name of the trainer
     *
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Chnage the name of the trainer
     *
     * @param name The new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Check if the trainer has any monster active
     * @return true if all monster is fainted
     */
    public boolean isWhitedOut() {
        return party.stream().allMatch(Monster::isFainted);
    }
}
