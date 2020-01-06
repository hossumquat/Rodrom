package com.rodrom;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.rodrom.ui.MapEditTab;

public class Input extends AbstractAction {
	private static final long serialVersionUID = 1L;

	public static final String GAME_MODE = "game.mode";
	public static final String EDIT_MODE = "edit.mode";

	public static final String UP = "up";
	public static final String DOWN = "down";
	public static final String LEFT = "left";
	public static final String RIGHT = "right";
	public static final String SHIFT_UP = "shift.up";
	public static final String SHIFT_DOWN = "shift.down";
	public static final String SHIFT_LEFT = "shift.left";
	public static final String SHIFT_RIGHT = "shift.right";
	
	public static final String EDIT_UP = "edit.up";
	public static final String EDIT_DOWN = "edit.down";
	public static final String EDIT_LEFT = "edit.left";
	public static final String EDIT_RIGHT = "edit.right";
	
	public static final String EDIT_CARVE_UP = "edit.carve.up";
	public static final String EDIT_CARVE_DOWN = "edit.carve.down";
	public static final String EDIT_CARVE_LEFT = "edit.carve.left";
	public static final String EDIT_CARVE_RIGHT = "edit.carve.right";
	public static final String EDIT_THIS_CELL = "edit.this.cell";
	public static final String EDIT_WALL = "edit.wall";
	public static final String EDIT_DOOR = "edit.door";
	public static final String EDIT_HIDDEN_DOOR = "edit.hidden.door";
	
	public static final String TEST = "test";
	public static final String FOG = "fog";
	
	public static MapEditTab editHandler;
	public String action;
	
	public Input(String action) {
		this.action = action;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (action == GAME_MODE)
			MainWindow.gameMode();
		else if (action == EDIT_MODE)
			MainWindow.editMode();

		else if (action == UP)
			Game.upAction();
		else if (action == DOWN)
			Game.downAction();
		else if (action == LEFT)
			Game.leftAction();
		else if (action == RIGHT)
			Game.rightAction();
		else if (action == SHIFT_UP)
			Game.shiftUpAction();
		else if (action == SHIFT_DOWN)
			Game.shiftDownAction();
		else if (action == SHIFT_LEFT)
			Game.shiftLeftAction();
		else if (action == SHIFT_RIGHT)
			Game.shiftRightAction();
		else if (action == TEST)
			Game.testAction();
		else if (action == FOG)
			Game.changeFog();

		else if (action == EDIT_UP)
			editHandler.upAction(false);
		else if (action == EDIT_DOWN)
			editHandler.downAction(false);
		else if (action == EDIT_LEFT)
			editHandler.leftAction(false);
		else if (action == EDIT_RIGHT)
			editHandler.rightAction(false);

		else if (action == EDIT_CARVE_UP)
			editHandler.upAction(true);
		else if (action == EDIT_CARVE_DOWN)
			editHandler.downAction(true);
		else if (action == EDIT_CARVE_LEFT)
			editHandler.leftAction(true);
		else if (action == EDIT_CARVE_RIGHT)
			editHandler.rightAction(true);
		
		else if (action == EDIT_THIS_CELL)
			editHandler.editCell();
		else if (action == EDIT_WALL)
			editHandler.editWall();
		else if (action == EDIT_DOOR)
			editHandler.editDoor(false);
		else if (action == EDIT_HIDDEN_DOOR)
			editHandler.editDoor(true);
	}

	// Top level card switching actions
	public static void addCardActions(JComponent component) {
		InputMap im = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0, false), GAME_MODE);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0, false), EDIT_MODE);

        ActionMap am = component.getActionMap();
        am.put(GAME_MODE, new Input(GAME_MODE));
        am.put(EDIT_MODE, new Input(EDIT_MODE));
	}

	public static void addGameActions(JComponent component) {
		InputMap im = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), UP);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), DOWN);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), LEFT);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), RIGHT);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_DOWN_MASK, false), SHIFT_UP);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_DOWN_MASK, false), SHIFT_DOWN);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_DOWN_MASK, false), SHIFT_LEFT);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_DOWN_MASK, false), SHIFT_RIGHT);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0, false), TEST);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0, false), FOG);

        ActionMap am = component.getActionMap();
        am.put(UP, new Input(UP));
        am.put(DOWN, new Input(DOWN));
        am.put(LEFT, new Input(LEFT));
        am.put(RIGHT, new Input(RIGHT));
        am.put(SHIFT_UP, new Input(SHIFT_UP));
        am.put(SHIFT_DOWN, new Input(SHIFT_DOWN));
        am.put(SHIFT_LEFT, new Input(SHIFT_LEFT));
        am.put(SHIFT_RIGHT, new Input(SHIFT_RIGHT));
        am.put(TEST, new Input(TEST));
        am.put(FOG, new Input(FOG));
	}
	
	// actions for map editing
	public static void addEditActions(JComponent component, MapEditTab handler) {
		editHandler = handler;
		
		InputMap im = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), EDIT_UP);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), EDIT_DOWN);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), EDIT_LEFT);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), EDIT_RIGHT);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_DOWN_MASK, false), EDIT_CARVE_UP);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_DOWN_MASK, false), EDIT_CARVE_DOWN);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_DOWN_MASK, false), EDIT_CARVE_LEFT);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_DOWN_MASK, false), EDIT_CARVE_RIGHT);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), EDIT_THIS_CELL);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0, false), EDIT_WALL);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0, false), EDIT_DOOR);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0, false), EDIT_HIDDEN_DOOR);

        ActionMap am = component.getActionMap();
        am.put(EDIT_UP, new Input(EDIT_UP));
        am.put(EDIT_DOWN, new Input(EDIT_DOWN));
        am.put(EDIT_LEFT, new Input(EDIT_LEFT));
        am.put(EDIT_RIGHT, new Input(EDIT_RIGHT));
        am.put(EDIT_CARVE_UP, new Input(EDIT_CARVE_UP));
        am.put(EDIT_CARVE_DOWN, new Input(EDIT_CARVE_DOWN));
        am.put(EDIT_CARVE_LEFT, new Input(EDIT_CARVE_LEFT));
        am.put(EDIT_CARVE_RIGHT, new Input(EDIT_CARVE_RIGHT));
        am.put(EDIT_THIS_CELL, new Input(EDIT_THIS_CELL));
        am.put(EDIT_WALL, new Input(EDIT_WALL));
        am.put(EDIT_DOOR, new Input(EDIT_DOOR));
        am.put(EDIT_HIDDEN_DOOR, new Input(EDIT_HIDDEN_DOOR));
	}
}
