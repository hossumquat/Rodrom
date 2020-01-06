package com.rodrom.ui;

import javax.swing.JPanel;

public class SpellTab extends JPanel {
	private static final long serialVersionUID = 1L;

	public SelectLookAndFeel slaf;

	public SpellTab() {
		slaf = new SelectLookAndFeel();
		
		add(slaf.comboBox);
	}
}
