package com.rodrom.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.rodrom.DungeonView;
import com.rodrom.Game;

public class DungeonViewPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int MAX_SIZE = 2000;

	public JButton inc, dec;

	public static int size = 300;
	public int left, right;
	public BufferedImage view = null;
	public BufferedImage creature = null;

	public DungeonViewPanel() {
		super(false);
		setMinimumSize(new Dimension(200, 200));
		setPreferredSize(new Dimension(480, 480));
		
		if (creature == null) {
			File file = new File("images/creature.png");

			try {
				creature = ImageIO.read(file);

			} catch (IOException e) {
				creature = null;
				e.printStackTrace();
			}
		}
	}

	public static void setSize(int s) {
		size = (s / 100) * 100;
		if (size < 100)
			size = 100;
		else if (size > MAX_SIZE)
			size = MAX_SIZE;
	}
	
	public void drawCreature(Graphics g, int height) {
		int cWidth = creature.getWidth();
		int cHeight = creature.getHeight();

//		int width = cWidth * height / cHeight;
		int width = height * cWidth / cHeight;
		int x = getWidth() / 2 - width / 2;
		g.drawImage(creature, x, 0, x + width, height, 0, 0, cWidth, cHeight, null);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // paint background

		int width = getWidth();
		int height = getHeight();

		if (view != null) {
			if (view.getWidth() != width || view.getHeight() != height)
				view = null;
		}

		if (view == null)
			view = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		DungeonView.render(view, Game.map);
		g.drawImage(view, 0, 0, null);
		if (Game.inCombat)
			drawCreature(g, height);

		/*		DungeonView.constructLayouts(Game.map);
		int left = DungeonView.left;
		int right = DungeonView.right;
		System.out.println("repaint " + left + " " + right);

		if (left == -1)
			g.drawImage(empty, 0, 0, size / 2, size, 0, 0, 50, 100, Color.red, null);
		else
			g.drawImage(walls, 0, 0, size / 2, size, left * 50, 100, left * 50 + 50, 200, Color.red, null);

		if (right == -1)
			g.drawImage(empty, size / 2, 0, size, size, 50, 0, 100, 100, Color.red, null);
		else
			g.drawImage(walls, size / 2, 0, size, size, right * 50, 0, right * 50 + 50, 100, Color.red, null);*/
	}
}
