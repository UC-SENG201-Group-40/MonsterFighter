//
//  Environment.java
//  seng-practice
//
//  Created by d-exclaimation on 3:26 PM.
//  Copyright © 2022 d-exclaimation. All rights reserved.
//
package seng.monsters.model;

import java.util.List;

/**
 * Environment about the field of battle
 */
public enum Environment {
    DESERT, FIELD, BEACH, HILL, FOREST, URBAN;

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