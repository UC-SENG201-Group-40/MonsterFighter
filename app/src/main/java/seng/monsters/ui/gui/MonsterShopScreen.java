package seng.monsters.ui.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import seng.monsters.model.GameManager;
import seng.monsters.model.Monster;
import seng.monsters.model.Shop;
import seng.monsters.model.Trainer;
import seng.monsters.ui.gui.components.DetailedMonsterPanel;
import seng.monsters.ui.gui.components.JoiningPopUp;
import seng.monsters.ui.gui.state.LabelComboboxModel;
import seng.monsters.ui.gui.state.State;

/**
 * A screen to allow user to purchase monsters from the shop
 */
public class MonsterShopScreen extends Screen {

    /**
     * The currently chosen monster
     */
    private final State<Monster> chosenMonster;


    /**
     * Create an active GUI screen for displaying the monster shop and allow purchasing
     *
     * @param gui         The GUI manager
     * @param gameManager The Game logic manager / controller
     */
    public MonsterShopScreen(GUI gui, GameManager gameManager) {
        super(gui, gameManager);
        chosenMonster = State.of(gameManager.getShop().getMonsterStock().get(0));
    }

    @Override
    public void render() {
        Screen
            .imageIconFromResource(
                String.format("/images/%s.jpeg", gameManager.getEnvironment().toString().toLowerCase())
            )
            .ifPresent(icon -> frame.setContentPane(new JLabel(icon)));

        // Button to return to the main menu
        JButton backToMainMenu = new JButton();
        backToMainMenu.setText("Main menu");
        backToMainMenu.setHorizontalAlignment(SwingConstants.CENTER);
        backToMainMenu.setBounds(330, 398, 156, 30);
        frame.getContentPane().add(backToMainMenu);

        // Panel to contain the shop's combobox and the player's gold amount
        JPanel monsterShopPanel = new JPanel();
        monsterShopPanel.setBackground(Color.WHITE);
        monsterShopPanel.setBounds(476, 130, 238, 130);
        monsterShopPanel.setLayout(null);
        frame.getContentPane().add(monsterShopPanel);

        // Label displaying amount of gold
        JLabel goldLabel = new JLabel(String.format("You have %d gold.", gameManager.getGold()));
        goldLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        goldLabel.setBackground(Color.WHITE);
        goldLabel.setBounds(8, 0, 222, 30);
        monsterShopPanel.add(goldLabel);

        JLabel promptLabel = new JLabel("Choose which monster to buy:");
        promptLabel.setHorizontalAlignment(SwingConstants.LEADING);
        promptLabel.setBackground(Color.WHITE);
        promptLabel.setBounds(8, 40, 222, 30);
        monsterShopPanel.add(promptLabel);

        // Combobox for buying monster choices
        JComboBox<String> monsterStockComboBox = new JComboBox<>();
        monsterStockComboBox.setModel(
            new LabelComboboxModel<>(gameManager.getShop().getMonsterStock(), Monster::uniqueName)
        );
        monsterStockComboBox.setSelectedIndex(0);
        monsterStockComboBox.setBounds(0, 70, 236, 30);
        monsterShopPanel.add(monsterStockComboBox);

        DetailedMonsterPanel monsterPanel = new DetailedMonsterPanel(chosenMonster.get(), true);
        monsterPanel.setBounds(66, 76);
        monsterPanel.applyToFrame(frame);

        // Button to buy the monster
        JButton buyButton = new JButton();
        buyButton.setText("Purchase");
        buyButton.setBounds(66, 86 + DetailedMonsterPanel.HEIGHT, DetailedMonsterPanel.WIDTH, 20);
        buyButton.setEnabled(!gameManager.getShop().getMonsterStock().isEmpty());
        frame.getContentPane().add(buyButton);

        // Label for when an error occurs
        JLabel errorLabel = new JLabel();
        errorLabel.setBounds(4, 100, 230, 30);
        errorLabel.setVisible(false);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setForeground(new Color(255, 0, 0));
        monsterShopPanel.add(errorLabel);

        // Label for when a monster is bought
        JLabel monsterBoughtLabel = new JLabel();
        monsterBoughtLabel.setBounds(8, 100, 222, 30);
        monsterBoughtLabel.setVisible(false);
        monsterBoughtLabel.setHorizontalAlignment(SwingConstants.CENTER);
        monsterShopPanel.add(monsterBoughtLabel);

        // Refresh the detailed monster panel when a new one is selected
        chosenMonster.onChange(monsterPanel::refresh);

        monsterStockComboBox.addActionListener(comboBoxSelectionAction(monsterStockComboBox));
        backToMainMenu.addActionListener(backToMainMenuAction());
        buyButton.addActionListener(buyAction(buyButton, errorLabel, monsterBoughtLabel, goldLabel, monsterStockComboBox));

        frame.setVisible(true);
    }


