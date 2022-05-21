package seng.monsters.ui.gui;

import seng.monsters.model.GameManager;
import seng.monsters.model.Inventory;
import seng.monsters.model.Monster;
import seng.monsters.model.Trainer;
import seng.monsters.ui.gui.components.DetailedMonsterPanel;
import seng.monsters.ui.gui.components.PartyPanel;
import seng.monsters.ui.gui.components.SelectPartyPopUp;
import seng.monsters.ui.gui.state.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * A screen to prompt user with their party and allow them to move and sell theirs monster
 */
public class PartyScreen extends Screen {

    /**
     * The trainer
     */
    private final Trainer trainer;

    /**
     * The currently chosen monster
     */
    private final State<Monster> chosenMonster;


    /**
     * Create an active GUI screen for displaying the party of the player and allow moving and selling monsters
     *
     * @param gui         The GUI manager
     * @param gameManager The Game logic manager / controller
     */
    public PartyScreen(GUI gui, GameManager gameManager) {
        super(gui, gameManager);
        trainer = gameManager.getTrainer();
        chosenMonster = State.of(trainer.getParty().get(0));
    }

    @Override
    public void render() {
        Screen
            .imageIconFromResource(
                String.format("/images/%s.jpeg", gameManager.getEnvironment().toString().toLowerCase())
            )
            .ifPresent(icon -> frame.setContentPane(new JLabel(icon)));

        JButton backToMainMenu = new JButton("Main menu");
        backToMainMenu.setBounds(351, 398, 117, 29);
        frame.getContentPane().add(backToMainMenu);

        JLabel monsterActionLabel = new JLabel();
        monsterActionLabel.setBounds(66, 51 + DetailedMonsterPanel.HEIGHT, DetailedMonsterPanel.WIDTH, 20);
        monsterActionLabel.setVisible(false);
        monsterActionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.getContentPane().add(monsterActionLabel);

        JLabel errorLabel = new JLabel();
        errorLabel.setBounds(66, 51 + DetailedMonsterPanel.HEIGHT, DetailedMonsterPanel.WIDTH, 20);
        errorLabel.setVisible(false);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setForeground(new Color(255, 0, 0));
        frame.getContentPane().add(errorLabel);

        PartyPanel partyPanel = new PartyPanel(trainer);
        partyPanel.setBounds(436, 68);
        partyPanel.applyToFrame(frame);

        DetailedMonsterPanel monsterPanel = new DetailedMonsterPanel(chosenMonster.get(), false);
        monsterPanel.setBounds(66, 76);
        monsterPanel.applyToFrame(frame);

        JButton moveButton = new JButton();
        moveButton.setText("Move");
        moveButton.setBounds(66, 86 + DetailedMonsterPanel.HEIGHT, DetailedMonsterPanel.WIDTH / 2, 20);
        frame.getContentPane().add(moveButton);

        JButton sellButton = new JButton();
        sellButton.setText("Sell");
        sellButton.setBounds(66 + DetailedMonsterPanel.WIDTH / 2, 86 + DetailedMonsterPanel.HEIGHT, DetailedMonsterPanel.WIDTH / 2, 20);
        sellButton.setEnabled(trainer.getParty().size() > 1);
        frame.getContentPane().add(sellButton);

        chosenMonster.onChange(monsterPanel::refresh);

        partyPanel.onAction(slotAction());
        backToMainMenu.addActionListener(backToMainMenuAction());
        moveButton.addActionListener(moveAction(moveButton, sellButton, errorLabel, monsterActionLabel, partyPanel));
        sellButton.addActionListener(sellAction(sellButton, errorLabel, monsterActionLabel, partyPanel));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /**
     * The action performed when a monster is selected
     *
     * @return A bi consumer for the PartySlotPanel
     */
    private BiConsumer<ActionEvent, Monster> slotAction() {
        return (ignoredEvent, mon) -> chosenMonster.set(mon);
    }

    /**
     * The action performed when a monster is being sold
     *
     * @param sellButton The sell button to be disabled so to prevent concurrent selling the same monster
     * @param errorLabel The error label to display the exception
     * @param monsterActionLabel The action label to be updated and displayed when a monster is moved or sold
     * @param partyPanel The party panel to be updated
     * @return An action listener for the sell button
     */
    private ActionListener sellAction(JButton sellButton, JLabel errorLabel, JLabel monsterActionLabel, PartyPanel partyPanel) {
        return ignoredEvent -> {
            try {
                gameManager.sell(chosenMonster.get());
                errorLabel.setVisible(false);
                monsterActionLabel.setText(String.format("%s sold!", chosenMonster.get().getName()));
                monsterActionLabel.setVisible(true);

            } catch (Trainer.MonsterDoesNotExistException err) {
                monsterActionLabel.setVisible(false);
                errorLabel.setVisible(true);
                errorLabel.setText("This monster is no longer in your party!");
            } catch (Inventory.ItemNotExistException err) {
                monsterActionLabel.setVisible(false);
                errorLabel.setVisible(true);
                errorLabel.setText("This item isn't in your invenotyr");
            } finally {
                partyPanel.refresh();
                sellButton.setEnabled(trainer.getParty().size() > 1);
                chosenMonster.set(trainer.getParty().get(0));
            }
        };
    }

    /**
     * The action performed when attempting to move a monster
     *
     * @param moveButton The move button to be re-enabled
     * @param sellButton The sell button to be re-enabled
     * @param errorLabel The error label to display failure in switching
     * @param monsterActionLabel The action label to be updated and displayed when a monster is moved or sold
     * @param partyPanel The party panel to update the display order of the party
     * @return An action listener for the move button
     */
    private ActionListener moveAction(JButton moveButton, JButton sellButton, JLabel errorLabel, JLabel monsterActionLabel, PartyPanel partyPanel) {
        return ignoredEvent -> {
            SelectPartyPopUp popUp = new SelectPartyPopUp(gameManager);
            popUp.onChosen(switchAction(moveButton, sellButton, errorLabel, monsterActionLabel, partyPanel));
        };
    }

    /**
     * The action performed when switching the selected monster to another monster
     *
     * @param moveButton The move button to be re-enabled
     * @param sellButton The sell button to be re-enabled
     * @param errorLabel The error label to display failure in switching
     * @param monsterActionLabel The action label to be updated and displayed when a monster is moved or sold
     * @param partyPanel The party panel to update the display order of the party
     * @return An action listener for the SelectPartyPopUp when switching a monster
     */
    private BiConsumer<ActionEvent, Monster> switchAction(JButton moveButton, JButton sellButton, JLabel errorLabel, JLabel monsterActionLabel, PartyPanel partyPanel) {
        return (ignoredEvent, mon) -> {
            try {
                gameManager.switchMonsterOnParty(chosenMonster.get(), trainer.getParty().indexOf(mon));
                errorLabel.setVisible(false);
                if (chosenMonster.get().getId() != mon.getId()) {
                    monsterActionLabel.setText(String.format("%s swapped with %s!",
                            chosenMonster.get().getName(), mon.getName()));
                    monsterActionLabel.setVisible(true);
                }
            } catch (IndexOutOfBoundsException err) {
                monsterActionLabel.setVisible(false);
                errorLabel.setVisible(true);
                errorLabel.setText("This isn't a valid position!");
            } finally {
                partyPanel.refresh();
                moveButton.setEnabled(true);
                sellButton.setEnabled(trainer.getParty().size() > 1);
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
