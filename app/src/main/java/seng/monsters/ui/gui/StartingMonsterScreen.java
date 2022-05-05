package seng.monsters.ui.gui;

import seng.monsters.model.GameManager;
import seng.monsters.model.Monster;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A screen as part of the setup process to prompt the user with a selection of monster to start with
 */
public class StartingMonsterScreen implements Screen {

	/**
	 * The options of monsters available
	 */
	private final List<Monster> startingMonsters = Monster.all(1).subList(0, 3);

	/**
	 * The GUi for navigation
	 */
	private final GUI gui;

	/**
	 * The game manager as the model
	 */
	private final GameManager gameManager;

	/**
	 * The window frame
	 */
	private JFrame frame;

	/**
	 * The selected monster state
	 */
	private final State<Monster> selectedMonster = State.of(startingMonsters.get(0));

	/**
	 * Create the application.
	 */
	public StartingMonsterScreen(GUI gui, GameManager gameManager) {
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

		JComboBox<String> startingMonsterComboBox = new JComboBox<>();
		startingMonsterComboBox.setModel(
			new DefaultComboBoxModel<>(startingMonsters
				.stream()
				.map(Monster::monsterType)
				.toList()
				.toArray(new String[startingMonsters.size()])
			)
		);
		startingMonsterComboBox.setSelectedIndex(0);
		startingMonsterComboBox.setBounds(66, 140, 238, 27);
		frame.getContentPane().add(startingMonsterComboBox);

		JLabel startingMonsterPrompt = new JLabel("Choose your starting monster:");
		startingMonsterPrompt.setBounds(66, 112, 238, 16);
		frame.getContentPane().add(startingMonsterPrompt);

		DetailedMonsterPanel panel = new DetailedMonsterPanel(selectedMonster.get(), false);
		panel.setBounds(439, 90);
		panel.applyToFrame(frame);

		JButton submitButton = new JButton("Next");
		submitButton.setBounds(350, 374, 117, 29);
		frame.getContentPane().add(submitButton);

		// Setting the on change callback for the selected monster
		selectedMonster.onChange(panel::refresh);

		startingMonsterComboBox.addActionListener(e -> {
			if (startingMonsterComboBox.getSelectedIndex() < 0)
				return;
			selectedMonster.set(startingMonsters.get(startingMonsterComboBox.getSelectedIndex()));
		});

		submitButton.addActionListener(e -> {
			final var monster = selectedMonster.get();
			startingMonsterComboBox.setEnabled(false);

			monster.takeDamage(monster.maxHp());
			final var popUp = new MonsterJoiningPopUp(monster);
			popUp.onEnd(ev -> {
				gameManager.getTrainer().add(monster);

				gui.navigateTo(new InventoryScreen(gui, gameManager));
			});
		});

		frame.setVisible(true);
	}

	@Override
	public void dispose() {
		frame.dispose();
	}
}
