//
//  GameManager.java
//  seng-practice
//
//  Created by Vincent on 9:20 PM.

//
package seng.monsters.model;

import java.io.InputStream;
import java.util.*;

/**
 * The Game Manager that contains and manages the majority of the game logic.
 */
public class GameManager {
    /**
     * The gold owned by the player.
     */
    private int gold = 0;

    /**
     * The score accumulated through the game.
     */
    private int score = 0;

    /**
     * The current day count.
     */
    private int currentDay = 1;

    /**
     * The maximum day limit.
     */
    private int maxDays = 5;

    /**
     * The difficulty scale.
     */
    private int difficulty = 1;

    /**
     * The current environment of the day.
     */
    private Environment environment = Environment.FIELD;

    /**
     * The player trainer.
     */
    private final Trainer player;

    /**
     * The player's inventory.
     */
    private final Inventory inventory;

    /**
     * The game's shop.
     */
    private final Shop shop;

    /**
     * The available battles for the day.
     */
    private final ArrayList<Trainer> availableBattles = new ArrayList<>();


    /**
     * Creates a bare-bone GameManager that still requires additional setup.
     */
    public GameManager() {
        player = new Trainer("Anonymous");
        inventory = new Inventory();
        shop = new Shop(this);
    }

    /**
     * Creates a fully ready and functional GameManager that requires no additional setup.
     * @param gold The starting amount of gold.
     * @param currentDay The current day.
     * @param maxDays The maximum amount of days.
     * @param difficulty The difficulty scale.
     * @param name The trainer name.
     */
    public GameManager(int gold, int currentDay, int maxDays, int difficulty, String name) {
        this.gold = gold;
        this.currentDay = currentDay % maxDays;
        this.maxDays = maxDays;
        this.difficulty = difficulty;
        this.player = new Trainer(name);
        this.inventory = new Inventory();
        this.shop = new Shop(this);

        // Stocks the shop and generates battles.
        refreshCurrentDay();
    }

    // MARK: -- Getters --

    /**
     * Get the player trainer.
     *
     * @return The Trainer for the player.
     */
    public Trainer getPlayer() {
        return player;
    }

    /**
     * Get the player's inventory.
     *
     * @return The player's Inventory.
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Get the gold reserve owned by the player.
     *
     * @return The count of gold the player owned.
     */
    public int getGold() {
        return gold;
    }

    /**
     * Get the current day count.
     *
     * @return How many days has passed.
     */
    public int getCurrentDay() {
        return currentDay;
    }

    /**
     * Get the maximum length of the game in days.
     *
     * @return The maximum day counter.
     */
    public int getMaxDays() {
        return maxDays;
    }

    /**
     * Get the current environment of the day.
     *
     * @return The Environment of the day.
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * Get the difficulty multiplier.
     *
     * @return The difficulty multiplier.
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Get the available battles for this current day.
     *
     * @return The List of all enemy Trainers available to fight.
     */
    public List<Trainer> getAvailableBattles() {
        // Does not include trainers that have been defeated to the list.
        return availableBattles
            .stream()
            .filter(trainer -> !trainer.isWhitedOut())
            .toList();
    }

    /**
     * Get the score.
     *
     * @return The score points.
     */
    public int getScore() {
        return score;
    }

    /**
     * Get the current shop.
     *
     * @return The game's Shop.
     */
    public Shop getShop() {
        return shop;
    }

    // MARK: -- Rule checking methods --

    /**
     * <p>
     * Check if the player has no monster and not enough funds to purchase a monster.
     * One of the game fail conditions.
     * </p>
     *
     * @return true if the player has no monster and gold is less than any monster price in the shop.
     */
    public boolean hasNotEnoughMoneyForMonster() {
        return player.getParty().isEmpty() &&
            getGold() < shop.getMonsterStock().stream().mapToInt(Monster::buyPrice).min().orElse(0);
    }

