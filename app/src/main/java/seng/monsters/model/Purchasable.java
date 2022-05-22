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
     * @return The buy price of the purchasable.
     */
    int buyPrice();

    /**
     * The cost of the purchasable when sold back.
     *
     * @return The sell price of the purchasable.
     */
    int sellPrice();

    /**
     * The name of the purchasable.
     *
     * @return The name of the purchasable.
     */
    String getName();

    /**
     * A description of the purchasable.
     *
     * @return The description of the purchasable.
     */
    String description();
}