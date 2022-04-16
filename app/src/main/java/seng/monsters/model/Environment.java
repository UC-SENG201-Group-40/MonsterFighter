//
//  Environment.java
//  seng-practice
//
//  Created by d-exclaimation on 3:26 PM.
//  Copyright © 2022 d-exclaimation. All rights reserved.
//
package seng.monsters.model;

import java.util.List;
import java.util.Random;

/**
 * Environment about the field of battle
 */
public enum Environment {
    DESERT, FIELD, BEACH, HILL, FOREST, URBAN;

    /**
     * Get all the possible environment
     * @return A list of all unique environment
     */
    static List<Environment> all() {
        return List.of(
            Environment.BEACH,
            Environment.DESERT,
            Environment.FIELD,
            Environment.FOREST,
            Environment.HILL,
            Environment.URBAN
        );
    }

    /**
     * Generates a random environment
     * @return An environment randomly chosen from the list of environments
     */
    public static Environment generateRandomEnvironment() {
        Environment[] environments = Environment.values();
        final var random = new Random();
        return environments[random.nextInt(environments.length)];
    }

    @Override
    public String toString() {
        return switch (this) {
            case DESERT -> "desert";
            case BEACH -> "beach";
            case FIELD -> "field";
            case FOREST -> "forest";
            case HILL -> "hill";
            case URBAN -> "urban";
        };
    }
}