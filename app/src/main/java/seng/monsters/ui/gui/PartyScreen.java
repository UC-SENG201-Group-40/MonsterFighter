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
     * Create the application.
     */
    public PartyScreen(GUI gui, GameManager gameManager) {
        super(gui, gameManager);
        trainer = gameManager.getTrainer();
        chosenMonster = State.of(trainer.getParty().get(0));
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

        JButton backToMainMenu = new JButton("Main menu");
        backToMainMenu.setBounds(351, 398, 117, 29);
        frame.getContentPane().add(backToMainMenu);

        PartyPanel partyPanel = new PartyPanel(trainer);
        partyPanel.setBounds(476, 68);
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

        JLabel errorLabel = new JLabel();
        errorLabel.setBounds(66, 106 + DetailedMonsterPanel.HEIGHT, DetailedMonsterPanel.WIDTH, 20);
        errorLabel.setVisible(false);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setForeground(new Color(255, 0, 0));
        frame.getContentPane().add(errorLabel);

        chosenMonster.onChange(monsterPanel::refresh);

        partyPanel.onAction(slotAction());
        backToMainMenu.addActionListener(backToMainMenuAction());
        moveButton.addActionListener(moveAction(moveButton, sellButton, errorLabel, partyPanel));
        sellButton.addActionListener(sellAction(sellButton, errorLabel, partyPanel));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private BiConsumer<ActionEvent, Monster> slotAction() {
        return (e, mon) -> chosenMonster.set(mon);
    }

    private ActionListener sellAction(JButton sellButton, JLabel errorLabel, PartyPanel partyPanel) {
        return e -> {
            try {
                gameManager.sell(chosenMonster.get());
                errorLabel.setVisible(false);
            } catch (Trainer.MonsterDoesNotExistException err) {
                errorLabel.setVisible(true);
                errorLabel.setText("This monster isn't in your party");
            } catch (Inventory.ItemNotExistException err) {
                errorLabel.setVisible(true);
                errorLabel.setText("This item isn't in your invenotyr");
            } finally {
                partyPanel.refresh();
                sellButton.setEnabled(trainer.getParty().size() > 1);
                chosenMonster.set(trainer.getParty().get(0));
            }
        };
    }

    private ActionListener moveAction(JButton moveButton, JButton sellButton, JLabel errorLabel, PartyPanel partyPanel) {
        return e -> {
            SelectPartyPopUp popUp = new SelectPartyPopUp(gameManager);
            popUp.onChosen(switchAction(moveButton, sellButton, errorLabel, partyPanel));
            moveButton.setEnabled(false);
        };
    }

    private BiConsumer<ActionEvent, Monster> switchAction(JButton moveButton, JButton sellButton, JLabel errorLabel, PartyPanel partyPanel) {
        return (e, mon) -> {
            try {
                gameManager.switchMonsterOnParty(chosenMonster.get(), trainer.getParty().indexOf(mon));
                errorLabel.setVisible(false);
            } catch (IndexOutOfBoundsException err) {
                errorLabel.setVisible(true);
                errorLabel.setText("Cannot move monster to invalid position");
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
        return e -> gui.navigateBackToMainMenu();
    }
}
