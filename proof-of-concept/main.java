package seng.practice.assignment;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import seng.practice.utils.Subsection;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Any class that can be bought and sold into the shop
 */
interface Purchasable {
    /**
     * The cost of the item when bought
     *
     * @return The price as integer
     */
    int buyPrice();

    /**
     * The cost of the item when sold back
     *
     * @return The price as integer
     */
    int sellPrice();
}

/**
 * Environment about the field of battle
 */
enum Environment {
    DESERT, FIELD, BEACH, HILL, FOREST, URBAN
}

/**
 * A usable item that can be applied to a monster to produce a variety of result
 */
abstract class Item implements Purchasable {
    /**
     * Signals that aan item has been applied to a monster but produce no result
     */
    static final class NoEffectException extends IllegalStateException {
        public NoEffectException(String desc) {
            super(desc);
        }
    }

    /**
     * The name of the item
     */
    private final @NotNull String name;

    /**
     * Create a new Item with a name
     *
     * @param name The name of the item
     */
    public Item(@NotNull String name) {
        this.name = name;
    }

    /**
     * Apply this item to a monster
     *
     * @param mon The target monster
     * @throws NoEffectException if the effect gave no changes
     */
    public abstract void applyTo(@NotNull Monster mon) throws NoEffectException;

    @Override
    public abstract int buyPrice();

    @Override
    public abstract int sellPrice();

    /**
     * Access the name of this item
     *
     * @return The name of this item
     */
    public @NotNull String getName() {
        return name;
    }
}

/**
 * An entity that can fight in a battle with a variety of stats
 */
