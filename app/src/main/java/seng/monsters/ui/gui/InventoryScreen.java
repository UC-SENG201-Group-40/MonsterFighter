package seng.monsters.ui.gui;

import seng.monsters.model.*;
import seng.monsters.ui.gui.components.ItemPanel;
import seng.monsters.ui.gui.components.SelectPartyPopUp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * A screen to display the user inventory and allow user to interact with them
 */
public class InventoryScreen extends Screen {

    /**
     * All the list of items
     */
    private final List<Item> items = Item.all();

    /**
     * The inventory of the player
     */
    private final Inventory inventory;

    /**
     * Create an active GUI screen for display player's inventory and allow user to use and sell their item(s)
     *
     * @param gui         The GUI manager
     * @param gameManager The Game logic manager / controller
     */
    public InventoryScreen(GUI gui, GameManager gameManager) {
        super(gui, gameManager);
        inventory = gameManager.getInventory();
    }


    @Override
    public void render() {
        // Label for when an error occurs
        JLabel errorLabel = new JLabel("No monster in party");
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setForeground(new Color(255, 0, 0));
        errorLabel.setBounds(6, 338, 807, 16);
        errorLabel.setVisible(false);
        frame.getContentPane().add(errorLabel);

        // Label for when an item is used or sold
        JLabel itemActionLabel = new JLabel();
        itemActionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        itemActionLabel.setForeground(new Color(0, 0, 0));
        itemActionLabel.setBounds(6, 338, 807, 16);
        itemActionLabel.setVisible(false);
        frame.getContentPane().add(itemActionLabel);

        // Button to return to the main menu
        JButton backToMainMenu = new JButton();
        backToMainMenu.setText("Main menu");
        backToMainMenu.setHorizontalAlignment(SwingConstants.CENTER);
        backToMainMenu.setBounds(331, 366, 156, 30);
        frame.getContentPane().add(backToMainMenu);

        // Positioning values for item panels
        final int distanceFromTop = 100;
        final int distanceBetweenPanel = (819 - 4 * ItemPanel.WIDTH) / 5;

        // Components for each item
        for (int i = 0; i < items.size(); i++) {
            final Item item = items.get(i);
            final int itemCount = gameManager.getInventory().getItemNumber(item);
            final int distanceX = (i + 1) * distanceBetweenPanel + i * ItemPanel.WIDTH;

            // Item information panel
            ItemPanel panel = new ItemPanel(item);
            panel.setBounds(distanceX, distanceFromTop);
            panel.applyToFrame(frame);

            // Label for the number of that item in the player's inventory
            JLabel countLabel = new JLabel(String.format("%dx", itemCount));
            countLabel.setHorizontalAlignment(SwingConstants.CENTER);
            countLabel.setBounds(distanceX, distanceFromTop + ItemPanel.HEIGHT, 79, 30);
            frame.getContentPane().add(countLabel);

            // Label for the sellback price of the item
            JLabel sellLabel = new JLabel(String.format("(%d gold)", item.sellPrice()));
            sellLabel.setHorizontalAlignment(SwingConstants.CENTER);
            sellLabel.setBounds(distanceX + 79, distanceFromTop + ItemPanel.HEIGHT, 79, 30);
            frame.getContentPane().add(sellLabel);

            // Button to use the item
            JButton useButton = new JButton("Use");
            useButton.setBounds(distanceX, distanceFromTop + 30 + ItemPanel.HEIGHT, 79, 30);
            useButton.setEnabled(itemCount > 0);
            frame.getContentPane().add(useButton);

            // Button to sell the item
            JButton sellButton = new JButton("Sell");
            sellButton.setBounds(distanceX + 79, distanceFromTop + 30 + ItemPanel.HEIGHT, 79, 30);
            sellButton.setEnabled(itemCount > 0);
            frame.getContentPane().add(sellButton);

            useButton.addActionListener(
                useItemAction(item, errorLabel, countLabel, itemActionLabel, useButton, sellButton)
            );

            sellButton.addActionListener(
                sellItemAction(item, errorLabel, countLabel, itemActionLabel, useButton, sellButton)
            );
        }

        backToMainMenu.addActionListener(
            backToMainMenuAction()
        );

        frame.setVisible(true);
    }

