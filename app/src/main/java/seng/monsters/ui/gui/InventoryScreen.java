package seng.monsters.ui.gui;

import java.awt.Color;

import javax.swing.JFrame;

import seng.monsters.model.GameManager;
import seng.monsters.model.Inventory;
import seng.monsters.model.Item;
import seng.monsters.model.Trainer;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.util.List;

import javax.swing.JButton;

public class InventoryScreen implements Screen {
	
	private final List<Item> items = Item.all();
	
    /**
     * The GUI manager for navigation
     */
    private final GUI gui;

    /**
     * The game manager to perform actions onto the model and retrieve data
     */
    private final GameManager gameManager;

    /**
     * The window screen
     */
    private JFrame frame;

	/**
	 * Create the application.
	 */
	public InventoryScreen(GUI gui, GameManager gameManager) {
        this.gui = gui;
        this.gameManager = gameManager;
    }

	
	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void initialize() {
        frame = new JFrame();
        frame.getContentPane().setBackground(new Color(255, 255, 204));
        frame.getContentPane().setForeground(new Color(0, 0, 0));
        frame.setBounds(100, 100, 819, 487);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        JLabel errorLabel = new JLabel("No monster in party");
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setForeground(new Color(255, 0, 0));
        errorLabel.setBounds(6, 336, 807, 16);
        errorLabel.setVisible(false);
        frame.getContentPane().add(errorLabel);
        

        JButton backToMainMenu = new JButton();
        backToMainMenu.setText("Back to main menu");
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
            useButton.addActionListener(e -> {
            		final int count = gameManager.getInventory().getItemNumber(item);
            		
            		if (gameManager.getTrainer().getParty().size() <= 0 || count <= 0) {
            			errorLabel.setText(count <= 0 ? "There is no such item in your inventory" : "There is no monster to apply item to");
            			errorLabel.setVisible(true);
            			return;
            		}
            		
            		SelectPartyPopUp popUp = new SelectPartyPopUp(gameManager);
            		
            		popUp.onChosen((ignored, monster) -> {
            			try {
            				gameManager.useItemFromInventory(item, monster);
                    	final var newCount = gameManager.getInventory().getItemNumber(item);
                        countLabel.setText(String.format("%dx", newCount));
                        useButton.setEnabled(newCount > 0);
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
            			}
            		});
         
            });
            frame.getContentPane().add(useButton);
        	}
        	
        	backToMainMenu.addActionListener(e -> {
        		final var trainer = gameManager.getTrainer();
        		final var inventory = gameManager.getInventory();
        		
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
        	});
		
		frame.setVisible(true);
	}

	
	@Override
	public void dispose() {
		frame.dispose();
	}
}
