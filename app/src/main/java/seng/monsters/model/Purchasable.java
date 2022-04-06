//
//  Purchasable.java
//  seng-practice
//
//  Created by d-exclaimation on 3:19 PM.
//  Copyright © 2022 d-exclaimation. All rights reserved.
//
package seng.monsters.model;

/**
 * Any class that can be bought and sold into the shop
 */
public interface Purchasable {
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