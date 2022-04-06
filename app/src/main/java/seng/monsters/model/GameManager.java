//
//  GameManager.java
//  seng-practice
//
//  Created by d-exclaimation on 9:20 PM.
//  Copyright © 2022 d-exclaimation. All rights reserved.
//
package seng.monsters.model;

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

    public GameManager() {
        trainer = new Trainer("");
        inventory = new Inventory();
        shop = new Shop(this);

        // TODO: Available battles
    }


    public int getGold() {
        return gold;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void setMaxDays(int maxDays) {
        this.maxDays = maxDays;
    }
}
