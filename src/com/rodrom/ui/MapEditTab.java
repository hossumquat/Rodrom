package com.rodrom.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import com.rodrom.Input;
import com.rodrom.Map;
import com.rodrom.MapArray;
import com.rodrom.Symbols;

import net.miginfocom.swing.MigLayout;

public class MapEditTab extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static final int defaultMapStartingSize = 30;

	public MapViewPanel mapView;
	public int placeDoor = 0;
	public MapArray mapArray = new MapArray();

	public JFormattedTextField levelNameUI;
	public JLabel levelNameL;
	public JLabel levelNumL;
	public JCheckBox stairsUpCB;
	public JCheckBox stairsDownCB;
	public JCheckBox pitCB;
	public JCheckBox chuteCB;
	public JCheckBox teleporterCB;
	public JCheckBox rotatorCB;
	public JCheckBox antiMagicCB;
	public JCheckBox extinguisherCB;
	public JCheckBox studCB;
	public JCheckBox waterCB;
	public JCheckBox fogCB;
	public JCheckBox darkCB;
	public JCheckBox quicksandCB;
	public JRadioButton faceNorthRB;
	public JRadioButton faceEastRB;
	public JRadioButton faceSouthRB;
	public JRadioButton faceWestRB;

	public MapEditTab() {
		super(new BorderLayout());

		if (mapArray.size() == 0)
			mapArray.add(new Map(defaultMapStartingSize));

		mapView = new MapViewPanel(mapArray.get());
		mapView.editMode = true;
		mapView.editor = this;
		add(mapView, BorderLayout.CENTER);

		JPanel panel = new JPanel(new MigLayout());
		/*
		 * levelNameL = new JLabel("Untitled"); Font font = levelNameL.getFont(); font =
		 * font.deriveFont(Font.BOLD, font.getSize() + 2); levelNameL.setFont(font);
		 */

//		levelNameUI = new JFormattedTextField("Untitled");
		levelNameUI = new JFormattedTextField("main");
		panel.add(levelNameUI, "growx, span, wrap");

		levelNumL = new JLabel("Level");
		setLevelLabel();
		levelNumL.setFont(levelNumL.getFont().deriveFont(Font.BOLD));
		panel.add(levelNumL, "split, span");

		JButton button = Utility.iconButton("images/left arrow.png", "small");
		button.setActionCommand("prev.level");
		button.addActionListener(this);
		panel.add(button);

		button = Utility.iconButton("images/right arrow.png", "small");
		button.setActionCommand("next.level");
		button.addActionListener(this);
		panel.add(button);

		button = Utility.iconButton("images/inc.png", "small");
		button.setToolTipText("Add a new level");
		button.setActionCommand("add.level");
		button.addActionListener(this);
		panel.add(button);

		button = Utility.iconButton("images/delete.png", "small");
		button.setToolTipText("Delete this level");
		button.setActionCommand("delete.level");
		button.addActionListener(this);
		panel.add(button, "wrap");

		stairsUpCB = addCheckBox("Stairs Up", Symbols.STAIRS_UP, panel, "split, span, growx");
		stairsUpCB.setActionCommand("stairs.up");
		stairsUpCB.addActionListener(this);

		button = new JButton("Load");
		panel.add(button, "wrap");
		button.setActionCommand("load");
		button.addActionListener(this);

		stairsDownCB = addCheckBox("Stairs Down", Symbols.STAIRS_DOWN, panel, "split, span, growx");
		stairsDownCB.setActionCommand("stairs.down");
		stairsDownCB.addActionListener(this);

		button = new JButton("Save");
		panel.add(button, "wrap");
		button.setActionCommand("save");
		button.addActionListener(this);

		pitCB = addCheckBox("Pit", Symbols.PIT, panel);
		pitCB.setActionCommand("pit");
		pitCB.addActionListener(this);

		chuteCB = addCheckBox("Chute", Symbols.CHUTE, panel);
		chuteCB.setActionCommand("chute");
		chuteCB.addActionListener(this);

		teleporterCB = addCheckBox("Teleporter", Symbols.TELEPORTER, panel);
		teleporterCB.setActionCommand("teleporter");
		teleporterCB.addActionListener(this);

		rotatorCB = addCheckBox("Rotator", Symbols.ROTATOR, panel);
		rotatorCB.setActionCommand("rotator");
		rotatorCB.addActionListener(this);

		antiMagicCB = addCheckBox("Anti Magic", Symbols.ANTIMAGIC, panel);
		antiMagicCB.setActionCommand("antimagic");
		antiMagicCB.addActionListener(this);

		extinguisherCB = addCheckBox("Extinguisher", Symbols.EXTINGUISHER, panel);
		extinguisherCB.setActionCommand("extinguisher");
		extinguisherCB.addActionListener(this);

		studCB = addCheckBox("Stud", Symbols.STUD, panel);
		studCB.setActionCommand("stud");
		studCB.addActionListener(this);

		waterCB = addCheckBox("Water", Symbols.WATER, panel);
		waterCB.setActionCommand("water");
		waterCB.addActionListener(this);

		fogCB = addCheckBox("Fog", Symbols.FOG, panel);
		fogCB.setActionCommand("fog");
		fogCB.addActionListener(this);

		darkCB = addCheckBox("Darkness", Symbols.DARK, panel);
		darkCB.setActionCommand("dark");
		darkCB.addActionListener(this);

		quicksandCB = addCheckBox("Quicksand", Symbols.QUICKSAND, panel);
		quicksandCB.setActionCommand("quicksand");
		quicksandCB.addActionListener(this);

		faceNorthRB = addRadioButton("Face North", Symbols.FACE_NORTH, panel);
		faceNorthRB.setActionCommand("face.north");
		faceNorthRB.addActionListener(this);

		faceEastRB = addRadioButton("Face East", Symbols.FACE_EAST, panel);
		faceEastRB.setActionCommand("face.east");
		faceEastRB.addActionListener(this);

		faceSouthRB = addRadioButton("Face South", Symbols.FACE_SOUTH, panel);
		faceSouthRB.setActionCommand("face.south");
		faceSouthRB.addActionListener(this);

		faceWestRB = addRadioButton("Face West", Symbols.FACE_WEST, panel);
		faceWestRB.setActionCommand("face.west");
		faceWestRB.addActionListener(this);

		JScrollPane scroll = new JScrollPane(panel) {
			private static final long serialVersionUID = 1L;

			public Dimension getPreferredSize() {
				Dimension dim = super.getPreferredSize();
				JScrollBar bar = getVerticalScrollBar();
				if (bar.isShowing())
					dim.width += bar.getWidth();

				return dim;
			}
		};
		add(scroll, BorderLayout.EAST);

		Input.addEditActions(mapView, this);
	}

	public void update() {
		int cell = mapView.map.get();

		stairsUpCB.setSelected((cell & Map.STAIRS_UP) != 0);
		stairsDownCB.setSelected((cell & Map.STAIRS_DOWN) != 0);
		pitCB.setSelected((cell & Map.PIT) != 0);
		chuteCB.setSelected((cell & Map.CHUTE) != 0);
		teleporterCB.setSelected((cell & Map.TELEPORTER) != 0);
		rotatorCB.setSelected((cell & Map.ROTATOR) != 0);
		antiMagicCB.setSelected((cell & Map.ANTIMAGIC) != 0);
		extinguisherCB.setSelected((cell & Map.EXTINGUISHER) != 0);
		studCB.setSelected((cell & Map.STUD) != 0);
		waterCB.setSelected((cell & Map.WATER) != 0);
		fogCB.setSelected((cell & Map.FOG) != 0);
		darkCB.setSelected((cell & Map.DARK) != 0);
		quicksandCB.setSelected((cell & Map.QUICKSAND) != 0);

		faceNorthRB.setSelected(false);
		faceEastRB.setSelected(false);
		faceSouthRB.setSelected(false);
		faceWestRB.setSelected(false);

		if ((cell & Map.FACE_DIR) != 0) {
			int value = cell & Map.FACE_MASK;

			if (value == Map.FACE_NORTH)
				faceNorthRB.setSelected(true);

			else if (value == Map.FACE_EAST)
				faceEastRB.setSelected(true);

			else if (value == Map.FACE_SOUTH)
				faceSouthRB.setSelected(true);

			else if (value == Map.FACE_WEST)
				faceWestRB.setSelected(true);
		}
	}

	public void setLevelLabel() {
		levelNumL.setText("Level: " + (mapArray.level + 1) + " / " + mapArray.size());
	}

	public JCheckBox addCheckBox(String text, int pict, JComponent parent) {
		return addCheckBox(text, pict, parent, "wrap");
	}

	public JCheckBox addCheckBox(String text, int pict, JComponent parent, String constraints) {
		ImageIcon icon = new ImageIcon(mapView.subImage(pict));
		parent.add(new JLabel(icon));

		JCheckBox checkBox = new JCheckBox(text);
		checkBox.setFocusable(false);
		parent.add(checkBox, constraints);

		return checkBox;
	}

	public JRadioButton addRadioButton(String text, int pict, JComponent parent) {
		ImageIcon icon = new ImageIcon(mapView.subImage(pict));
		parent.add(new JLabel(icon));

		JRadioButton radioButton = new JRadioButton(text);
		radioButton.setFocusable(false);
		parent.add(radioButton, "wrap");

		return radioButton;
	}

	public void carveVertical(int x, int y) {
		Map map = mapView.map;

		if (x < 0 || x >= map.width)
			return;

		if (y < -1 || y >= map.height)
			return;

		if (y == -1) {
			map.tiles[x] &= ~Map.ROCK;
			return;
		}

		if (y == map.height - 1) {
			map.tiles[x + y * map.width] &= ~Map.ROCK;
			return;
		}

		map.tiles[x + y * map.width] &= ~Map.ROCK;
		y++;
		map.tiles[x + y * map.width] &= ~(Map.ROCK | Map.NORTH_WALL);
	}

	public void carveHorizontal(int x, int y) {
		Map map = mapView.map;

		if (y < 0 || y >= map.height)
			return;

		if (x < -1 || x >= map.width)
			return;

		if (x == -1) {
			map.tiles[y * map.width] &= ~Map.ROCK;
			return;
		}

		if (x == map.width - 1) {
			map.tiles[x + y * map.width] &= ~Map.ROCK;
			return;
		}

		map.tiles[x + y * map.width] &= ~Map.ROCK;
		x++;
		map.tiles[x + y * map.width] &= ~(Map.ROCK | Map.WEST_WALL);
	}

	public void editCell() {
		Map map = mapView.map;
		int x = map.xPos;
		int y = map.yPos;

		if (x < 0 || x >= map.width || y < 0 || y >= map.height)
			return;

		int index = x + y * map.width;
		if ((map.tiles[index] & Map.ROCK) != 0) {
			map.tiles[index] &= ~Map.ROCK;
			mapView.repaint();
			return;
		}

		// don't allow filling with rock if their are stairs here
		if ((map.tiles[index] & (Map.STAIRS_UP | Map.STAIRS_DOWN)) != 0)
			return;

		map.tiles[index] = Map.ROCK;
		map.setWall(x, y, Map.WALL, Map.NORTH_WALL);
		map.setWall(x, y, Map.WALL, Map.WEST_WALL);

		if (x < map.width)
			map.setWall(x + 1, y, Map.WALL, Map.WEST_WALL);

		if (y < map.height)
			map.setWall(x, y + 1, Map.WALL, Map.NORTH_WALL);

		mapView.repaint();
	}

	public void upAction(boolean carve) {
		Map map = mapView.map;
		if (placeDoor > 0) {
			map.setWall(map.xPos, map.yPos, placeDoor, Map.NORTH_WALL);
			placeDoor = 0;
			mapView.repaint();
			return;
		}

		if (carve)
			carveVertical(map.xPos, map.yPos - 1);

		map.yPos--;
		mapView.repaint();
		update();
	}

	public void downAction(boolean carve) {
		Map map = mapView.map;
		if (placeDoor > 0) {
			map.setWall(map.xPos, map.yPos + 1, placeDoor, Map.NORTH_WALL);
			placeDoor = 0;
			mapView.repaint();
			return;
		}

		if (carve)
			carveVertical(map.xPos, map.yPos);

		map.yPos++;
		mapView.repaint();
		update();
	}

	public void leftAction(boolean carve) {
		Map map = mapView.map;
		if (placeDoor > 0) {
			map.setWall(map.xPos, map.yPos, placeDoor, Map.WEST_WALL);
			placeDoor = 0;
			mapView.repaint();
			return;
		}

		if (carve)
			carveHorizontal(map.xPos - 1, map.yPos);

		map.xPos--;
		mapView.repaint();
		update();
	}

	public void rightAction(boolean carve) {
		Map map = mapView.map;
		if (placeDoor > 0) {
			map.setWall(map.xPos + 1, map.yPos, placeDoor, Map.WEST_WALL);
			placeDoor = 0;
			mapView.repaint();
			return;
		}

		if (carve)
			carveHorizontal(map.xPos, map.yPos);

		map.xPos++;
		mapView.repaint();
		update();
	}

	public void editWall() {
		int v = Map.WALL;

		if (v == placeDoor)
			placeDoor = 0;
		else
			placeDoor = v;
	}

	public void editDoor(boolean hidden) {
		int v = hidden ? Map.HIDDEN : Map.DOOR;

		if (v == placeDoor)
			placeDoor = 0;
		else
			placeDoor = v;
	}

	public void modifyCell(int mask, boolean state) {
		mapView.map.set(mask, state);
	}

	public void modifyFace(boolean state, int value) {
		if (state)
			mapView.map.set(Map.FACE_DIR | Map.FACE_MASK, Map.FACE_DIR | value);
		else
			mapView.map.set(Map.FACE_DIR, false);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		boolean shift = (event.getModifiers() & ActionEvent.SHIFT_MASK) != 0;

		if (event.getActionCommand().equals("prev.level")) {
			if (mapArray.level > 0) {
				if (shift)
					mapArray.level = 0;
				else
					mapArray.level--;

				Map map = mapArray.get();
				map.setPosFrom(mapView.map);
				mapView.map = map;

				repaint();
				update();
				setLevelLabel();
			}

		} else if (event.getActionCommand().equals("next.level")) {
			if (mapArray.level < mapArray.size() - 1) {
				if (shift)
					mapArray.level = mapArray.size() - 1;
				else
					mapArray.level++;

				Map map = mapArray.get();
				map.setPosFrom(mapView.map);
				mapView.map = map;

				repaint();
				update();
				setLevelLabel();
			}

		} else if (event.getActionCommand().equals("add.level")) {
			Map map = mapArray.get();
			if (!shift)
				mapArray.level++;

			mapArray.add(new Map(map.width, map.height));
			map = mapArray.get();
			map.setPosFrom(mapView.map);
			mapView.map = map;

			repaint();
			update();
			setLevelLabel();

		} else if (event.getActionCommand().equals("delete.level")) {
			Object message = "Are you sure you want to delete this level?";
			if (shift) {
				Icon icon = UIManager.getIcon("OptionPane.warningIcon");
				String text = "Are you sure you want to delete all levels and start over from scratch?";
				message = new JLabel(text, icon, JLabel.LEFT);
			}

			int r = JOptionPane.showConfirmDialog(null, message, "Confirm", JOptionPane.YES_NO_OPTION);
			if (r == JOptionPane.YES_OPTION) {
				if (shift) {
					mapArray.removeAll();

				} else {
					mapArray.remove();
				}

				if (mapArray.size() == 0)
					mapArray.add(new Map(defaultMapStartingSize));

				mapView.map = mapArray.get();
				repaint();
				update();
				setLevelLabel();
			}

		} else if (event.getActionCommand().equals("load")) {
			MapArray temp = new MapArray();
			if (temp.load(levelNameUI.getText() + ".map")) {
				if (mapArray.level < temp.size()) {
					temp.level = mapArray.level;
					Map map = temp.get();
					map.xPos = mapView.map.xPos;
					map.yPos = mapView.map.yPos;
				}

				mapArray = temp;
				mapView.map = mapArray.get();

				repaint();
				update();
				setLevelLabel();
			}

		} else if (event.getActionCommand().equals("save")) {
			mapArray.name = levelNameUI.getText() + ".map";
			mapArray.save();

			repaint();
			update();
			setLevelLabel();

		} else if (event.getActionCommand().equals("stairs.up")) {
			modifyCell(Map.STAIRS_UP, stairsUpCB.isSelected());
			update();

		} else if (event.getActionCommand().equals("stairs.down")) {
			modifyCell(Map.STAIRS_DOWN, stairsDownCB.isSelected());
			update();

		} else if (event.getActionCommand().equals("pit")) {
			modifyCell(Map.PIT, pitCB.isSelected());
			update();

		} else if (event.getActionCommand().equals("chute")) {
			modifyCell(Map.CHUTE, chuteCB.isSelected());
			update();

		} else if (event.getActionCommand().equals("teleporter")) {
			modifyCell(Map.TELEPORTER, teleporterCB.isSelected());
			update();

		} else if (event.getActionCommand().equals("rotator")) {
			modifyCell(Map.ROTATOR, rotatorCB.isSelected());
			update();

		} else if (event.getActionCommand().equals("antimagic")) {
			modifyCell(Map.ANTIMAGIC, antiMagicCB.isSelected());
			update();

		} else if (event.getActionCommand().equals("extinguisher")) {
			modifyCell(Map.EXTINGUISHER, extinguisherCB.isSelected());
			update();

		} else if (event.getActionCommand().equals("stud")) {
			modifyCell(Map.STUD, studCB.isSelected());
			update();

		} else if (event.getActionCommand().equals("water")) {
			modifyCell(Map.WATER, waterCB.isSelected());
			update();

		} else if (event.getActionCommand().equals("fog")) {
			modifyCell(Map.FOG, fogCB.isSelected());
			update();

		} else if (event.getActionCommand().equals("dark")) {
			modifyCell(Map.DARK, darkCB.isSelected());
			update();

		} else if (event.getActionCommand().equals("quicksand")) {
			modifyCell(Map.QUICKSAND, quicksandCB.isSelected());
			update();

		} else if (event.getActionCommand().equals("face.north")) {
			modifyFace(faceNorthRB.isSelected(), Map.FACE_NORTH);
			update();

		} else if (event.getActionCommand().equals("face.east")) {
			modifyFace(faceEastRB.isSelected(), Map.FACE_EAST);
			update();

		} else if (event.getActionCommand().equals("face.south")) {
			modifyFace(faceSouthRB.isSelected(), Map.FACE_SOUTH);
			update();

		} else if (event.getActionCommand().equals("face.west")) {
			modifyFace(faceWestRB.isSelected(), Map.FACE_WEST);
			update();
		}
	}
}
