//
//  GameManager.java
//  seng-practice
//
//  Created by d-exclaimation on 9:20 PM.

//
package seng.monsters.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Game Manager that the model of the game
 */
public class GameManager {
    /**
     * The gold owned by the player
     */
    private int gold = 0;

    /**
     * The score accumulated through the game
     */
    private int score = 0;

    /**
     * The current day count
     */
    private int currentDay = 1;

    /**
     * The maximum day limit
     */
    private int maxDays = 5;

    /**
     * The difficulty scale
     */
    private int difficulty = 1;

    /**
     * The current environment of the day
     */
    private Environment environment = Environment.FIELD;

    /**
     * The player trainer
     */
    private final Trainer trainer;

    /**
     * The player's inventory
     */
    private final Inventory inventory;

    /**
     * The current shop for the day
     */
    private final Shop shop;

    /**
     * The available battles for the day
     */
    private final ArrayList<Trainer> availableBattles = new ArrayList<>();

    public GameManager() {
        trainer = new Trainer("Anonymous");
        inventory = new Inventory();
        shop = new Shop(this);
    }

    public GameManager(int gold, int currentDay, int maxDays, int difficulty, String name) {
        this.gold = gold;
        this.currentDay = currentDay % maxDays;
        this.maxDays = maxDays;
        this.difficulty = difficulty;
        this.trainer = new Trainer(name);
        this.inventory = new Inventory();
        this.shop = new Shop(this);

        refreshCurrentDay();
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
     * Get the player's inventory
     *
     * @return The trainer for the player
     */
    public Inventory getInventory() {
        return inventory;
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

    /**
     * Get the available battles for this current day
     *
     * @return The list of all trainer available to fight
     */
    public List<Trainer> getAvailableBattles() {
        return availableBattles
            .stream()
            .filter(trainer -> !trainer.isWhitedOut())
            .toList();
    }

    /**
     * Get the score
     *
     * @return The score points
     */
    public int getScore() {
        return score;
    }

    /**
     * Get the current shop
     * @return The shop
     */
    public Shop getShop() {
        return shop;
    }

    // MARK: -- Rule checking methods --


    /**
     * Check if the player has no monster and not enough funds to purchase a monster
     *
     * @return true if the player has no monster and gold is less than any monster price in the shop
     */
    public boolean hasNotEnoughMoneyForMonster() {
        return trainer.getParty().isEmpty() &&
            getGold() < shop.getMonsterStock().stream().mapToInt(Monster::buyPrice).min().orElse(0);
    }

    /**
     * Check if the player has not active monster and not enough funds to purchase revive
     *
     * @return true if the player's party is all fainted and gold is less than the price of revive
     */
    public boolean hasNoPossibilityForRevive() {
        return trainer.isWhitedOut() && getGold() < new Item.Revive().buyPrice();
    }

    /**
     * Check if the player has done a battle once
     *
     * @return true if any of enemies has fainted monster
     */
    public boolean hasNotBattleOnce() {
        return availableBattles.stream()
            .map(Trainer::getParty)
            .noneMatch(party -> party.stream().anyMatch(mon -> mon.getCurrentHp() < mon.maxHp()));
    }

    // MARK: -- Next day methods --

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

        return hasEnded
            || hasNotEnoughMoneyForMonster()
            || hasNoPossibilityForRevive();
    }


    /**
     * Trigger refreshes for any value that can be reloaded without changing the day
     */
    public void refreshCurrentDay() {
        shop.restock();
        updateAvailableBattles();
    }

    /**
     * Triggers night events if game does not end after calling nextDay
     */
    protected void triggerNightEvents() {
        changeEnvironment();
        partyMonstersLeave();
        partyMonstersHeal();
        partyMonstersLevelUp();
        monsterJoinsParty();
        shop.restock();
        updateAvailableBattles();
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
            if (mon.shouldLevelUp()) {
                mon.levelUp();
            }
        }
    }

    /**
     * A chance that a random monsters join the party if there is a slot
     * <p>
     * Joining chance:
     * <code> base = 0.01 </code> <p>
     * <code> f(d, c, m) = base x d x c / m</code>
     */
    protected void monsterJoinsParty() {
        final var chance = 0.01 * getDifficulty() * (getCurrentDay() / getMaxDays());
        final var isLucky = Math.random() <= chance;
        if (trainer.getParty().size() >= 4 || !isLucky)
            return;

        final var joining = shop.randomMonster();
        trainer.add(joining);
    }

    /**
     * Update the available battles for the day
     */
    protected void updateAvailableBattles() {
        final var amountEnemies = Math.max(1, Math.min(5, 5 * getDifficulty() * getCurrentDay() / getMaxDays()));
        final var amountMonster = Math.max(1, Math.min(4, 4 * getDifficulty() * getCurrentDay() / getMaxDays()));

        availableBattles.clear();

        for (var i = 0; i < amountEnemies; i++) {
            final var enemy = new Trainer(getEnvironment().toString() + " enemy " + i);
            for (var j = 0; j < amountMonster; j++) {
                enemy.add(shop.randomMonster());
            }
            availableBattles.add(enemy);
        }
    }

    // MARK: -- Actions --

    /**
     * Prepare battle manager for a battle against selected enemy
     *
     * @param ui    The UI used to the battle manager
     * @param index The index of the available battle
     * @return The battle manager that had been prepared
     * @throws IndexOutOfBoundsException If the index given does not point to a valid enemy
     */
    public BattleManager prepareBattle(BattleManager.UI ui, int index) throws IndexOutOfBoundsException {
        final var enemy = getAvailableBattles().get(index);
        return new BattleManager(ui, getTrainer(), enemy, getEnvironment());
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
     * @throws Trainer.PartyFullException      if the current party if full
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
     * Set the new score
     *
     * @param score The new total score
     */
    public void setScore(int score) {
        this.score = score;
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


