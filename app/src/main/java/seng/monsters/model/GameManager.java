//
//  GameManager.java
//  seng-practice
//
//  Created by d-exclaimation on 9:20 PM.
//  Copyright © 2022 d-exclaimation. All rights reserved.
//
package seng.monsters.model;

import java.util.ArrayList;

// TODO: Clean the GameManager
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
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public int getGold() {
        return gold;
    }

    public int getCurrentDay() {
        return currentDay;
    }


    public int getMaxDays() {
        return maxDays;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public int getDifficulty() {
        return difficulty;
    }


    // TODO: --- Required controller methods to use to play the game (pulled from the flow of the game) ---

    /**
     * Proceed to the following day and update all state accordingly
     * @return A boolean signalling if the game is finished
     */
    public boolean nextDay() {
        // TODO: --- Missing implementations ---
        return true;
    }

    /**
     * Use a certain item in the inventory to a monster in the party
     * @param item The item being used
     * @param monsterIndex The monster by their index
     * @throws Item.NoEffectException If the item produces no changes
     * @throws Inventory.ItemNotExistException If the item does not exist
     */
    public void useItemFromInventory(Item item, int monsterIndex) throws Item.NoEffectException, Inventory.ItemNotExistException {
        // TODO: --- Missing checks ---
        final var monster = this.trainer.getParty().get(monsterIndex);
        if (monster == null)
            throw new Item.NoEffectException("There is no monster to apply an effect");

        this.inventory.use(item, monster);
    }

    /**
     * Use a certain item in the inventory to a monster in the party
     * @param item The item being used
     * @param monster The monster itself
     * @throws Item.NoEffectException If the item produces no changes
     * @throws Inventory.ItemNotExistException If the item does not exist
     */
    public void useItemFromInventory(Item item, Monster monster) throws Item.NoEffectException, Inventory.ItemNotExistException {
        // TODO: --- Missing checks ---
        if (monster == null)
            throw new Item.NoEffectException("There is no monster to apply an effect");
        this.inventory.use(item, monster);
    }

    /**
     * Switch the monster by their index
     * @param from Starting index
     * @param to Destination index
     * @throws IndexOutOfBoundsException If either the indices are not valid indices for the party
     */
    public void switchMonsterOnParty(int from, int to) throws IndexOutOfBoundsException {
        this.trainer.switchMonster(from, to);
    }

    /**
     * Switch the monster
     * @param monster Monster to be moved
     * @param to Destination index
     * @throws IndexOutOfBoundsException If either the indices are not valid indices for the party
     */
    public void switchMonsterOnParty(Monster monster, int to) throws IndexOutOfBoundsException {
        // TODO: --- Required controller methods to use to play the game (pulled from the flow of the game) ---
    }

    /**
     * Buy a purchasable from the shop
     * @param purchasable The purchase being made
     * @throws Shop.NotInStockException If the item does not exist
     * @throws Shop.InsufficientFundsException If the current gold is not enough to make the purchase
     */
    public void buy(Purchasable purchasable) throws Shop.NotInStockException, Shop.InsufficientFundsException {
        this.shop.buyPurchasable(purchasable);
        if (purchasable instanceof Item item) {
            this.inventory.add(item);
        } else if (purchasable instanceof Monster monster) {
            this.trainer.add(monster);
        }
    }

    /**
     * Sell a purchasable from the shop
     * @param purchasable The purchase being made
     * @throws Shop.NotInStockException If the item does not exist
     */
    public void sell(Purchasable purchasable) throws Inventory.ItemNotExistException {
        if (purchasable instanceof Item item) {
            this.inventory.remove(item);
        } else if (purchasable instanceof Monster monster) {
            var i = this.trainer.getParty().indexOf(monster);
            // TODO: --- Custom error ---
            if (i == -1)
                throw new Inventory.ItemNotExistException();
            this.trainer.remove(i);
        }
        this.shop.sellPurchasable(purchasable);
    }

    /**
     * Random night event
     */
    public void randomNightEvent() {
        // TODO: --- Missing implementations ---
    }

    /**
     * Change the player name to a new onw
     * @param name The new name the player choose
     */
    public void setTrainerName(String name) {
        this.trainer.setName(name);
    }

    /**
     * Change the current holding of golds
     * @param gold The new amount of gold
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * Extend or shrink the maximum total game days
     * @param maxDays The new total days
     */
    public void setMaxDays(int maxDays) {
        this.maxDays = maxDays;
    }

    /**
     * Change the environment of the current day
     * <b>(Internal code, for testing purposes)</b>
     * @param environment The new environment
     */
    protected void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * Jump to a day
     * <b>(Internal code, for testing purposes)</b>
     * @param currentDay The day to jump into
     */
    protected void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }

    /**
     * Change the difficulty of the game
     * <b>(Internal code, for testing purposes)</b>
     * @param difficulty The new difficulty multiplier
     */
    protected void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}


