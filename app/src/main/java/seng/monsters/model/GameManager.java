//
//  GameManager.java
//  seng-practice
//
//  Created by d-exclaimation on 9:20 PM.
//  Copyright © 2022 d-exclaimation. All rights reserved.
//
package seng.monsters.model;

import java.util.ArrayList;
import java.util.Random;

public class GameManager {
    private int gold = 0;
    private int score = 0;
    private int currentDay = 1;
    private int maxDays = 5;
    private int difficulty = 1;
    private Environment environment = Environment.FIELD;

    private final Trainer trainer;
    private final Inventory inventory;
    private final Shop shop;
    private final ArrayList<Trainer> availableBattles = new ArrayList<>();

    public GameManager() {
        trainer = new Trainer("");
        inventory = new Inventory();
        shop = new Shop(this);
    }

    public GameManager(int gold, int currentDay, int maxDays, int difficulty) {
        this.gold = gold;
        this.currentDay = currentDay % maxDays;
        this.maxDays = maxDays;
        this.difficulty = difficulty;
        this.trainer = new Trainer("");
        this.inventory = new Inventory();
        this.shop = new Shop(this);

        // TODO: -- Available battles, shop should be restocked after initial setup
    }

    /**
     * Get the player trainer
     *
     * @return The trainer for the player
     */
    public Trainer getTrainer() {
        return trainer;
    }

    /**
     * Get the gold reserve owned by the player
     *
     * @return The count of gold the player owned
     */
    public int getGold() {
        return gold;
    }

    /**
     * Get the current day count
     *
     * @return How many days has passed
     */
    public int getCurrentDay() {
        return currentDay;
    }

    /**
     * Get the maximum length of the game in days
     *
     * @return The limit
     */
    public int getMaxDays() {
        return maxDays;
    }

    /**
     * Get the current environment of the day
     *
     * @return The environment of the day
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * Get the difficulty
     *
     * @return The difficulty multiplier
     */
    public int getDifficulty() {
        return difficulty;
    }


    // TODO: --- Required controller methods to use to play the game (pulled from the flow of the game) ---

    /**
     * Proceed to the following day and update all state accordingly
     *
     * @return A boolean signalling if the game is finished
     */
    public boolean nextDay() {
        setCurrentDay(getCurrentDay() + 1);
        final var hasEnded = getCurrentDay() >= getMaxDays();
        if (!hasEnded)
            triggerNightEvents();
        return hasEnded;
    }

    /**
     * Triggers night events if game does not end after calling nextDay
     */
    protected void triggerNightEvents() {
        changeEnvironment();
        shop.restock();
        partyMonstersLeave();
        partyMonstersHeal();
        partyMonstersLevelUp();
        monsterJoinsParty();

        // TODO: -- Missing available battles --
    }

    /**
     * Changes the environment to a randomly selected environment
     */
    protected void changeEnvironment() {
        final var newEnvironment = Environment.generateRandomEnvironment();
        setEnvironment(newEnvironment);
    }

    /**
     * Removes a monster from the party if their leave prerequisites are met.
     * Only remove one monster at once to prevent indexing errors.
     */
    protected void partyMonstersLeave() {
        // TODO:
        //  - Decide if only at most 1 monster leave per night or remove the limit
        //  - If there is no limit, use the remove(Monster mon) (which takes O(n^2) where n <= 4)
        for (int i = 0; i < trainer.getParty().size(); i++) {
            if (trainer.getParty().get(i).shouldLeave()) {
                trainer.remove(i);
                return;
            }
        }
    }

    /**
     * Heals each party monster by their heal rate if they are not fainted.
     */
    protected void partyMonstersHeal() {
        for (final var mon : trainer.getParty()) {
            if (!mon.isFainted()) {
                final var monsterHealRate = mon.healRate();
                mon.healSelf(monsterHealRate);
            }
        }
    }

    /**
     * Levels up each monster in the party that meets level up prerequisites
     */
    protected void partyMonstersLevelUp() {
        for (final var mon : trainer.getParty()) {
            if (mon.shouldLeave()) {
                mon.levelUp();
            }
        }
    }

    /**
     * A chance that a random monsters join the party if there is a slot
     *
     * Joining chance:
     * <code> base = 0.01 </code> <p>
     * <code> f(d, c, m) = base x d x c / m</code>
     */
    public void monsterJoinsParty() {
        final var chance = 0.01 * getDifficulty() * (getCurrentDay() / getMaxDays());
        final var isLucky = Math.random() <= chance;
        if (trainer.getParty().size() >= 4 || !isLucky)
            return;

        final var joining = shop.randomMonster();
        trainer.add(joining);
    }

