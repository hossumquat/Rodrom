package com.rodrom.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.rodrom.dll.LoadIcons;

public class ItemTab extends JPanel {
	private static final long serialVersionUID = 1L;

	public BufferedImage image = null;

	public ItemTab() {
		image = LoadIcons.getImage(3);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g); // paint background

		if (image == null)
			return;

//		g.drawImage(image, 0, 0, null);
	}
}
