//
//  Shop.java
//  seng-practice
//
//  Created by d-exclaimation on 5:11 PM.
//  Copyright © 2022 d-exclaimation. All rights reserved.
//
package seng.monsters.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * A shop where you can buy or sell purchasable
 */
final class Shop {
    /**
     * Signals that the desired purchasable is not available in the Shop at this current state
     */
    static final class NotInStockException extends IndexOutOfBoundsException {
    }

    /**
     * Signals that the buyer does not have sufficient fund to make the purchase
     */
    static final class InsufficientFundsException extends IndexOutOfBoundsException {
    }

    /**
     * The item stock in a form of counters
     */
    private final Map<Item, Integer> itemStock = new HashMap<>();
    /**
     * The monsters available to purchase
     */
    private final Map<UUID, Monster> monsterStock = new HashMap<>();

    /** The game managger */
    private final GameManager manager;

    public Shop(GameManager manager) {
        this.manager = manager;
    }


    /**
     * Buy a purchasable from the shop
     *
     * @param purchasable The purchasable trying to be bought
     * @throws NotInStockException        If the item is not available
     * @throws InsufficientFundsException If the buyer does not have enough gold
     */
    public void buyPurchasable(Purchasable purchasable) throws NotInStockException, InsufficientFundsException {
        final var funds = manager.getGold();
        if (funds < purchasable.buyPrice())
            throw new InsufficientFundsException();

        if (purchasable instanceof Item item) {
            final var stock = itemStock
                .getOrDefault(item, 0);

            if (stock <= 0)
                throw new NotInStockException();

            itemStock.put(item, stock - 1);
        } else if (purchasable instanceof Monster monster) {
            if (!monsterStock.containsKey(monster.getId()))
                throw new NotInStockException();

            monsterStock.remove(monster.getId());
        } else {
            throw new NotInStockException();
        }

        manager.setGold(funds - purchasable.buyPrice());
    }

    /**
     * Sell a purchasable and get gold
     *
     * @param purchasable The item to be sold
     */
    public void sellPurchasable(Purchasable purchasable) {
        manager.setGold(manager.getGold() + purchasable.sellPrice());
    }

    /**
     * Create a random monster given the context
     *
     * @return A monster
     */
    public Monster randomMonster() {
        final var seed = (int) Math.floor(Math.random() * 3) % 3;
        final var range = (int) Math.round(Math.random() * 6 * manager.getDifficulty()) - 3 * manager.getDifficulty();
        final var level = Math.max(1, manager.getCurrentDay() + range);
        final List<Monster> allMonsters = List.of(
            new Monster.Quacker(level),
            new Monster.Raver(level),
            new Monster.Tree(level)
        );
        return allMonsters.get(seed);
    }

    /**
     * Create a new stock of the items
     *
     * @return A map mapping all the item to a count
     */
    public Map<Item, Integer> randomItemStock() {
        final List<Item> allItems = List.of(new Item.Potion(), new Item.Revive(), new Item.RareCandy());
        final Map<Item, Integer> map = new HashMap<>(allItems.size());
        allItems
            .forEach(item -> {
                final var count = (int) Math.round(Math.random() * manager.getCurrentDay() * manager.getDifficulty()) + 1;
                map.put(item, count);
            });
        return map;
    }

    /**
     * Refresh all item and monster sold
     */
    public void restock() {
        monsterStock.clear();
        IntStream.range(0, 3 * manager.getDifficulty())
            .mapToObj(_i -> randomMonster())
            .forEachOrdered(mon -> monsterStock.put(mon.getId(), mon));

        itemStock.clear();
        itemStock.putAll(randomItemStock());
    }

    /**
     * Get the monster in stock
     * @return The list all monster in stock
     */
    public List<Monster> getMonsterStock() {
        return monsterStock.values().stream().toList();
    }

    /**
     * Get the items and its count
     * @return The list all item in stock
     */
    public List<Map.Entry<Item, Integer>> getItemStock() {
        return itemStock.entrySet().stream().filter(entry -> entry.getValue() > 0).toList();
    }
}