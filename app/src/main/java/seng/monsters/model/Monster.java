//
//  Monster.java
//  seng-practice
//
//  Created by d-exclaimation on 3:29 PM.
//  Copyright © 2022 d-exclaimation. All rights reserved.
//
package seng.monsters.model;


import java.util.List;
import java.util.UUID;

/**
 * An entity that can fight in a battle with a variety of stats
 */
public abstract class Monster implements Purchasable {
    /**
     * A Quacker duck (Jack of all trades)
     */
    public static final class Quacker extends Monster {
        /**
         * Create a new monster
         *
         * @param level The current level
         */
        public Quacker(int level) {
            super("Quacker", 80, level);
        }

        /**
         * Create a new monster
         *
         * @param name  The name of the monster
         * @param level The current level
         */
        public Quacker(String name, int level) {
            super(name, 80, level);
        }

        @Override
        public int baseDamage() {
            return 45;
        }

        @Override
        public int speed() {
            return 40;
        }

        @Override
        public int healRate() {
            return 40;
        }

        @Override
        public Environment idealEnvironment() {
            return Environment.BEACH;
        }

        @Override
        public boolean shouldLevelUp() {
            return Math.random() <= 0.4;
        }

        @Override
        public boolean shouldLeave() {
            final var line = isFainted() ? 0.25 : 0.01;
            return Math.random() <= line;
        }
    }

    /**
     * A Raver crab (Damage sponge, doesn't fight back well)
     */
    public static final class Raver extends Monster {
        /**
         * Create a new monster
         *
         * @param level The current level
         */
        public Raver(int level) {
            super("Raver", 200, level);
        }

        /**
         * Create a new monster
         *
         * @param name  The name of the monster
         * @param level The current level
         */
        public Raver(String name, int level) {
            super(name, 200, level);
        }

        @Override
        public int baseDamage() {
            return 20;
        }

        @Override
        public int speed() {
            return 20;
        }

        @Override
        public int healRate() {
            return 75;
        }

        @Override
        public Environment idealEnvironment() {
            return Environment.DESERT;
        }

        @Override
        public boolean shouldLevelUp() {
            return Math.random() <= 0.4;
        }

        @Override
        public boolean shouldLeave() {
            final var line = isFainted() ? 0.2 : 0.05;
            return Math.random() <= line;
        }
    }

    /**
     * A Tree, that's it (Long lasting, always in full health before battle)
     */
    public static final class Tree extends Monster {
        /**
         * Create a new monster
         *
         * @param level The current level
         */
        public Tree(int level) {
            super("Tree", 60, level);
        }

        /**
         * Create a new monster
         *
         * @param name  The name of the monster
         * @param level The current level
         */
        public Tree(String name, int level) {
            super(name, 60, level);
        }

        @Override
        public int baseDamage() {
            return 40;
        }

        @Override
        public int speed() {
            return 40;
        }

        @Override
        public int healRate() {
            return 200;
        }

        @Override
        public Environment idealEnvironment() {
            return Environment.FOREST;
        }

        @Override
        public boolean shouldLevelUp() {
            return Math.random() <= 0.3;
        }

        @Override
        public boolean shouldLeave() {
            return Math.random() <= (isFainted() ? 0.05 : 0.01);
        }
    }

    /**
     * An eel that can steals health (Heals in battle, but mega slow)
     */
    public static final class Eel extends Monster {
        /**
         * Create a new monster
         *
         * @param level The current level
         */
        public Eel(int level) {
            super("Eel", 60, level);
        }

        /**
         * Create a new monster
         *
         * @param name  The name of the monster
         * @param level The current level
         */
        public Eel(String name, int level) {
            super(name, 60, level);
        }

        @Override
        public int baseDamage() {
            return 40;
        }

        @Override
        public int speed() {
            return 10;
        }

        @Override
        public int healRate() {
            return 50;
        }

