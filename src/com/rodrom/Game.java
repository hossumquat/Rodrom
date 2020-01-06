package com.rodrom;

import java.awt.SplashScreen;
import java.util.Random;

import com.rodrom.dll.LoadIcons;
import com.rodrom.ui.CreateCharacter;
import com.rodrom.ui.CreaturesViewPanel;
import com.rodrom.ui.DungeonViewPanel;
import com.rodrom.ui.MapViewPanel;
import com.rodrom.ui.MessageText;

public class Game implements Runnable {
	public static final int DAYS_PER_YEAR = 365;
	public static final int MINIMUM_AGE = 16 * DAYS_PER_YEAR;
	public static MainWindow mainWindow;
	public static DungeonViewPanel dungeonView;
	public static CreaturesViewPanel creaturesView;
	public static MapViewPanel mapView;

	public static Game game;
	public static Map map, masterMap;
	public static MapArray mapArray;
	public static Random rng;
	public static MessageText messageLog;
	public static MessageText combatLog;

	public static String lookAndFeel;
	
	public static boolean inCombat = false;

	int backgroundColor = 0xece9d8;
	final private SplashScreen splash = SplashScreen.getSplashScreen();

	public Game() {
		rng = new Random();

		javax.swing.SwingUtilities.invokeLater(this);
	}

	@Override
	public void run() {
		LoadSave.loadConfig();
		LoadSave.loadMaster();
		LoadIcons.load();

		mapArray = new MapArray();
		mapArray.load("main.map");
		masterMap = mapArray.get();

		masterMap.findStart();
		map = new Map();
		map.derive(masterMap);
		masterMap.explore(map);

		mainWindow = new MainWindow();

		messageLog.addMessage("Welcome to Rodrom v0.1");
		combatLog.addMessage("Combat log is ready for messages");

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				LoadSave.saveConfig();
			}
		}));
	}

	public static void upAction() {
		if (map.facing == 0) {
			if (map.isNorthWall()) {
				// play oomph sound here
			} else {
				map.yPos--;
			}

		} else if (map.facing == 1) {
			if (map.isEastWall()) {
				// play oomph sound here
			} else {
				map.xPos++;
			}

		} else if (map.facing == 2) {
			if (map.isSouthWall()) {
				// play oomph sound here
			} else {
				map.yPos++;
			}

		} else if (map.facing == 3) {
			if (map.isWestWall()) {
				// play oomph sound here
			} else {
				map.xPos--;
			}
		}

		masterMap.explore(map);
		updateViews();
	}

	public static void downAction() {
		map.facing ^= 2;
		masterMap.explore(map);
		updateViews();
	}

	public static void leftAction() {
		map.facing = (map.facing - 1) & 3;
		masterMap.explore(map);
		updateViews();
	}

	public static void rightAction() {
		map.facing = (map.facing + 1) & 3;
		masterMap.explore(map);
		updateViews();
	}

	public static void shiftUpAction() {
		DungeonView.texFlag = !DungeonView.texFlag;
		if (map.facing == 0) {
			map.setExplored(map.xPos, map.yPos - 1, masterMap);
		} else if (map.facing == 1) {
			map.setExplored(map.xPos + 1, map.yPos, masterMap);
		} else if (map.facing == 2) {
			map.setExplored(map.xPos, map.yPos + 1, masterMap);
		} else {
			map.setExplored(map.xPos - 1, map.yPos, masterMap);
		}

		updateViews();
	}

	public static void shiftDownAction() {
		DungeonView.litFlag = !DungeonView.litFlag;
		updateViews();
	}

	public static void shiftLeftAction() {
		map.orient--;
		if (map.orient < 0)
			map.orient = 3;

		updateViews();
	}

	public static void shiftRightAction() {
		map.orient++;
		if (map.orient > 3)
			map.orient = 0;

		updateViews();
	}

	public static void testAction() {
		CreateCharacter dialog = new CreateCharacter();
		
		dialog.pack();
		dialog.setLocationRelativeTo(MainWindow.frame);
		dialog.setVisible(true);
	}

	public static void changeFog() {
		MapViewPanel.FOU++;
		if (MapViewPanel.FOU > 2)
			MapViewPanel.FOU = 0;
	}

	public static void updateViews() {
		mapView.repaint();
		dungeonView.repaint();
		creaturesView.repaint();
	}
}
