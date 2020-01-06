package com.rodrom.ui;

import java.awt.Dimension;

import javax.swing.JComponent;

public class Gap extends JComponent {
	private static final long serialVersionUID = 1L;

	private static Dimension min = new Dimension(0, 0);
	private static Dimension max = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
	
	public Dimension getMinimumSize() {
		return min;
	}

	public Dimension getPreferredSize() {
		return min;
	}

	public Dimension getMaximumSize() {
		return max;
	}
}
