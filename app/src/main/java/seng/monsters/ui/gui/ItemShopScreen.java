package seng.monsters.ui.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import seng.monsters.model.GameManager;
import seng.monsters.model.Item;
import seng.monsters.model.Shop;
import seng.monsters.model.Trainer;
import seng.monsters.ui.gui.components.ItemPanel;

/**
 * A screen to allow user to purchase items from the shop
 */
public class ItemShopScreen extends Screen {

    /**
     * All the list of items
     */
    private final List<Item> items = Item.all();

    /**
     * Create an active GUI screen for purchasing item from the shop
     *
     * @param gui         The GUI manager
     * @param gameManager The Game logic manager / controller
     */
    public ItemShopScreen(GUI gui, GameManager gameManager) {
        super(gui, gameManager);
    }


    @Override
    public void render() {
        JLabel titleLabel = new JLabel("Item shop! (No refund)");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 26));
        titleLabel.setBounds((Screen.WIDTH - 600) / 2, 42, 600, 39);
        frame.getContentPane().add(titleLabel);

        JLabel errorLabel = new JLabel("No monster in party");
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setForeground(new Color(255, 0, 0));
        errorLabel.setBounds(6, 338, 807, 16);
        errorLabel.setVisible(false);
        frame.getContentPane().add(errorLabel);

        JLabel itemBoughtLabel = new JLabel();
        itemBoughtLabel.setHorizontalAlignment(SwingConstants.CENTER);
        itemBoughtLabel.setForeground(new Color(0, 0, 0));
        itemBoughtLabel.setBounds(6, 338, 807, 16);
        itemBoughtLabel.setVisible(false);
        frame.getContentPane().add(itemBoughtLabel);

        JLabel goldLabel = new JLabel(String.format("Your own %d gold", gameManager.getGold()));
        goldLabel.setHorizontalAlignment(SwingConstants.LEADING);
        goldLabel.setBounds(206, 366, 200, 30);
        frame.getContentPane().add(goldLabel);

        JButton backToMainMenu = new JButton();
        backToMainMenu.setText("Main menu");
        backToMainMenu.setHorizontalAlignment(SwingConstants.CENTER);
        backToMainMenu.setBounds(456, 366, 156, 30);
        frame.getContentPane().add(backToMainMenu);

        final var distanceFromTop = 100;
        final var distanceBetweenPanel = (819 - 4 * ItemPanel.WIDTH) / 5;

        for (var i = 0; i < items.size(); i++) {
            final var item = items.get(i);
            final var itemCount = gameManager.getShop().getItemStock(item);
            final var distanceX = (i + 1) * distanceBetweenPanel + i * ItemPanel.WIDTH;

            ItemPanel panel = new ItemPanel(item);
            panel.setBounds(distanceX, distanceFromTop);
            panel.applyToFrame(frame);

            JLabel countLabel = new JLabel(String.format("%dx (%d gold)", itemCount, item.buyPrice()));
            countLabel.setHorizontalAlignment(SwingConstants.CENTER);
            countLabel.setBounds(distanceX, distanceFromTop + ItemPanel.HEIGHT, 158, 30);
            frame.getContentPane().add(countLabel);


            JButton buyButton = new JButton("Buy");
            buyButton.setBounds(distanceX, distanceFromTop + 30 + ItemPanel.HEIGHT, 158, 30);
            buyButton.setEnabled(itemCount > 0);
            frame.getContentPane().add(buyButton);

            buyButton.addActionListener(
                buyItemAction(item, errorLabel, countLabel, itemBoughtLabel, buyButton, goldLabel)
            );
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
     * @param buyButton  The button itself to enable or disable it if item is not available anymore
     * @param goldLabel  The label for the amount of gold
     * @return An action listener that can be passed into the button
     */
    private ActionListener buyItemAction(Item item, JLabel errorLabel, JLabel countLabel, JLabel itemBoughtLabel, JButton buyButton, JLabel goldLabel) {
        return ignoredEvent -> {
            try {
                gameManager.buy(item);
                errorLabel.setVisible(false);
                itemBoughtLabel.setText(String.format("%s bought!", item.getName()));
                itemBoughtLabel.setVisible(true);
            } catch (Shop.InsufficientFundsException err) {
                itemBoughtLabel.setVisible(false);
                errorLabel.setVisible(true);
                errorLabel.setText(String.format(
                    "You're too poor! You have %d gold and the item costs %d!", gameManager.getGold(), item.buyPrice()
                ));
            } catch (Shop.NotInStockException err) {
                itemBoughtLabel.setVisible(false);
                errorLabel.setVisible(true);
                errorLabel.setText(String.format(
                    "The item, %s not in stock!", item.getName()
                ));
            } catch (Trainer.PartyFullException err) {
                itemBoughtLabel.setVisible(false);
                errorLabel.setVisible(true);
                errorLabel.setText("Your party is full!");
            } finally {
                final var newCount = gameManager.getShop().getItemStock(item);
                countLabel.setText(String.format("%dx (%d gold)", newCount, item.buyPrice()));
                buyButton.setEnabled(newCount > 0);
                goldLabel.setText(String.format("Your own %d gold", gameManager.getGold()));
            }
        };
    }

    /**
     * The action performed when the user chose to return the main menu
     */
    private ActionListener backToMainMenuAction() {
        return ignoredEvent -> gui.navigateBackToMainMenu();
    }
}
