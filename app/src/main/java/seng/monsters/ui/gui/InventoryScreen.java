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
     * Create the application.
     */
    public InventoryScreen(GUI gui, GameManager gameManager) {
        super(gui, gameManager);
        inventory = gameManager.getInventory();
    }


    @Override
    public void render() {
        JLabel errorLabel = new JLabel("No monster in party");
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setForeground(new Color(255, 0, 0));
        errorLabel.setBounds(6, 336, 807, 16);
        errorLabel.setVisible(false);
        frame.getContentPane().add(errorLabel);


        JButton backToMainMenu = new JButton();
        backToMainMenu.setText("Main menu");
        backToMainMenu.setHorizontalAlignment(SwingConstants.CENTER);
        backToMainMenu.setBounds(331, 366, 156, 30);
        frame.getContentPane().add(backToMainMenu);

        final var distanceFromTop = 100;
        final var distanceBetweenPanel = (819 - 4 * ItemPanel.WIDTH) / 5;

        for (var i = 0; i < items.size(); i++) {
            final var item = items.get(i);
            final var itemCount = gameManager.getInventory().getItemNumber(item);
            final var distanceX = (i + 1) * distanceBetweenPanel + i * ItemPanel.WIDTH;

            ItemPanel panel = new ItemPanel(item);
            panel.setBounds(distanceX, distanceFromTop);
            panel.applyToFrame(frame);

            JLabel countLabel = new JLabel(String.format("%dx", itemCount));
            countLabel.setHorizontalAlignment(SwingConstants.CENTER);
            countLabel.setBounds(distanceX, distanceFromTop + 20 + ItemPanel.HEIGHT, 58, 30);
            frame.getContentPane().add(countLabel);


            JButton useButton = new JButton("Use");
            useButton.setBounds(distanceX + 58, distanceFromTop + 20 + ItemPanel.HEIGHT, 100, 30);
            useButton.setEnabled(itemCount > 0);
            useButton.addActionListener(
                useItemAction(item, errorLabel, countLabel, useButton)
            );
            frame.getContentPane().add(useButton);
        }

        backToMainMenu.addActionListener(
            backToMainMenuAction()
        );

        frame.setVisible(true);
    }

    /**
     * The action performed when using an item
     *
     * @param item       The item to be used
     * @param errorLabel The error label to prompt error messages
     * @param countLabel The count label for this item, to update the count once succeed
     * @param useButton  The button itself to enable or disable it if item is not available anymore
     * @return An action listener that can be passed into the button
     */
    private ActionListener useItemAction(Item item, JLabel errorLabel, JLabel countLabel, JButton useButton) {
        return e -> {
            final int count = inventory.getItemNumber(item);

            // Do not proceed if the trainer has no monster and the item count is 0
            if (gameManager.getTrainer().getParty().size() <= 0 || count <= 0) {
                errorLabel.setText(count <= 0 ? "There is no such item in your inventory" : "There is no monster to apply item to");
                errorLabel.setVisible(true);
                return;
            }

            SelectPartyPopUp popUp = new SelectPartyPopUp(gameManager);
            popUp.onChosen(
                popUpChosenAction(item, errorLabel, countLabel, useButton)
            );
            useButton.setEnabled(false);
        };
    }

    /**
     * The action performed after user has chosen a monster from the pop-up
     *
     * @param item       The item to be used on the monster
     * @param errorLabel The error label to display failure in using the item
     * @param countLabel The count to update the count of item after usage
     * @param useButton  The button to disable if item count hits 0
     * @return A consumer function to be passed to the pop-up as callback
     */
    private BiConsumer<ActionEvent, Monster> popUpChosenAction(Item item, JLabel errorLabel, JLabel countLabel, JButton useButton) {
        return (e, monster) -> {
            try {
                gameManager.useItemFromInventory(item, monster);

                final var newCount = inventory.getItemNumber(item);
                	System.out.println(newCount);
                countLabel.setText(String.format("%dx", newCount));
                useButton.setEnabled(newCount >= 0);
                errorLabel.setVisible(false);
            } catch (Inventory.ItemNotExistException err) {
                errorLabel.setText("There is no such item in your inventory");
                errorLabel.setVisible(true);
            } catch (Trainer.MonsterDoesNotExistException err) {
                errorLabel.setText("There is no such monster in your party");
                errorLabel.setVisible(true);
            } catch (Item.NoEffectException err) {
                errorLabel.setText("The item produces no effect, " + err.getMessage());
                errorLabel.setVisible(true);
            } finally {
                useButton.setEnabled(true);
            }
        };
    }

    /**
     * The action performed when the user chose to return the main menu
     */
    private ActionListener backToMainMenuAction() {
        return e -> gui.navigateBackToMainMenu();
    }
}