    /**
     * <p>
     * Check if the player has not active monster and not enough funds to purchase revive.
     * One of the game fail conditions.
     * </p>
     *
     * @return true if the player's party is all fainted and gold is less than the price of revive.
     */
    public boolean hasNoPossibilityForRevive() {
        final boolean hasNoRevive = inventory.getItemNumber(new Item.Revive()) <= 0 &&
            inventory.getItemNumber(new Item.FullRestore()) <= 0;
        return player.isWhitedOut() && getGold() < new Item.Revive().buyPrice() && hasNoRevive;
    }

    /**
     * Check if the player has done a battle once.
     * Condition to be able to sleep.
     *
     * @return true if any of enemies has fainted monster.
     */
    public boolean hasNotBattleOnce() {
        // Checking if any enemy monsters have been damaged.
        return availableBattles.stream()
            .map(Trainer::getParty)
            .noneMatch(party -> party.stream().anyMatch(mon -> mon.getCurrentHp() < mon.maxHp()));
    }

    // MARK: -- Next day methods --

    /**
     * Proceed to the following day and update all state accordingly.
     *
     * @return A boolean signalling if the game is finished.
     */
    public boolean nextDay() {
        setCurrentDay(getCurrentDay() + 1);
        final boolean hasEnded = getCurrentDay() > getMaxDays();

        // Triggers night events if game is not completed.
        if (!hasEnded)
            triggerNightEvents();

        // Check if game has finished, either by completing the game or fulfilling lose condition(s).
        return hasEnded
            || hasNotEnoughMoneyForMonster()
            || hasNoPossibilityForRevive();
    }


    /**
     * Trigger refreshes for any value that can be reloaded without changing the day.
     */
    public void refreshCurrentDay() {
        shop.restock();
        updateAvailableBattles();
    }

    /**
     * Triggers night events if game does not end after calling nextDay.
     */
    protected void triggerNightEvents() {
        // Changes the environment, heals monsters, restock shop, refreshes battles.
        changeEnvironment();
        partyMonstersHeal();
        shop.restock();
        updateAvailableBattles();
    }

    /**
     * Changes the environment to a randomly selected environment.
     */
    protected void changeEnvironment() {
        final Environment newEnvironment = Environment.generateRandomEnvironment();
        setEnvironment(newEnvironment);
    }

    /**
     * <p>
     * Removes a monster from the party if their leave prerequisites are met.
     * Only remove one monster at once to prevent indexing errors.
     * </p>
     *
     * @return the monster leaving the party for displaying to output, or null otherwise.
     */
    public Optional<Monster> partyMonstersLeave() {
        // Checking each monster in the party if they meet leave conditions.
        for (int i = 0; i < player.getParty().size(); i++) {
            final Monster mon = player.getParty().get(i);
            if (mon.shouldLeave()) {
                player.remove(i);
                return Optional.of(mon);
            }
        }
        return Optional.empty();
    }

    /**
     * Heals each party monster by their heal rate if they are not fainted.
     */
    protected void partyMonstersHeal() {
        for (final Monster mon : player.getParty()) {
            if (!mon.isFainted()) {
                final int monsterHealRate = mon.healRate();
                mon.healSelf(monsterHealRate);
            }
        }
    }

    /**
     * Levels up each monster in the party that meets level up prerequisites.
     *
     * @return a list of any monsters that levelled up for displaying to output.
     */
    public List<Monster> partyMonstersLevelUp() {
        // Creates a list of monsters that meet level up conditions and increments their level by 1.
        final List<Monster> lastLevelledUp = player
            .getParty()
            .stream()
            .filter(mon -> !mon.isFainted())
            .filter(Monster::shouldLevelUp)
            .toList();
        lastLevelledUp.forEach(Monster::levelUp);
        return lastLevelledUp;
    }