    /**
     * The action performed when a selection is made in the combobox (sets the chosenMonster)
     *
     * @param comboBox The combo box to get the selection
     * @return An action listener for the combo box
     */
    private ActionListener comboBoxSelectionAction(JComboBox<String> comboBox) {
        return ignoredEvent -> {
            final int index = comboBox.getSelectedIndex();
            if (index < 0 || index >= gameManager.getShop().getMonsterStock().size())
                return;
            chosenMonster.set(gameManager.getShop().getMonsterStock().get(index));
        };
    }

    /**
     * The action performed when using the buy button (attempts to buy the monster)
     *
     * @param buyButton     The buy button to be disabled while processing payment
     * @param errorLabel    The error label to display failure in purchase
     * @param goldLabel     The gold label to update the gold count
     * @param partyComboBox The combo box to update the monster displayed in the shop
     * @return An action listener for the buy button
     */
    private ActionListener buyAction(JButton buyButton, JLabel errorLabel, JLabel monsterBoughtLabel, JLabel goldLabel, JComboBox<String> partyComboBox) {
        return ignoredEvent -> {
            try {
                // A monster is successfully bought and the corresponding monster popup appears
                final Monster boughtMonster = chosenMonster.get();

                gameManager.buy(boughtMonster);
                errorLabel.setVisible(false);
                buyButton.setEnabled(false);

                JoiningPopUp popUp = new JoiningPopUp(chosenMonster.get());
                popUp.onEnd(popUpAction(buyButton));
                monsterBoughtLabel.setText(String.format("%s has been bought!", chosenMonster.get().getName()));
                monsterBoughtLabel.setVisible(true);
            } catch (Shop.InsufficientFundsException err) {
                // The player does not have enough money to buy that monster
                monsterBoughtLabel.setVisible(false);
                errorLabel.setVisible(true);
                errorLabel.setText(String.format(
                    "Insufficient gold to buy %s!", chosenMonster.get().getName()
                ));
            } catch (Shop.NotInStockException err) {
                // The monster is no longer in the shop
                monsterBoughtLabel.setVisible(false);
                errorLabel.setVisible(true);
                errorLabel.setText(String.format(
                    "The monster %s is not in stock!", chosenMonster.get().getName()
                ));
            } catch (Trainer.PartyFullException err) {
                // The player's party is full
                monsterBoughtLabel.setVisible(false);
                errorLabel.setVisible(true);
                errorLabel.setText("Your party is full!");
            } finally {
                final List<Monster> stock = gameManager.getShop().getMonsterStock();
                if (stock.isEmpty()) {
                    gui.navigateBackToMainMenu();
                } else {
                    partyComboBox.setModel(
                        new LabelComboboxModel<>(stock, Monster::uniqueName)
                    );
                    partyComboBox.setSelectedIndex(0);
                    chosenMonster.set(stock.get(0));
                    goldLabel.setText(String.format("You have %d gold.", gameManager.getGold()));
                }
            }
        };
    }

    /**
     * The action performed after player has named the bought monster
     *
     * @param buyButton The button to be re-enabled
     * @return A consumer function for the pop up
     */
    private Consumer<ActionEvent> popUpAction(JButton buyButton) {
        return ignoredEvent -> buyButton.setEnabled(true);
    }

    /**
     * The action performed when the main menu button is used (returns to the main menu)
     */
    private ActionListener backToMainMenuAction() {
        return ignoredEvent -> gui.navigateBackToMainMenu();
    }
}