    /**
     * Use a certain item in the inventory to a monster in the party
     *
     * @param item         The item being used
     * @param monsterIndex The monster by their index
     * @throws Item.NoEffectException               If the item produces no changes
     * @throws Inventory.ItemNotExistException      If the item does not exist
     * @throws Trainer.MonsterDoesNotExistException if the monster is null or doesn't exist
     */
    public void useItemFromInventory(
        Item item,
        int monsterIndex
    ) throws Item.NoEffectException, Inventory.ItemNotExistException, Trainer.MonsterDoesNotExistException {
        if (item == null)
            throw new Inventory.ItemNotExistException();

        if (monsterIndex < 0 || monsterIndex >= trainer.getParty().size())
            throw new Trainer.MonsterDoesNotExistException();

        final var monster = trainer.getParty().get(monsterIndex);
        if (monster == null)
            throw new Trainer.MonsterDoesNotExistException();

        inventory.use(item, monster);
    }

    /**
     * Use a certain item in the inventory to a monster in the party
     *
     * @param item    The item being used
     * @param monster The monster itself
     * @throws Item.NoEffectException               If the item produces no changes
     * @throws Inventory.ItemNotExistException      If the item does not exist
     * @throws Trainer.MonsterDoesNotExistException if the monster is null or doesn't exist
     */
    public void useItemFromInventory(
        Item item,
        Monster monster
    ) throws Item.NoEffectException, Inventory.ItemNotExistException, Trainer.MonsterDoesNotExistException {
        if (item == null)
            throw new Inventory.ItemNotExistException();

        if (monster == null)
            throw new Trainer.MonsterDoesNotExistException();

        inventory.use(item, monster);
    }

    /**
     * Switch the monster by their index
     *
     * @param from Starting index
     * @param to   Destination index
     * @throws IndexOutOfBoundsException If either the indices are not valid indices for the party
     */
    public void switchMonsterOnParty(int from, int to) throws IndexOutOfBoundsException {
        trainer.switchMonster(from, to);
    }

    /**
     * Switch the monster
     *
     * @param monster Monster to be moved
     * @param to      Destination index
     * @throws IndexOutOfBoundsException If either the indices are not valid indices for the party
     */
    public void switchMonsterOnParty(Monster monster, int to) throws IndexOutOfBoundsException {
        trainer.moveMonster(monster, to);
    }

    /**
     * Buy a purchasable from the shop
     *
     * @param purchasable The purchase being made
     * @throws Shop.NotInStockException        If the item does not exist
     * @throws Shop.InsufficientFundsException If the current gold is not enough to make the purchase
     */
    public void buy(
        Purchasable purchasable
    ) throws Shop.NotInStockException, Shop.InsufficientFundsException, Trainer.PartyFullException {
        shop.buyPurchasable(purchasable);
        if (purchasable instanceof Item item) {
            inventory.add(item);
        } else if (purchasable instanceof Monster monster) {
            trainer.add(monster);
        }
    }

    /**
     * Sell a purchasable from the shop
     *
     * @param purchasable The purchase being made
     * @throws Inventory.ItemNotExistException      If the item does not exist
     * @throws Trainer.MonsterDoesNotExistException If the monster does not exist
     */
    public void sell(Purchasable purchasable) throws Inventory.ItemNotExistException, Trainer.MonsterDoesNotExistException {
        if (purchasable instanceof Item item) {
            inventory.remove(item);
        } else if (purchasable instanceof Monster monster) {
            var i = trainer.getParty().indexOf(monster);
            if (i == -1)
                throw new Trainer.MonsterDoesNotExistException();
            trainer.remove(i);
        }
        shop.sellPurchasable(purchasable);
    }

    /**
     * Change the player name to a new one
     *
     * @param name The new name the player choose
     */
    public void setTrainerName(String name) {
        trainer.setName(name);
    }

    /**
     * Change the current holding of golds
     *
     * @param gold The new amount of gold
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * Extend or shrink the maximum total game days
     *
     * @param maxDays The new total days
     */
    public void setMaxDays(int maxDays) {
        this.maxDays = maxDays;
    }

    /**
     * Change the environment of the current day
     * <b>(Internal code, for testing purposes)</b>
     *
     * @param environment The new environment
     */
    protected void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * Jump to a day
     * <b>(Internal code, for testing purposes)</b>
     *
     * @param currentDay The day to jump into
     */
    protected void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }

    /**
     * Change the difficulty of the game
     * <b>(Internal code, for testing purposes)</b>
     *
     * @param difficulty The new difficulty multiplier
     */
    protected void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}


