package seng.monsters.ui.gui;

import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.ImageIcon;
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
     * Create the application.
     */
    public MonsterShopScreen(GUI gui, GameManager gameManager) {
        super(gui, gameManager);
        chosenMonster = State.of(gameManager.getShop().getMonsterStock().get(0));
    }

    /**
     * @wbp.parser.entryPoint
     */
    @Override
    public void render() {
        frame.setContentPane(new JLabel(
            new ImageIcon(
                Objects.requireNonNull(BattleScreen.class.getResource(
                    String.format("/images/%s.jpeg", gameManager.getEnvironment().toString())
                )))
        ));


        JButton backToMainMenu = new JButton();
        backToMainMenu.setText("Main menu");
        backToMainMenu.setHorizontalAlignment(SwingConstants.CENTER);
        backToMainMenu.setBounds(330, 398, 156, 30);
        frame.getContentPane().add(backToMainMenu);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBounds(476, 130, 238, 130);
        panel.setLayout(null);
        frame.getContentPane().add(panel);

        JLabel goldLabel = new JLabel(String.format("Your own %d gold", gameManager.getGold()));
        goldLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        goldLabel.setBackground(Color.WHITE);
        goldLabel.setBounds(8, 0, 222, 30);
        panel.add(goldLabel);

        JLabel promptLabel = new JLabel("Choose which monster to buy:");
        promptLabel.setHorizontalAlignment(SwingConstants.LEADING);
        promptLabel.setBackground(Color.WHITE);
        promptLabel.setBounds(8, 40, 222, 30);
        panel.add(promptLabel);

        JComboBox<String> monsterStockComboBox = new JComboBox<>();
        monsterStockComboBox.setModel(
            new LabelComboboxModel<>(gameManager.getShop().getMonsterStock(), Monster::uniqueName)
        );
        monsterStockComboBox.setSelectedIndex(0);
        monsterStockComboBox.setBounds(0, 70, 236, 30);
        panel.add(monsterStockComboBox);

        DetailedMonsterPanel monsterPanel = new DetailedMonsterPanel(chosenMonster.get(), true);
        monsterPanel.setBounds(66, 76);
        monsterPanel.applyToFrame(frame);

        JButton buyButton = new JButton();
        buyButton.setText("Purchase");
        buyButton.setBounds(66, 86 + DetailedMonsterPanel.HEIGHT, DetailedMonsterPanel.WIDTH, 20);
        buyButton.setEnabled(!gameManager.getShop().getMonsterStock().isEmpty());
        frame.getContentPane().add(buyButton);

        JLabel errorLabel = new JLabel();
        errorLabel.setBounds(8, 100, 222, 30);
        errorLabel.setVisible(false);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setForeground(new Color(255, 0, 0));
        panel.add(errorLabel);

        chosenMonster.onChange(monsterPanel::refresh);

        monsterStockComboBox.addActionListener(comboBoxSelectionAction(monsterStockComboBox));
        backToMainMenu.addActionListener(backToMainMenuAction());
        buyButton.addActionListener(buyAction(buyButton, errorLabel, goldLabel, monsterStockComboBox));

        frame.setVisible(true);
    }


    /**
     * The action performed when a selection is made in the combobox
     *
     * @param comboBox The combo box to get the selection
     * @return An action listener for the combo box
     */
    private ActionListener comboBoxSelectionAction(JComboBox<String> comboBox) {
        return ignoredEvent -> {
            final var index = comboBox.getSelectedIndex();
            if (index < 0 || index >= gameManager.getShop().getMonsterStock().size())
                return;
            chosenMonster.set(gameManager.getShop().getMonsterStock().get(index));
        };
    }

    /**
     * The action performed when attempting to buy a monster from the shop
     *
     * @param buyButton     The buy button to be disabled while processing payment
     * @param errorLabel    The error label to display failure in purchase
     * @param goldLabel     The gold label to update the gold count
     * @param partyComboBox The combo box to update the monster displayed in the shop
     * @return An action listener for the buy button
     */
    private ActionListener buyAction(JButton buyButton, JLabel errorLabel, JLabel goldLabel, JComboBox<String> partyComboBox) {
        return ignoredEvent -> {
            try {
                final var boughtMonster = chosenMonster.get();

                gameManager.buy(boughtMonster);
                errorLabel.setVisible(false);
                buyButton.setEnabled(false);

                JoiningPopUp popUp = new JoiningPopUp(chosenMonster.get());
                popUp.onEnd(popUpAction(buyButton));
            } catch (Shop.InsufficientFundsException err) {
                errorLabel.setVisible(true);
                errorLabel.setText(String.format(
                    "Insufficient gold to buy %s!", chosenMonster.get().getName()
                ));
            } catch (Shop.NotInStockException err) {
                errorLabel.setVisible(true);
                errorLabel.setText(String.format(
                    "The item, %s not in stock!", chosenMonster.get().getName()
                ));
            } catch (Trainer.PartyFullException err) {
                errorLabel.setVisible(true);
                errorLabel.setText("Your party is full!");
            } finally {
                final var stock = gameManager.getShop().getMonsterStock();
                if (stock.isEmpty()) {
                    gui.navigateBackToMainMenu();
                } else {
                    partyComboBox.setModel(
                        new LabelComboboxModel<>(stock, Monster::uniqueName)
                    );
                    partyComboBox.setSelectedIndex(0);
                    chosenMonster.set(stock.get(0));
                    goldLabel.setText(String.format("Your own %d gold", gameManager.getGold()));
                }
            }
        };
    }

    /**
     * The action performed after player named the bought monster
     *
     * @param buyButton The button to be re-enabled
     * @return A consumer function for the pop up
     */
    private Consumer<ActionEvent> popUpAction(JButton buyButton) {
        return ignoredEvent -> buyButton.setEnabled(true);
    }

    /**
     * The action performed when the user chose to return the main menu
     */
    private ActionListener backToMainMenuAction() {
        return ignoredEvent -> gui.navigateBackToMainMenu();
    }
}
