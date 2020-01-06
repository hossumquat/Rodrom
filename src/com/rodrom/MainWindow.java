package com.rodrom;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.rodrom.ui.EditorPanel;
import com.rodrom.ui.GameLayoutPanel;

public class MainWindow {
	final public static String TITLE = "Rodrom: The Heights of Lonejed";
	final public static String MAIN_MENU_CARD = "Main Menu";
	final public static String GAME_LAYOUT_CARD = "Game";
	final public static String EDITOR_CARD = "Editor";

	final public static int WIDTH = 1280; //1028;
	final public static int HEIGHT = 720; //768;

	final public static Color backgroundColor = new Color(0xece9d8);
	final public static Color shadowColor = new Color(0xd6d3c5);

	public static JFrame frame;
	public static JPanel cards;
	public static MainMenu mainMenuPanel;
	public static GameLayoutPanel gameLayoutPanel;
	public static EditorPanel editorPanel;
	public static CardLayout cardLayout;

	public MainWindow() {
		UIManager.put("control", backgroundColor);
		UIManager.getDefaults().put("SplitPane.border", BorderFactory.createEmptyBorder());
		try {
			String name = Game.lookAndFeel;
			if (name == null)
				name = UIManager.getSystemLookAndFeelClassName();

			UIManager.setLookAndFeel(name);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		frame = new JFrame(TITLE);
		frame.setMinimumSize(new Dimension(780, 340));
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));

		mainMenuPanel = new MainMenu();
		gameLayoutPanel = new GameLayoutPanel();
		editorPanel = new EditorPanel();

		cardLayout = new CardLayout();
		cards = new JPanel(cardLayout);
		Input.addCardActions(cards);

		cards.add(mainMenuPanel, MAIN_MENU_CARD);
		cards.add(gameLayoutPanel, GAME_LAYOUT_CARD);
		cards.add(editorPanel, EDITOR_CARD);

		cardLayout.show(cards, GAME_LAYOUT_CARD);
//		cardLayout.show(cards, EDITOR_CARD);

		frame.add(cards, BorderLayout.CENTER);

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		SwingUtilities.updateComponentTreeUI(frame);
	}

	public static void gameMode() {
		cardLayout.show(cards, GAME_LAYOUT_CARD);
	}

	public static void editMode() {
		cardLayout.show(cards, EDITOR_CARD);
	}

	void init() {
	}
}