    /**
     * A chance that a random monsters join the party if there is a slot
     * <p>
     * Joining chance:
     * <code> base = 0.05 </code> <p>
     * <code> f(d, p) = base * (d + 1)(0.5) * (4 - p)</code>
     *
     * @return the monster that is joining the party for displaying to output, or null otherwise.
     */
    public Optional<Monster> monsterJoinsParty() {
        final double chance = 0.05 * (0.5 * getDifficulty() + 0.5) * (4 - getPlayer().getParty().size());
        final boolean isLucky = Math.random() <= chance;
        // Returns nothing if the check is failed
        if (!isLucky)
            return Optional.empty();
        // Attempts to add and return the joining monster, else returns nothing if party is full.
        try {
            final Monster joining = shop.randomMonster();
            player.add(joining);
            return Optional.of(joining);
        } catch (Trainer.PartyFullException err) {
            return Optional.empty();
        }
    }

    /**
     * Update the available battles for the day.
     */
    protected void updateAvailableBattles() {
        final Random rng = new Random();
        final List<String> names = getRandomTrainerNames();
        final int amountEnemies = Math.max(3, Math.min(5, 5 * getDifficulty() * getCurrentDay() / 5));
        final int amountMonster = Math.max(1, Math.min(4, 4 * getDifficulty() * getCurrentDay() / 5));

        availableBattles.clear();

        // Enemy generation.
        for (int i = 0; i < amountEnemies; i++) {
            final int index = rng.nextInt(names.size());
            final String name = names.get(index);
            final Trainer enemy = new Trainer(name);
            for (int j = 0; j < amountMonster; j++) {
                enemy.add(shop.randomMonster());
            }
            names.remove(index);
            availableBattles.add(enemy);
        }
    }

    // MARK: -- Actions --

    /**
     * Prepares a battle manager for a battle against selected enemy.
     *
     * @param ui    The UI used to the battle manager.
     * @param index The index of the available battle.
     * @return The battle manager that had been prepared.
     * @throws IndexOutOfBoundsException If the index given does not point to a valid enemy.
     */
    public BattleManager prepareBattle(BattleManager.UI ui, int index) throws IndexOutOfBoundsException {
        // Retrieves enemy from availableBattles and creates a BattleManager
        final Trainer enemy = getAvailableBattles().get(index);
        return new BattleManager(ui, getPlayer(), enemy, getEnvironment());
    }

    /**
     * Use a certain item in the inventory to a monster in the party.
     *
     * @param item         The item being used.
     * @param monsterIndex The monster by their index.
     * @throws Item.NoEffectException               If the item produces no changes.
     * @throws Inventory.ItemNotExistException      If the item does not exist.
     * @throws Trainer.MonsterDoesNotExistException if the monster is null or doesn't exist.
     */
    public void useItemFromInventory(
        Item item,
        int monsterIndex
    ) throws Item.NoEffectException, Inventory.ItemNotExistException, Trainer.MonsterDoesNotExistException {
        if (item == null)
            // Error if the item attempting to be used is invalid.
            throw new Inventory.ItemNotExistException();

        if (monsterIndex < 0 || monsterIndex >= player.getParty().size())
            // Error if the monster index is invalid.
            throw new Trainer.MonsterDoesNotExistException();

        final Monster monster = player.getParty().get(monsterIndex);
        if (monster == null)
            // Error if the monster the item attempting to be used on is invalid.
            throw new Trainer.MonsterDoesNotExistException();

        // Uses the item on the monster.
        inventory.use(item, monster);
    }

    /**
     * Use a certain item in the inventory to a monster in the party.
     *
     * @param item    The item being used.
     * @param monster The monster itself.
     * @throws Item.NoEffectException               If the item produces no changes.
     * @throws Inventory.ItemNotExistException      If the item does not exist.
     * @throws Trainer.MonsterDoesNotExistException if the monster is null or doesn't exist.
     */
    public void useItemFromInventory(
        Item item,
        Monster monster
    ) throws Item.NoEffectException, Inventory.ItemNotExistException, Trainer.MonsterDoesNotExistException {
        if (item == null)
            // Error if the item attempting to be used is invalid
            throw new Inventory.ItemNotExistException();

        if (monster == null)
            // Error if the monster the item attempting to be used on is invalid
            throw new Trainer.MonsterDoesNotExistException();

        // Uses the item on the monster
        inventory.use(item, monster);
    }

