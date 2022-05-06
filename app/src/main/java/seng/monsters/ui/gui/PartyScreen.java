package seng.monsters.ui.gui;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import seng.monsters.model.GameManager;
import seng.monsters.model.Inventory;
import seng.monsters.model.Monster;
import seng.monsters.model.Trainer;
import seng.monsters.ui.gui.components.DetailedMonsterPanel;
import seng.monsters.ui.gui.components.PartySlotPanel;
import seng.monsters.ui.gui.components.SelectPartyPopUp;
import seng.monsters.ui.gui.state.State;

public class PartyScreen extends Screen {
	
	private final Trainer trainer;
	
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
        JButton backToMainMenu = new JButton("Main menu");
        backToMainMenu.setBounds(351, 398, 117, 29);
        frame.getContentPane().add(backToMainMenu);
        
        	final var party = trainer.getParty();
        	final var baseX = 476;
        	final var baseY = 68;
        	final var diffY = 20;
        	
        	final var panelSlots = IntStream.range(0, party.size())
        		.mapToObj(i -> {
            		PartySlotPanel panel = new PartySlotPanel(party.get(i));
            		panel.setBounds(baseX, baseY + i * (PartySlotPanel.HEIGHT + diffY));
            		panel.applyToFrame(frame);       
            		panel.addActionListener(eachPanelAction(i));
            		return panel;
        		})
        		.toList();
        	
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
        	
        	backToMainMenu.addActionListener(backToMainMenuAction());
        	moveButton.addActionListener(moveAction(moveButton, sellButton, errorLabel, panelSlots));
        	sellButton.addActionListener(sellAction(sellButton, errorLabel, panelSlots));
        
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	private void redrawSlots(List<PartySlotPanel> panelSlots) {
		for (var i = 0; i < trainer.getParty().size(); i++) {
			final var newMon = trainer.getParty().get(i);
			panelSlots.get(i).refresh(newMon);
			panelSlots.get(i).setVisible(true);
		}
		for (var i = trainer.getParty().size(); i < panelSlots.size(); i++) {
			panelSlots.get(i).setVisible(false);
		}
	}
	
	private ActionListener eachPanelAction(int i) {
		return e -> chosenMonster.set(trainer.getParty().get(i));
	}
	
	private ActionListener sellAction(JButton sellButton, JLabel errorLabel, List<PartySlotPanel> panelSlots) {
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
    			redrawSlots(panelSlots);
    			sellButton.setEnabled(trainer.getParty().size() > 1);
    			chosenMonster.set(trainer.getParty().get(0));
    		}
    	};
	}
	
	private ActionListener moveAction(JButton moveButton, JButton sellButton, JLabel errorLabel, List<PartySlotPanel> panelSlots) {
		return e -> {
    		SelectPartyPopUp popUp = new SelectPartyPopUp(gameManager);
    		popUp.onChosen(switchAction(moveButton, sellButton, errorLabel, panelSlots));
    		moveButton.setEnabled(false);
    	};
	}
	
	private BiConsumer<ActionEvent, Monster> switchAction(JButton moveButton, JButton sellButton, JLabel errorLabel, List<PartySlotPanel> panelSlots) {
		return (e, mon) -> {
			try { 
				gameManager.switchMonsterOnParty(chosenMonster.get(), trainer.getParty().indexOf(mon));
				errorLabel.setVisible(false);
			} catch (IndexOutOfBoundsException err) {
				errorLabel.setVisible(true);
				errorLabel.setText("Cannot move monster to invalid position");
			} finally {
				redrawSlots(panelSlots);
				moveButton.setEnabled(true);
    			sellButton.setEnabled(trainer.getParty().size() > 1);
			}
		};
	}
	
    /**
     * The action performed when the user chose to return the main menu
     */
    private ActionListener backToMainMenuAction() {
        return e -> {
        		final var inventory = gameManager.getInventory();


            // TODO: Back to main screen
            System.out.printf("Name: %s\n", trainer.getName());

            System.out.println("-- Party --");
            trainer.getParty()
                .stream()
                .map(mon ->
                    String.format(
                        "%s (%s) lvl %d\nHP: %d/%d\nATK: %d\nHEAL:%d\nSPEED:%d\n",
                        mon.getName(),
                        mon.monsterType(),
                        mon.getLevel(),
                        mon.getCurrentHp(),
                        mon.maxHp(),
                        mon.scaledDamage(),
                        mon.healRate(),
                        mon.speed()
                    )
                )
                .map(str -> "=======\n" + str)
                .forEachOrdered(System.out::println);

            System.out.println("-- Inventory --");
            inventory.getItemEntries()
                .stream()
                .map(entry -> String.format("- %s (%dx)", entry.getKey().getName(), entry.getValue()))
                .forEachOrdered(System.out::println);

            gui.quit();
        };
    }
}