abstract class Monster implements Purchasable {
    /**
     * The name of the monster
     */
    private @NotNull String name;
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
    public Monster(@NotNull String name, int baseHp, int level) {
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

    public void levelUp() {
        level += 1;
        this.currentHp = maxHp();
    }

    public void healSelf(int amount) {
        this.currentHp = Math.min(currentHp + amount, maxHp());
    }

    public void takeDamage(int amount) {
        this.currentHp = Math.max(currentHp - amount, 0);
    }

    public int maxHp() {
        return (int) (this.baseHp * multiplier());
    }

    public int damage(Environment env) {
        var envMultiplier = env == idealEnvironment() ? 1.5 : 1;
        var res = (double) baseDamage() * multiplier() * envMultiplier;
        return (int) res;
    }

    public boolean isFainted() {
        return currentHp <= 0;
    }

    public int buyPrice() {
        var basePrice = (baseHp + baseDamage() + healRate() + speed()) * multiplier();
        return (int) basePrice;
    }

    public int sellPrice() {
        return buyPrice() / 2;
    }

    public int getLevel() {
        return level;
    }

    public @NotNull String getName() {
        return name;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public void setBaseHp(int baseHp) {
        this.baseHp = baseHp;
    }

    public boolean equals(Monster mon) {
        return this.maxHp() == mon.maxHp()
            && this.baseDamage() == mon.baseDamage()
            && this.speed() == mon.speed()
            && this.getLevel() == mon.getLevel()
            && this.healRate() == mon.healRate()
            && this.getName().equals(mon.getName());
    }
}

/**
 * AN entity with a party of monsters
 */
final class Trainer {
    /**
     * Signals that add is called when the party reach its maximum size
     */
    static final class PartyFullException extends IllegalStateException {
        public PartyFullException(String desc) {
            super(desc);
        }
    }

    /**
     * The name of the trainer
     */
    private @NotNull String name;

    /**
     * The party of monsters
     */
    private final @Nullable Monster[] party = new Monster[4];

    /**
     * Create a new instance of Trainer
     *
     * @param name Name of the trainer
     */
    public Trainer(@NotNull String name) {
        this.name = name;
    }

    /**
     * Add a new monster into a party if possible
     *
     * @param mon The monster added
     * @throws PartyFullException Monsters cannot be added if party has already reached 4 monsters
     */
    public void add(Monster mon) throws PartyFullException {
        for (var i = 0; i < 4; i++) {
            if (party[i] == null) {
                party[i] = mon;
                return;
            }
        }
        throw new PartyFullException("Cannot add more than 4 monster");
    }

    /**
     * Switch the ordering of two monster
     *
     * @param from One of the index of the monster being swapped
     * @param to   The other index
     * @throws IndexOutOfBoundsException If either the index is out of bound
     */
    public void switchMonster(int from, int to) throws IndexOutOfBoundsException {
        var temp = party[from];
        party[from] = party[to];
        party[to] = temp;
    }

    /**
     * Get the party as it is with all the nulls
     *
     * @return All nullable monster in the party
     */
    public List<@Nullable Monster> getParty() {
        return Arrays.asList(party);
    }


    /**
     * Get the party removing all nulls
     *
     * @return All not null monster in the party
     */
    public List<@NotNull Monster> getFlattenParty() {
        return Arrays.stream(party)
            .flatMap(m -> Optional.ofNullable(m).stream())
            .toList();
    }

    /**
     * Get the name of the trainer
     *
     * @return The name
     */
    public @NotNull String getName() {
        return name;
    }

    /**
     * Chnage the name of the trainer
     *
     * @param name The new name
     */
    public void setName(@NotNull String name) {
        this.name = name;
    }
}

/**
 * A collection for item used by trainer
 */
final class Inventory {
    /**
     * Signals that the caller intended to use an item that does not exist in the inventory
     */
    static final class ItemNotExistException extends IndexOutOfBoundsException {
        public ItemNotExistException() {
            super();
        }
    }

    /**
     * The item collection
     */
    private final ArrayList<@NotNull Item> items = new ArrayList<>();

    /**
     * Adding a new item to the inventory
     * @param item The newly added item
     */
    public void add(Item item) {
        if (item == null) return;
        items.add(item);
    }

    /**
     * Use a specific item in the inventory
     * @param i The index to reference the object
     * @param mon The monster to apply the item to
     * @throws ItemNotExistException If the index reference a non-existing item
     * @throws Item.NoEffectException If the item produce no effect
     */
    public void use(int i, Monster mon) throws ItemNotExistException, Item.NoEffectException {
        if (i < 0 || i >= items.size()) throw new ItemNotExistException();
        items.get(i).applyTo(mon);
    }

    /**
     * Get all the item in the inventory
     * @return The items as an arraylist
     */
    public ArrayList<@NotNull Item> getItems() {
        return items;
    }
}

/**
 * A shop where you can buy or sell purchasable
 */
final class Shop {
    /**
     * Signals that the desired purchasable is not available in the Shop at this current state
     */
    static final class NotInStockException extends IndexOutOfBoundsException {
        public NotInStockException() {
            super();
        }
    }

    /**
     * Signals that the buyer does not have sufficient fund to make the purchase
     */
    static final class InsufficientFundsException extends IndexOutOfBoundsException {
        public InsufficientFundsException() {
            super();
        }
    }

    /** The item stock in a form of counters */
    private final Map<@NotNull String, Integer> itemStock = new HashMap<>();
    /** The monsters available to purchase */
    private final ArrayList<@NotNull Monster> monsterStock = new ArrayList<>();
    /** The trainer party */
    private final @NotNull Trainer trainer;
    /** The trainer inventory */
    private final @NotNull Inventory inventory;
    /** The game manager itself */
    private final @NotNull GameManager manager;

    /**
     * Create a new shop with the manager
     * @param manager The game manager to also get trainer and inventory
     */
    public Shop(@NotNull GameManager manager) {
        this.manager = manager;
        this.trainer = manager.trainer;
        this.inventory = manager.inventory;
    }

    /**
     * Buy a purchasable from the shop
     * @param purchasable The purchasable trying to be bought
     * @throws NotInStockException If the item is not available
     * @throws InsufficientFundsException If the buyer does not have enough gold
     */
    public void buyPurchasable(Purchasable purchasable) throws NotInStockException, InsufficientFundsException {
        if (manager.getGold() < purchasable.buyPrice())
            throw new InsufficientFundsException();

        if (purchasable instanceof Item item) {
            var stock = itemStock.getOrDefault(item.getName(), 0);

            if (stock <= 0)
                throw new NotInStockException();

            itemStock.put(item.getName(), stock - 1);
            inventory.add(item);
        } else if (purchasable instanceof Monster monster) {
            var index = IntStream.range(0, monsterStock.size())
                .filter(i -> monsterStock.get(i).equals(monster))
                .findFirst();

            if (index.isEmpty())
                throw new NotInStockException();

            monsterStock.remove(index.getAsInt());
            trainer.add(monster);
        } else {
            throw new NotInStockException();
        }

        manager.setGold(manager.getGold() - purchasable.buyPrice());
    }

    /**
     * Sell a purchasable and get gold
     * @param purchasable The item to be sold
     */
    public void sellPurchasable(Purchasable purchasable) {
        // TODO: Remove from party / inventory
        manager.setGold(manager.getGold() + purchasable.sellPrice());
    }
}

final class GameManager {
    private int gold = 0;
    public final @NotNull Trainer trainer = new Trainer("");
    public final @NotNull Inventory inventory = new Inventory();
    public final @NotNull Shop shop = new Shop(this);

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}