    /**
     * The action performed when using the sell item button (attempts to sell the item)
     *
     * @param item       The item to be used
     * @param errorLabel The error label to prompt error messages
     * @param countLabel The count label for this item, to update the count once succeed
     * @param itemActionLabel The action label to be updated and displayed when an item is used or sold
     * @param useButton  The use button to enable or disable it if item is not available anymore
     * @param sellButton The button itself to enable or disable it if item is not available anymore
     * @return An action listener that can be passed into the button
     */
    private ActionListener sellItemAction(Item item, JLabel errorLabel, JLabel countLabel, JLabel itemActionLabel, JButton useButton, JButton sellButton) {
        return ignoredEvent -> {
            try {
                // An item is successfully sold
                gameManager.sell(item);
                errorLabel.setVisible(false);
                itemActionLabel.setText(String.format("%s sold!", item.getName()));
                itemActionLabel.setVisible(true);
            } catch (Inventory.ItemNotExistException err) {
                // The player does not have any of that item to sell
                itemActionLabel.setVisible(false);
                errorLabel.setText("There is no such item in your inventory");
                errorLabel.setVisible(true);
            } catch (Trainer.MonsterDoesNotExistException err) {
                itemActionLabel.setVisible(false);
                errorLabel.setText("There is no such monster in your inventory");
                errorLabel.setVisible(true);
            } finally {
                final int newCount = inventory.getItemNumber(item);
                countLabel.setText(String.format("%dx", newCount));
                sellButton.setEnabled(newCount > 0);
                useButton.setEnabled(newCount > 0);
            }
        };
    }

    /**
     * The action performed when using the use item button (displays use item popup)
     *
     * @param item       The item to be used
     * @param errorLabel The error label to prompt error messages
     * @param countLabel The count label for this item, to update the count once succeed
     * @param itemActionLabel The action label to be updated and displayed when an item is used or sold
     * @param useButton  The button itself to enable or disable it if item is not available anymore
     * @param sellButton The sell button to enable or disable it if item is not available anymore
     * @return An action listener that can be passed into the button
     */
    private ActionListener useItemAction(Item item, JLabel errorLabel, JLabel countLabel, JLabel itemActionLabel, JButton useButton, JButton sellButton) {
        return ignoredEvent -> {
            final int count = inventory.getItemNumber(item);

            // Do not proceed if the trainer has no monster and the item count is 0
            if (gameManager.getPlayer().getParty().size() <= 0 || count <= 0) {
                errorLabel.setText(count <= 0 ? String.format("You don't have any %ss!", item.getName().toLowerCase())
                                                : "Your party is empty!");
                errorLabel.setVisible(true);
                return;
            }

            SelectPartyPopUp popUp = new SelectPartyPopUp(gameManager);
            popUp.onChosen(
                popUpChosenAction(item, errorLabel, countLabel, itemActionLabel, useButton, sellButton)
            );
        };
    }

    /**
     * The action performed after user has chosen a monster from the pop-up (attempts to use the item on the monster)
     *
     * @param item       The item to be used on the monster
     * @param errorLabel The error label to display failure in using the item
     * @param countLabel The count to update the count of item after usage
     * @param itemActionLabel The action label to be updated and displayed when an item is used or sold
     * @param useButton  The use button to disable if item count hits 0
     * @param sellButton The sell button to enable or disable it if item is not available anymore
     * @return A consumer function to be passed to the pop-up as callback
     */
    private BiConsumer<ActionEvent, Monster> popUpChosenAction(Item item, JLabel errorLabel, JLabel countLabel, JLabel itemActionLabel, JButton useButton, JButton sellButton) {
        return (ignoredEvent, monster) -> {
            try {
                // Item is successfully used
                gameManager.useItemFromInventory(item, monster);
                errorLabel.setVisible(false);
                itemActionLabel.setText(String.format("%s used on %s!", item.getName(), monster.getName()));
                itemActionLabel.setVisible(true);
            } catch (Inventory.ItemNotExistException err) {
                // Player does not have any of that item to use
                itemActionLabel.setVisible(false);
                errorLabel.setText(String.format("You don't have any %ss!", item.getName().toLowerCase()));
                errorLabel.setVisible(true);
            } catch (Trainer.MonsterDoesNotExistException err) {
                // Monster item is attempting to be used on is no longer in the party
                itemActionLabel.setVisible(false);
                errorLabel.setText(String.format("%s is no longer in your party!", monster.getName()));
                errorLabel.setVisible(true);
            } catch (Item.NoEffectException err) {
                // Item has no effect on the monster it is being used on
                itemActionLabel.setVisible(false);
                errorLabel.setText("The item has no effect, " + err.getMessage().toLowerCase() + "!");
                errorLabel.setVisible(true);
            } finally {
                final int newCount = inventory.getItemNumber(item);
                countLabel.setText(String.format("%dx", newCount));
                sellButton.setEnabled(newCount > 0);
                useButton.setEnabled(newCount > 0);
            }
        };
    }

    /**
     * The action performed when the main menu button is used (returns to the main menu)
     */
    private ActionListener backToMainMenuAction() {
        return ignoredEvent -> gui.navigateBackToMainMenu();
    }
}