    /**
     * Switch the monster by their index.
     *
     * @param from Starting index.
     * @param to   Destination index.
     * @throws IndexOutOfBoundsException If either of the indices are not valid indices for the party.
     */
    public void switchMonsterOnParty(int from, int to) throws IndexOutOfBoundsException {
        player.switchMonster(from, to);
    }

    /**
     * Switch the monster.
     *
     * @param monster Monster to be moved.
     * @param to      Destination index.
     * @throws IndexOutOfBoundsException If either of the indices are not valid indices for the party.
     */
    public void switchMonsterOnParty(Monster monster, int to) throws IndexOutOfBoundsException {
        player.moveMonster(monster, to);
    }

    /**
     * Buy a purchasable from the shop.
     *
     * @param purchasable The purchase being made.
     * @throws Shop.NotInStockException        If the shop does not have any of the item in stock
     * @throws Shop.InsufficientFundsException If the current gold is not enough to make the purchase.
     * @throws Trainer.PartyFullException      if the current party if full.
     */
    public void buy(
        Purchasable purchasable
    ) throws Shop.NotInStockException, Shop.InsufficientFundsException, Trainer.PartyFullException {
        shop.buyPurchasable(purchasable);
        if (purchasable instanceof Item item) {
            // Item is being purchased.
            inventory.add(item);
        } else if (purchasable instanceof Monster monster) {
            // Monster is being purchased.
            player.add(monster);
        }
    }

    /**
     * Sell a purchasable from the shop.
     *
     * @param purchasable The purchase being made.
     * @throws Inventory.ItemNotExistException      If the player does not have any of the item.
     * @throws Trainer.MonsterDoesNotExistException If the monster does not exist.
     */
    public void sell(Purchasable purchasable) throws Inventory.ItemNotExistException, Trainer.MonsterDoesNotExistException {
        if (purchasable instanceof Item item) {
            // Item is being sold.
            inventory.remove(item);
        } else if (purchasable instanceof Monster monster) {
            // Monster is being sold.
            int i = player.getParty().indexOf(monster);
            if (i == -1)
                // Error if index is invalid
                throw new Trainer.MonsterDoesNotExistException();
            player.remove(i);
        }
        shop.sellPurchasable(purchasable);
    }

    // MARK: -- Setters --

    /**
     * Change the player name to a new one.
     *
     * @param name The new name the player choose as a string.
     */
    public void setTrainerName(String name) throws IllegalArgumentException {
        player.setName(name);
    }

    /**
     * Change the current holding of golds.
     *
     * @param gold The new amount of gold as an int.
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * Extend or shrink the maximum total game days.
     *
     * @param maxDays The new total days as an int.
     */
    public void setMaxDays(int maxDays) {
        this.maxDays = maxDays;
    }

    /**
     * Set the new score.
     *
     * @param score The new total score as an int.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Change the environment of the current day.
     * <b>(Internal code, for testing purposes)</b>
     *
     * @param environment The new environment.
     */
    protected void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * Jump to a day.
     * <b>(Internal code, for testing purposes)</b>
     *
     * @param currentDay The day to jump into as an int.
     */
    protected void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }

    /**
     * Change the difficulty of the game.
     * <b>(Internal code, for testing purposes)</b>
     *
     * @param difficulty The new difficulty multiplier as an int.
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Get the list of role based name from the resource text file.
     *
     * @return A Map of index to role name pair.
     */
    private List<String> getRandomTrainerNames() {
        // Opening text file with all enemy names
        final InputStream file = Objects.requireNonNull(GameManager.class.getResourceAsStream("/txt/roles.txt"));
        final Scanner scanner = new Scanner(file);
        // ArrayList with capacity of the number of names
        final ArrayList<String> res = new ArrayList<>(56);
        int i = 0;
        while (scanner.hasNextLine()) {
            final String name = scanner.nextLine();
            if (name.isBlank())
                continue;
            res.add(name);
            i++;
        }
        scanner.close();
        return res;
    }
}


