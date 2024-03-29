//
//  Monster.java
//  seng-practice
//
//  Created by Vincent on 3:29 PM.
//
package seng.monsters.model;


import java.util.List;
import java.util.UUID;

/**
 * An entity that can fight in a battle with a variety of stats
 */
public abstract class Monster implements Purchasable {
    /**
     * A Quacker duck (Jack of all trades).
     */
    public static final class Quacker extends Monster {
        /**
         * Create a new monster.
         *
         * @param level The current level.
         */
        public Quacker(int level) {
            super("Quacker", 80, level);
        }

        /**
         * Create a new monster with a name.
         *
         * @param name  The name of the monster.
         * @param level The current level.
         */
        public Quacker(String name, int level) {
            super(name, 80, level);
        }

        @Override
        public String description() {
            return "A Quacker Duck. Jack of all trades.";
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
            return Environment.HILL;
        }

        @Override
        public boolean shouldLevelUp() {
            return Math.random() <= 0.6;
        }

        @Override
        public boolean shouldLeave() {
            final double line = isFainted() ? 0.25 : 0.01;
            return Math.random() <= line;
        }
    }

    /**
     * A Raver crab (Damage sponge, doesn't fight back well).
     */
    public static final class Raver extends Monster {
        /**
         * Create a new monster.
         *
         * @param level The current level as an int.
         */
        public Raver(int level) {
            super("Raver", 200, level);
        }

        /**
         * Create a new monster with a name.
         *
         * @param name  The name of the monster.
         * @param level The current level.
         */
        public Raver(String name, int level) {
            super(name, 200, level);
        }

        @Override
        public String description() {
            return "A Raver crab. Weak, but tanky.";
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
            final double line = isFainted() ? 0.2 : 0.05;
            return Math.random() <= line;
        }
    }

    /**
     * A Tree, that's it (Long lasting, always in full health before battle)
     */
    public static final class Tree extends Monster {
        /**
         * Create a new monster.
         *
         * @param level The current level.
         */
        public Tree(int level) {
            super("Tree", 60, level);
        }

        /**
         * Create a new monster with a name.
         *
         * @param name  The name of the monster.
         * @param level The current level.
         */
        public Tree(String name, int level) {
            super(name, 60, level);
        }

        @Override
        public String description() {
            return "A tree... that's it. Healthy.";
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
            return !isFainted() || Math.random() <= 0.8;
        }

