package seng.monsters.ui.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.function.BiConsumer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import seng.monsters.model.GameManager;
import seng.monsters.model.Monster;

public final class SelectPartyPopUp {
	private final List<Monster> party;
	
	private JFrame frame;
	private BiConsumer<ActionEvent, Monster> onChosen = (e, m) -> {
	};
	
	private final State<Monster> selectedMonster;
	
	public SelectPartyPopUp(GameManager gameManager) {
		this.party = gameManager.getTrainer().getParty();
		this.selectedMonster = State.of(party.get(0));
		initialize();
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(255, 255, 204));
		frame.getContentPane().setForeground(new Color(0, 0, 0));
		frame.setBounds(100, 100, 819, 487);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JComboBox<String> partyComboBox = new JComboBox<>();
		partyComboBox.setModel(
			new DefaultComboBoxModel<>(party
				.stream()
				.map(Monster::getName)
				.toList()
				.toArray(new String[party.size()])
			)
		);
		partyComboBox.setSelectedIndex(0);
		partyComboBox.setBounds(66, 140, 238, 27);
		frame.getContentPane().add(partyComboBox);

		JLabel monsterPrompt = new JLabel("Choose your monster:");
		monsterPrompt.setBounds(66, 112, 238, 16);
		frame.getContentPane().add(monsterPrompt);

		DetailedMonsterPanel panel = new DetailedMonsterPanel(selectedMonster.get(), false);
		panel.setBounds(439, 90);
		panel.applyToFrame(frame);

		JButton submitButton = new JButton("Next");
		submitButton.setBounds(350, 374, 117, 29);
		frame.getContentPane().add(submitButton);

		// Setting the on change callback for the selected monster
		selectedMonster.onChange(panel::refresh);

		partyComboBox.addActionListener(e -> {
			if (partyComboBox.getSelectedIndex() < 0)
				return;
			selectedMonster.set(party.get(partyComboBox.getSelectedIndex()));
		});

		submitButton.addActionListener(e -> {
			final var monster = selectedMonster.get();
			partyComboBox.setEnabled(false);

			onChosen.accept(e, monster);
			frame.dispose();
		});

		frame.setVisible(true);
	}
	
	public void onChosen(BiConsumer<ActionEvent, Monster> function) {
		onChosen = function;
	}
}
