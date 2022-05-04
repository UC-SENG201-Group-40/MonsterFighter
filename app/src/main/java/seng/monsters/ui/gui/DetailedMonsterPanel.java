package seng.monsters.ui.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import seng.monsters.model.Monster;

public final class DetailedMonsterPanel {
	private JPanel monsterDisplayPanel;
	private JLabel monsterNameLabel;
	private JLabel levelLabel;
	private JLabel monsterCurrHpLabel;
	private JLabel priceLabel;
	private JLabel attackLabel;
	private JLabel speedLabel;
	private JLabel healRateLabel;
	private JLabel envLabel;
	private JLabel iconLabel;
	
	private Monster monster;
	private final boolean showBuying;

	
	public DetailedMonsterPanel(Monster monster) {
		this.monster = monster;
		this.showBuying = true;
		init();
	}
	
	public DetailedMonsterPanel(Monster monster, boolean showBuying) {
		this.monster = monster;
		this.showBuying = showBuying;
		init();
	}
	
	public void setBounds(int x, int y, int width, int height) {
		monsterDisplayPanel.setBounds(x, y, width, height);
		monsterDisplayPanel.setLayout(null);
	}
	
	public void applyToFrame(JFrame frame) {
		frame.getContentPane().add(monsterDisplayPanel);
	}
	
	public void refresh(Monster monster) {
		this.monster = monster;
		refresh();
	}
	
	public void refresh() {
		monsterNameLabel.setText(
			String.format("%s (%s)", monster.getName(), monster.monsterType())
		);
		
		levelLabel.setText(
			String.format("Lv. %d", monster.getLevel())
		);
		
		monsterCurrHpLabel.setText(
			String.format("%d/%d", monster.getCurrentHp(), monster.maxHp())
		);
		
		priceLabel.setText(
			String.format("%s price: %d Gold", 
				showBuying ? "Buy" : "Sell",
				showBuying ? monster.buyPrice() : monster.sellPrice()
			)
		);
		
		attackLabel.setText(
			String.format("Attack: %d", monster.scaledDamage())
		);
			
		speedLabel.setText(
			String.format("Speed: %d", monster.speed())
		);
		
		healRateLabel.setText(
			String.format("Heal rate: %d", monster.healRate())
		);
		
		envLabel.setText(
			String.format("Environent: %s", monster.idealEnvironment().toString())
		);
		
		iconLabel.setIcon(new ImageIcon(
			this.getClass().getResource(
				String.format("/images/%s.gif", monster.monsterType().toLowerCase())
			)
		));
	}
	
	private void init() {
		monsterDisplayPanel = new JPanel();
		monsterDisplayPanel.setBackground(new Color(255, 250, 240));
		
		monsterNameLabel = new JLabel(
			String.format("%s (%s)", monster.getName(), monster.monsterType())
		);
		monsterNameLabel.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		monsterNameLabel.setForeground(Color.BLACK);
		monsterNameLabel.setBounds(6, 6, 240, 36);
		monsterDisplayPanel.add(monsterNameLabel);
		
		JLabel hpLabel = new JLabel("HP:");
		hpLabel.setForeground(Color.BLACK);
		hpLabel.setBounds(16, 54, 25, 16);
		monsterDisplayPanel.add(hpLabel);
		
		levelLabel = new JLabel(
			String.format("Lv. %d", monster.getLevel())
		);
		levelLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		levelLabel.setForeground(Color.BLACK);
		levelLabel.setFont(new Font("Lucida Grande", Font.BOLD, 12));
		levelLabel.setBounds(249, 6, 45, 36);
		monsterDisplayPanel.add(levelLabel);
		
		monsterCurrHpLabel = new JLabel(
			String.format("%d/%d", monster.getCurrentHp(), monster.maxHp())
		);
		monsterCurrHpLabel.setForeground(hpColor());
		monsterCurrHpLabel.setBounds(44, 54, 99, 16);
		monsterDisplayPanel.add(monsterCurrHpLabel);
		
		priceLabel = new JLabel(
			String.format("%s price: %d Gold", 
				showBuying ? "Buy" : "Sell",
				showBuying ? monster.buyPrice() : monster.sellPrice()
			)
		);
		priceLabel.setForeground(Color.BLACK);
		priceLabel.setBounds(16, 82, 127, 16);
		monsterDisplayPanel.add(priceLabel);
		
		attackLabel = new JLabel(
			String.format("Attack: %d", monster.scaledDamage())
		);
		attackLabel.setForeground(Color.BLACK);
		attackLabel.setBounds(16, 110, 127, 16);
		monsterDisplayPanel.add(attackLabel);
		
		speedLabel = new JLabel(
			String.format("Speed: %d", monster.speed())
		);
		speedLabel.setForeground(Color.BLACK);
		speedLabel.setBounds(16, 138, 127, 16);
		monsterDisplayPanel.add(speedLabel);
		
		healRateLabel = new JLabel(
			String.format("Heal rate: %d", monster.healRate())
		);
		healRateLabel.setForeground(Color.BLACK);
		healRateLabel.setBounds(16, 166, 127, 16);
		monsterDisplayPanel.add(healRateLabel);
		
		envLabel = new JLabel(
			String.format("Environent: %s", monster.idealEnvironment().toString())
		);
		envLabel.setForeground(Color.BLACK);
		envLabel.setBounds(16, 194, 127, 16);
		monsterDisplayPanel.add(envLabel);
		
		iconLabel = new JLabel("");
		iconLabel.setIcon(new ImageIcon(
			this.getClass().getResource(
				String.format("/images/%s.gif", monster.monsterType().toLowerCase())
			)
		));
		iconLabel.setBounds(148, 54, 146, 156);
		monsterDisplayPanel.add(iconLabel);
	}
	
	private Color hpColor() {
		final var percentage = monster.getCurrentHp() * 100 / monster.maxHp();
		if (percentage < 25)
			return new Color(112, 0, 0);
		if (percentage < 50)
			return new Color(196, 120, 0);
		return new Color(46, 139, 87);
	}
}
