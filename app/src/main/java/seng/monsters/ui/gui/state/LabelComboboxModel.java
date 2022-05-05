//
//  LabelComboboxModel.java
//  seng-monsters
//
//  Created by d-exclaimation on 20:33.
//  Copyright © 2022 d-exclaimation. All rights reserved.
//
package seng.monsters.ui.gui.state;

import javax.swing.*;
import java.util.List;
import java.util.function.Function;

/**
 * A combo box model using a string from a list of item and a function to get its label
 * @param <T> The type of item in list
 */
public class LabelComboboxModel<T> extends DefaultComboBoxModel<String> {
    /**
     * A combo box model using a string from a list of item and a function to get its label
     * @param items The list of item
     * @param getLabel A function to get the label from the item
     */
    public LabelComboboxModel(List<T> items, Function<T, String> getLabel) {
        super(
            items.stream()
                .map(getLabel)
                .toList()
                .toArray(new String[items.size()])
        );
    }
}
