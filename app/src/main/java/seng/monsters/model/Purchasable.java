//
//  Purchasable.java
//  seng-practice
//
//  Created by Vincent on 3:19 PM.

//
package seng.monsters.model;

/**
 * Any class that can be bought and sold into the shop.
 */
public interface Purchasable {
    /**
     * The cost of the purchasable when bought.
     *
     * @return The price as integer.
     */
    int buyPrice();

    /**
     * The cost of the purchasable when sold back.
     *
     * @return The price as integer.
     */
    int sellPrice();

    /**
     * The name of the purchasable.
     *
     * @return The name of the purchasable as string.
     */
    String getName();

    /**
     * A description of the purchasable.
     *
     * @return The description of the purchasable as a string.
     */
    String description();
}