        @Override
        public int damage(Environment env) {
            final var res = super.damage(env);
            healSelf(res / 5);
            return res;
        }

        @Override
        public Environment idealEnvironment() {
            return Environment.DESERT;
        }

        @Override
        public boolean shouldLevelUp() {
            return Math.random() <= 0.4;
        }

        @Override
        public boolean shouldLeave() {
            return Math.random() <= (isFainted() ? 0.25 : 0.01);
        }
    }

    /**
     * A speedy boy (Fast, hits pretty good, but might die afterward)
     */
    public static final class Doger extends Monster {
        /**
         * Create a new monster
         *
         * @param level The current level
         */
        public Doger(int level) {
            super("Doger", 40, level);
        }

        /**
         * Create a new monster
         *
         * @param name  The name of the monster
         * @param level The current level
         */
        public Doger(String name, int level) {
            super(name, 40, level);
        }

        @Override
        public int baseDamage() {
            return 80;
        }

        @Override
        public int speed() {
            return 100;
        }

        @Override
        public int healRate() {
            return 10;
        }

        @Override
        public Environment idealEnvironment() {
            return Environment.URBAN;
        }

        @Override
        public boolean shouldLevelUp() {
            return Math.random() <= 0.5;
        }

        @Override
        public boolean shouldLeave() {
            return Math.random() <= (isFainted() ? 0.25 : 0.01);
        }
    }

    /**
     * Definitely a shark (Hit hard, if it doesn't die first)
     */
    public static final class Shark extends Monster {
        /**
         * Create a new monster
         *
         * @param level The current level
         */
        public Shark(int level) {
            super("Shark", 50, level);
        }

        /**
         * Create a new monster
         *
         * @param name  The name of the monster
         * @param level The current level
         */
        public Shark(String name, int level) {
            super(name, 50, level);
        }

        @Override
        public int baseDamage() {
            return 120;
        }

        @Override
        public int speed() {
            return 20;
        }

        @Override
        public int healRate() {
            return 20;
        }

        @Override
        public Environment idealEnvironment() {
            return Environment.BEACH;
        }

        @Override
        public boolean shouldLevelUp() {
            return Math.random() <= 0.5;
        }

        @Override
        public boolean shouldLeave() {
            return Math.random() <= (isFainted() ? 0.25 : 0.01);
        }
    }

    /**
     * Unique id
     */
    private final UUID id = UUID.randomUUID();

    /**
     * The name of the monster
     */
    private String name;
    /**
     * The current hp of the monster
     */
    private int currentHp;
    /**
     * The level of this monster, that increase all others stats
     */
    private int level;
    /**
     * The initial max hp at the lowest level
     */
    private int baseHp;

    /**
     * Create a new monster
     *
     * @param name   The name of the monster
     * @param baseHp The base hp
     * @param level  The current level
     */
    public Monster(String name, int baseHp, int level) {
        this.name = name;
        this.level = level;
        this.baseHp = baseHp;
        this.currentHp = maxHp();
    }

    /**
     * The base damage output of this monster on level 1
     *
     * @return The damage in integer
     */
    public abstract int baseDamage();

    /**
     * The speed used to determine the which monster attack first
     *
     * @return The speed in integer
     */
    public abstract int speed();

    /**
     * The amount of health point healed for this monster
     *
     * @return The amount in integer
     */
    public abstract int healRate();

    /**
     * The preferred environment
     *
     * @return The environment enum
     */
    public abstract Environment idealEnvironment();

    /**
     * The randomized chance for the monster to level up overnight
     *
     * @return A boolean for whether the monster should level up
     */
    public abstract boolean shouldLevelUp();

    /**
     * The randomized chance for the monster to leave
     *
     * @return A boolean for whether the monster should leave
     */
    public abstract boolean shouldLeave();

    /**
     * The multiplier computed by level
     *
     * @return A level multiplier double
     */
    private double multiplier() {
        return Math.pow(1.1, this.level - 1);
    }