        @Override
        public int sellPrice() {
            return buyPrice();
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
         * Create a new monster.
         *
         * @param level The current level.
         */
        public Eel(int level) {
            super("Eel", 60, level);
        }

        /**
         * Create a new monster with a name.
         *
         * @param name  The name of the monster.
         * @param level The current level.
         */
        public Eel(String name, int level) {
            super(name, 60, level);
        }

        @Override
        public String description() {
            return "A parasitic eel. Slow but steals health during battle.";
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
            // Calls healSelf method for life steal
            final int res = super.damage(env);
            healSelf(res / 5);
            return res;
        }

        @Override
        public Environment idealEnvironment() {
            return Environment.URBAN;
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
         * Create a new monster.
         *
         * @param level The current level.
         */
        public Doger(int level) {
            super("Doger", 40, level);
        }

        /**
         * Create a new monster with a name.
         *
         * @param name  The name of the monster.
         * @param level The current level.
         */
        public Doger(String name, int level) {
            super(name, 40, level);
        }

        @Override
        public String description() {
            return "A very speedy boy. Glass cannon.";
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
         * Create a new monster.
         *
         * @param level The current level.
         */
        public Shark(int level) {
            super("Shark", 50, level);
        }

        /**
         * Create a new monster with a name.
         *
         * @param name  The name of the monster.
         * @param level The current level.
         */
        public Shark(String name, int level) {
            super(name, 50, level);
        }

        @Override
        public String description() {
            return "Definitely a shark. Jacked, but that's it.";
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
     * Unique id of the monster.
     */
    private final UUID id = UUID.randomUUID();

    /**
     * The name of the monster.
     */
    private String name;

    /**
     * The current hp of the monster.
     */
    private int currentHp;

    /**
     * The level of this monster, which increases all others stats.
     */
    private int level;

    /**
     * The initial max hp at the lowest level.
     */
    private int baseHp;

    /**
     * Create a new monster.
     *
     * @param name   The name of the monster.
     * @param baseHp The base hp.
     * @param level  The current level.
     */
    public Monster(String name, int baseHp, int level) {
        this.name = name;
        this.level = level;
        this.baseHp = baseHp;
        this.currentHp = maxHp();
    }

    /**
     * The base damage output of this monster on level 1.
     *
     * @return The monster's base damage.
     */
    public abstract int baseDamage();

    /**
     * The speed used to determine the which monster attack first.
     *
     * @return The monster's speed.
     */
    public abstract int speed();

    /**
     * The amount of health point healed overnight for this monster.
     *
     * @return The monsters healrate.
     */
    public abstract int healRate();

    /**
     * The preferred environment which gives an attack bonus.
     *
     * @return The monster's preferred environment.
     */
    public abstract Environment idealEnvironment();

    /**
     * The randomized chance for the monster to level up overnight.
     *
     * @return A boolean for whether the monster should level up.
     */
    public abstract boolean shouldLevelUp();

    /**
     * The randomized chance for the monster to leave overnight.
     *
     * @return A boolean for whether the monster should leave.
     */
    public abstract boolean shouldLeave();

    /**
     * The type of monster.
     *
     * @return The type of monster as a string.
     */
    public String monsterType() {
        return this.getClass().getSimpleName();
    }

    /**
     * The multiplier computed by level, which dictates the strength of the other stats.
     *
     * @return A level scaled multiplier.
     */
    private double multiplier() {
        return Math.pow(1.1, this.level - 1);
    }

    /**
     * Increase the level of the monster.
     */
    public void levelUp() {
        // Calculate current hp percentage to maintain the sam percentage after level up.
        final int hpPercentage = this.currentHp * 100 / maxHp();
        level += 1;
        this.currentHp = hpPercentage * maxHp() / 100;
    }

    /**
     * Heal self with a certain amount.
     *
     * @param amount The amount healed as an int.
     */
    public void healSelf(int amount) {
        if (amount < 0)
            return;
        this.currentHp = Math.min(currentHp + amount, maxHp());
    }

    /**
     * Damage self with a certain amount.
     *
     * @param amount The amount damaged as an int.
     */
    public void takeDamage(int amount) {
        if (amount < 0)
            return;
        this.currentHp = Math.max(currentHp - amount, 0);
    }

    /**
     * Set the name of the monster.
     *
     * @param name The new name as a string.
     */
    public void setName(String name) throws IllegalArgumentException {
        if ((name.length() < 3) || (name.length() > 15) || (!name.matches("[a-zA-Z]+")))
            // Error if name not valid.
            throw new IllegalArgumentException();
        this.name = name;
    }

    /**
     * Set the base hp of the monster.
     *
     * @param baseHp The new base hp as an int.
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
     * Computes and returns the maximum hp for this level.
     *
     * @return The max hp as an int.
     */
    public int maxHp() {
        return (int) Math.min(Integer.MAX_VALUE - 1, this.baseHp * multiplier());
    }

    /**
     * Computes and returns the damage scaled by level.
     *
     * @return The scaled damage as an int.
     */
    public int scaledDamage() {
        final double res = Math.min(
            Integer.MAX_VALUE - 1,
            baseDamage() * multiplier()
        );
        return (int) res;
    }

    /**
     * Computes and returns the damage depending on what environment the monster is in.
     *
     * @param env The environment enum of the battlefield.
     * @return The damage taking account the environment boost as an int.
     */
    public int damage(Environment env) {
        final double envMultiplier = env == idealEnvironment() ? 1.5 : 1;
        final double res = Math.min(
            Integer.MAX_VALUE - 1,
            baseDamage() * multiplier() * envMultiplier
        );
        return (int) res;
    }

    /**
     * Signal if the monster is fainted and needs to be revived.
     *
     * @return A boolean signalling whether monster is fainted.
     */
    public boolean isFainted() {
        return currentHp <= 0;
    }

    /**
     * <p>
     * Computes and returns the cost to buy this monster.
     * Calculated based on monster stats.
     * </p>
     *
     * @return The integer representing the price.
     */
    @Override
    public int buyPrice() {
        final double basePrice = Math.min(
            Integer.MAX_VALUE - 1,
            (baseHp + baseDamage() + healRate() + speed()) * multiplier()
        );
        return (int) basePrice;
    }

    /**
     * Computes and returns the cost to sell this monster.
     *
     * @return The integer representing the price.
     */
    @Override
    public int sellPrice() {
        return buyPrice() / 2;
    }

    /**
     * A description of the monster and a brief summary of its properties.
     *
     * @return The description of the monster.
     */
    @Override
    public abstract String description();

    /**
     * Get the current level.
     *
     * @return The level of the monster.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Get the current name.
     *
     * @return The name of the monster.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the current hp.
     *
     * @return The current hp of the monster.
     */
    public int getCurrentHp() {
        return currentHp;
    }

    /**
     * Get the id.
     *
     * @return The id of the monster.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Compared the monster with another monster.
     *
     * @param other The other monster to compare with.
     * @return A boolean whether the monster is the same.
     */
    public boolean equals(Object other) {
        if (other instanceof Monster mon)
            return this.getId().equals(mon.getId());
        return false;
    }

    /**
     * Gives a combination of name and id to create a unique identifier that still recognizable by name.
     *
     * @return A string of name and id combined.
     */
    public String uniqueName() {
        return String.format("%s (%s)",
            getName(),
            getId().toString().substring(0, 8)
        );
    }

    /**
     * Get all types of monster with a specific level.
     *
     * @param level The level all monster is set to as an int.
     * @return A list of unique monsters set to the level given.
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
