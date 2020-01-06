package com.rodrom.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class InfoTab extends JPanel {
	private static final long serialVersionUID = 1L;

	public JLabel line1, line2, line3, line4, line5;
	
	public InfoTab() {
		super(false);

		setLayout(new MigLayout());

		line1 = new JLabel("Line 1");
		line1.setAlignmentX(Component.CENTER_ALIGNMENT);

		line2 = new JLabel("Line 2");
		line2.setAlignmentX(Component.CENTER_ALIGNMENT);

		line3 = new JLabel("Line 3");
		line3.setAlignmentX(Component.CENTER_ALIGNMENT);

		line4 = new JLabel("Line 4");
		line4.setAlignmentX(Component.CENTER_ALIGNMENT);

		line5 = new JLabel("Line 5");
		line5.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createLineBorder(Color.blue, 5));
//		panel.setLayout(new BorderLayout());
		panel.add(line3);

		add(line1, "align center, wrap");
		add(line2, "align center, wrap");
		add(panel, "width :100%:, wrap");
		add(line4, "align center, wrap");
		add(line5, "align center, wrap");
	}
}