    /**
     * Increase the level of the monster and all its properties
     */
    public void levelUp() {
        level += 1;
        this.currentHp = maxHp();
    }

    /**
     * Heal self with a certain amount
     *
     * @param amount The amount healed
     */
    public void healSelf(int amount) {
        if (amount < 0)
            return;
        this.currentHp = Math.min(currentHp + amount, maxHp());
    }

    /**
     * Damage self with a certain amount
     *
     * @param amount The amount damaged
     */
    public void takeDamage(int amount) {
        if (amount < 0)
            return;
        this.currentHp = Math.max(currentHp - amount, 0);
    }

    /**
     * Set the name of the monster
     *
     * @param name The new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the base hp of the monster
     *
     * @param baseHp The new base hp
     */
    public void setBaseHp(int baseHp) {
        if (baseHp < 0)
            return;
        this.baseHp = baseHp;
        if (this.currentHp > maxHp()) {
            this.currentHp = maxHp();
        }
    }


    /**
     * The maximum hp for this level
     *
     * @return The max hp in integer
     */
    public int maxHp() {
        return (int) Math.min(Integer.MAX_VALUE - 1, this.baseHp * multiplier());
    }

    /**
     * The produce the damage given the proper environment
     *
     * @return The damage taking level
     */
    public int scaledDamage() {
        final var res = Math.min(
            Integer.MAX_VALUE - 1,
            baseDamage() * multiplier()
        );
        return (int) res;
    }

    /**
     * The produce the damage given the proper environment
     *
     * @param env The environment of the battlefield
     * @return The damage taking account the environment boost
     */
    public int damage(Environment env) {
        final var envMultiplier = env == idealEnvironment() ? 1.5 : 1;
        final var res = Math.min(
            Integer.MAX_VALUE - 1,
            baseDamage() * multiplier() * envMultiplier
        );
        return (int) res;
    }

    /**
     * Signal if the monster is fainted and needed to be revived
     *
     * @return A boolean signalling whether monster is fainted
     */
    public boolean isFainted() {
        return currentHp <= 0;
    }

    /**
     * The cost to buy this monster
     *
     * @return The integer representing the price
     */
    public int buyPrice() {
        final var basePrice = Math.min(
            Integer.MAX_VALUE - 1,
            (baseHp + baseDamage() + healRate() + speed()) * multiplier()
        );
        return (int) basePrice;
    }

    /**
     * The cost to sell this monster
     *
     * @return The integer representing the price
     */
    public int sellPrice() {
        return buyPrice() / 2;
    }

    /**
     * Get the current level
     *
     * @return The level of the monster
     */
    public int getLevel() {
        return level;
    }

    /**
     * Get the current name
     *
     * @return The name of the monster
     */
    public String getName() {
        return name;
    }

    /**
     * Get the current hp
     *
     * @return The current hp of the monster
     */
    public int getCurrentHp() {
        return currentHp;
    }

    /**
     * Get the max hp
     *
     * @return The max hp of the monster
     */
    public int getMaxHp() { return maxHp(); }

    /**
     * Get the id
     *
     * @return The id of the monster
     */
    public UUID getId() {
        return id;
    }

    /**
     * Compared the monster with another
     *
     * @param other The other to compared
     * @return A boolean whether the monster is the same
     */
    public boolean equals(Object other) {
        if (other instanceof Monster mon)
            return this.getId().equals(mon.getId());
        return false;
    }

    /**
     * Get all types of monster with a specific level
     *
     * @param level The level all monster is set to
     * @return A list of unique monsters set to the level given
     */
    public static List<Monster> all(int level) {
        return List.of(
            new Monster.Quacker(level),
            new Monster.Raver(level),
            new Monster.Tree(level),
            new Monster.Eel(level),
            new Monster.Doger(level),
            new Monster.Shark(level)
        );
    }
}
